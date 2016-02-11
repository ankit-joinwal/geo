package com.geogenie.geo.service.transformers;

import java.util.ArrayList;
import java.util.List;

import com.geogenie.data.model.User;
import com.geogenie.data.model.UserFriend;

public class UsersToFriendsTransformer implements Transformer<List<UserFriend>, List<User>>{

	public List<UserFriend> transform(List<User> users) throws com.geogenie.geo.service.exception.ServiceException {
		List<UserFriend> userFriends = new ArrayList<>();
		if(users!=null && !users.isEmpty()){
			for(User user : users){
				UserFriend userFriend = new UserFriend();
				userFriend.setEmailId(user.getEmailId());
				userFriend.setName(user.getName());
				userFriend.setProfilePic(user.getProfilePictureURL());
				userFriends.add(userFriend);
			}
		}
		return userFriends;
		
	};
}
