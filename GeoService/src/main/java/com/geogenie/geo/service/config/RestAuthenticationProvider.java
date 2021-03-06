package com.geogenie.geo.service.config;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import com.geogenie.Constants;
import com.geogenie.data.model.Role;
import com.geogenie.data.model.SmartDevice;
import com.geogenie.data.model.SocialDetailType;
import com.geogenie.data.model.User;
import com.geogenie.data.model.UserSocialDetail;
import com.geogenie.geo.service.business.UserService;
import com.geogenie.geo.service.exception.ClientException;
import com.geogenie.geo.service.exception.RestErrorCodes;
import com.geogenie.geo.service.exception.ServiceException;
import com.geogenie.geo.service.exception.UnauthorizedException;
import com.geogenie.geo.service.security.RestCredentials;
import com.geogenie.geo.service.security.RestToken;
import com.geogenie.geo.service.utils.LoginUtil;

@Component("authenticationProvider")
public class RestAuthenticationProvider implements AuthenticationProvider,
		Constants {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(RestAuthenticationProvider.class);
	@Autowired
	private UserService userService;

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		LOGGER.info("### Inside authenticate method of AuthenticationProvider ###");

		RestToken restToken = (RestToken) authentication;
		LOGGER.info("RestToken = {}", restToken);

		String secret = restToken.getPrincipal();
		LOGGER.info("Principal = {}", secret);
		RestCredentials credentials = restToken.getCredentials();
		LOGGER.info("Signature = {}", credentials.getSignature());
		String[] secretParts = LoginUtil.getUsernameParts(secret);
		if (secretParts == null || secretParts.length != 2) {
			throw new BadCredentialsException(ERROR_LOGIN_INVALID_CREDENTIALS);
		}
		String userType = secretParts[0];

		LOGGER.info("UserType = {}", userType);
		if (userType.equals(Constants.DEVICE_PREFIX)) {
			String deviceId = secretParts[1];
			String deviceKey = null;
			List<Role> userRoles = null;
			try {
				SmartDevice device = this.userService
						.getSmartDeviceDetails(deviceId);
				deviceKey = device.getPrivateKey();
			} catch (ClientException exception) {
				throw new BadCredentialsException(exception.getMessage());
			}

			String signature = calculateSignature(deviceKey,
					restToken.getTimestamp());
			LOGGER.info("Signature Calculated = {}", signature);
			// check if signatures match
			if (!credentials.getSignature().equals(signature)) {
				throw new BadCredentialsException(
						ERROR_LOGIN_INVALID_CREDENTIALS);
			}
			List<GrantedAuthority> authorities = new ArrayList<>();
			try {
				userRoles = this.userService.getUserRolesByDevice(deviceId);
			} catch (ServiceException exception) {
				throw new BadCredentialsException(exception.getMessage());
			}
			authorities = convertRoleToGA(userRoles);
			// this constructor create a new fully authenticated token, with the
			// "authenticated" flag set to true
			// we use null as to indicates that the user has no authorities. you
			// can change it if you need to set some roles.
			restToken = new RestToken(secret, credentials,
					restToken.getTimestamp(), authorities);
		} else if (userType.equals(Constants.WEB_USER_PREFIX)) {
			String userId = secretParts[1];
			User user = null;
			String externalId = null;

			LOGGER.info("Loading user with id {}" + userId);
			user = this.userService.loadUserByUsername(userId);

			Set<UserSocialDetail> socialDetails = user.getSocialDetails();
			if (socialDetails == null || socialDetails.isEmpty()) {
				LOGGER.error("User Social Details are null ");
				throw new UnauthorizedException(RestErrorCodes.ERR_002,
						ERROR_LOGIN_INVALID_CREDENTIALS);
			}
			for (UserSocialDetail socialDetail : socialDetails) {
				if (socialDetail.getSocialDetailType().equals(
						SocialDetailType.USER_EXTERNAL_ID)) {
					externalId = socialDetail.getUserSocialDetail();
				}
			}
			if (externalId == null) {
				LOGGER.error("Social details are missing for user");
				throw new UnauthorizedException(RestErrorCodes.ERR_002,
						ERROR_LOGIN_INVALID_CREDENTIALS);
			}

			String signature = calculateSignature(externalId,
					restToken.getTimestamp());
			if (!credentials.getSignature().equals(signature)) {
				throw new BadCredentialsException(
						ERROR_LOGIN_INVALID_CREDENTIALS);
			}
			List<Role> roles = new ArrayList<>(user.getUserroles());
			if (roles == null || roles.isEmpty()) {
				throw new BadCredentialsException(ERROR_LOGIN_USER_UNAUTHORIZED);
			}
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities = convertRoleToGA(roles);
			// this constructor create a new fully authenticated token, with the
			// "authenticated" flag set to true
			// we use null as to indicates that the user has no authorities. you
			// can change it if you need to set some roles.
			restToken = new RestToken(secret, credentials,
					restToken.getTimestamp(), authorities);

		}

		return restToken;
	}

	private List<GrantedAuthority> convertRoleToGA(List<Role> userRoles) {

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (Role role : userRoles) {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
					role.getUserRoleType());
			grantedAuthorities.add(grantedAuthority);
		}
		return grantedAuthorities;
	}

	public boolean supports(Class<?> authentication) {
		return RestToken.class.equals(authentication);
	}

	private String calculateSignature(String secret, Long timeStamp) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(),
					"HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);
			String timeStampStr = timeStamp + "";
			byte[] rawHmac = mac.doFinal(timeStampStr.getBytes());
			String result = new String(Base64.encode(rawHmac));
			return result;
		} catch (GeneralSecurityException e) {
			throw new IllegalArgumentException();
		}
	}
}
