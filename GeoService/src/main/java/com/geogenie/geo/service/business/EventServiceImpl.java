package com.geogenie.geo.service.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.geogenie.Constants;
import com.geogenie.data.model.AddressComponentType;
import com.geogenie.data.model.CreateEventRequest;
import com.geogenie.data.model.CreateEventRequest.MockEventDetails;
import com.geogenie.data.model.Event;
import com.geogenie.data.model.EventAddressInfo;
import com.geogenie.data.model.EventDetails;
import com.geogenie.data.model.EventImage;
import com.geogenie.data.model.EventListResponse;
import com.geogenie.data.model.EventTag;
import com.geogenie.data.model.EventType;
import com.geogenie.data.model.User;
import com.geogenie.data.model.ext.PlaceDetails;
import com.geogenie.data.model.ext.PlaceDetails.Result.AddressComponent;
import com.geogenie.geo.service.dao.EventDAO;
import com.geogenie.geo.service.dao.EventTagDAO;
import com.geogenie.geo.service.dao.EventTypeDAO;
import com.geogenie.geo.service.dao.MeetupDAO;
import com.geogenie.geo.service.dao.UserDAO;
import com.geogenie.geo.service.exception.ServiceErrorCodes;
import com.geogenie.geo.service.exception.ServiceException;
import com.geogenie.geo.service.transformers.Transformer;
import com.geogenie.geo.service.transformers.TransformerFactory;
import com.geogenie.geo.service.transformers.TransformerFactory.Transformer_Types;

@Service
@Transactional
public class EventServiceImpl implements EventService {

	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private EventDAO eventDAO;
	
	@Autowired
	private EventTagDAO eventTagDAO;
	
	@Autowired
	private EventTypeDAO eventTypeDAO;
	
	@Autowired
	private MeetupDAO meetupDAO;
	
	@Override
	public Event create(CreateEventRequest createEventRequest) {
		logger.info("### Inside CreateEventRequest.create ###");
		Event event = new Event();
		MockEventDetails mockEventDetails = createEventRequest.getEventDetails();
		
		
		User organizer = this.userDAO.getUserByEmailId(createEventRequest.getOrganizerId(), false); 
		logger.info("   Found organizer details in DB for {} : Id {}",organizer.getEmailId(),organizer.getId());
		
		Set<EventTag> tags = createEventRequest.getTags();
		if(tags!=null && !tags.isEmpty()){
			List<String> tagNames = new ArrayList<>();
			for(EventTag eventTag : tags){
				tagNames.add(eventTag.getName());
			}
			List<EventTag> tagsInDB = eventTagDAO.getTagsByNames(tagNames);
			event.setTags(new HashSet<>(tagsInDB));
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.MEETUP_DATE_FORMAT);
		try {
			event.setStartDate(dateFormat.parse(createEventRequest.getStartDate()));
			event.setEndDate(dateFormat.parse(createEventRequest.getEndDate()));
		} catch (ParseException e) {

			logger.error("ParseException",e);
		}
		
		EventDetails eventDetails = new EventDetails();
		eventDetails.setLocation(mockEventDetails.getLocation());
		eventDetails.setOrganizer(organizer);
		event.setTitle(createEventRequest.getTitle());
		event.setDescription(createEventRequest.getDescription());
		event.setEventDetails(eventDetails);
		eventDetails.setEvent(event);
		Event created = this.eventDAO.create(event);
		created.getEventDetails().setAddressComponents(this.getEventAddressInfo(eventDetails,mockEventDetails.getAddressComponents()));
		this.eventDAO.saveEvent(created);
		return created;
	}
	
	private Set<EventAddressInfo> getEventAddressInfo(EventDetails eventDetails,Set<PlaceDetails.Result.AddressComponent> addressComponents){
		List<AddressComponentType> addressComponentTypes = this.meetupDAO.getAddressTypes();
		logger.info("Inside getEventAddressInfo. addressComponentTypes : {}  ",addressComponentTypes);
		Map<String,AddressComponentType> addressComponentTypesMap = new HashMap<>();
		for(AddressComponentType addressComponentType: addressComponentTypes){
			addressComponentTypesMap.put(addressComponentType.getName(), addressComponentType);
		}
		Set<EventAddressInfo> eventAddresses = new HashSet<>();
		for(AddressComponent addressComponent : addressComponents){
			logger.info("Address Component {} "+addressComponent.getLongName());
			List<String> types = addressComponent.getTypes();
			logger.info("Types : {} ",types);
			for(String type : types){
				if(addressComponentTypes.contains(new AddressComponentType(type))){
					logger.info("Address component type found : {}",type);
					
					AddressComponentType addressComponentType = addressComponentTypesMap.get(type);
					EventAddressInfo eventAddressInfo  = new EventAddressInfo();
					eventAddressInfo.setAddressComponentType(addressComponentType);
					eventAddressInfo.setValue(addressComponent.getLongName());
					eventAddressInfo.setEventDetails(eventDetails);
					eventAddresses.add(eventAddressInfo);
				
					continue;
				}
			}
		}
		
		logger.info("Event Address Components : " + eventAddresses);
		
		return eventAddresses;
	}
	
	@Override
	public Event get(String uuid) {
		
		return this.eventDAO.getEvent(uuid);
	}
	
	@Override
	public void makeEventLive(String eventId) {
		logger.info("### Inside Make vent Live ###");
		this.eventDAO.makeEventLive(eventId);
		
		
	}
	
	@Override
	public EventListResponse getEventsInCity(String city, String country) throws ServiceException{
		logger.info("### Inside getEventsInCity . City {} , Country {} ###",city,country);
		return this.eventDAO.getEventsBasedOnCityAndCountry(city, country);
		
	}
	
	@Override
	public EventListResponse getEventsByType(String eventTypeName, String city,
			String country)throws ServiceException {

		logger.info("### Inside getEventsByType .Type {}, City {} , Country {} ###",eventTypeName,city,country);
		EventType eventType = this.eventTypeDAO.getEventTypeByName(eventTypeName);
		EventListResponse eventsResponse = null;
		if(eventType!=null){
			logger.info("Found Event Type by name {}",eventTypeName);
			Set<EventTag> tags = eventType.getRelatedTags();
			
			List<Long> tagIds = new ArrayList<Long>(tags.size());
			for(EventTag eventTag : tags){
				tagIds.add(eventTag.getId());
			}
			eventsResponse = this.eventDAO.getEventsBasedOnTags(tagIds, city, country);
		}else{
			eventsResponse = new EventListResponse();
		}
		return eventsResponse;
	}
	
	@Override
	public void storeEventImages(List<MultipartFile> images, String eventId) throws ServiceException{
		logger.info("### Inside EventServiceImpl.storeEventImages ###");
		 List<EventImage> eventImages = new ArrayList<>();
		 Event event = this.eventDAO.getEventWithoutImage(eventId);
		 if(event==null){
			 throw new ServiceException(ServiceErrorCodes.ERR_001,"No event found with id "+eventId);
		 }
		 int displayOrder = 1;
         for(MultipartFile multipartFile : images){
      	   logger.info("File to process : {} ",multipartFile.getOriginalFilename());
      	   logger.info("File size : {} ", multipartFile.getSize());
      	   Transformer<EventImage, MultipartFile> transformer = 
      			   (Transformer<EventImage, MultipartFile>)TransformerFactory.getTransformer(Transformer_Types.MULTIPART_TO_IMAGE_TRANFORMER);
      	   try{
      		   EventImage eventImage = transformer.transform(multipartFile);
      		   eventImage.setEvent(event);
      		   eventImage.setDisplayOrder(displayOrder);
      		   eventImages.add(eventImage);
      		   displayOrder++;
      	   }catch(ServiceException serviceException){
      		   logger.error("Error occurred while processing event image",serviceException);
      	   }
         }
         if(!eventImages.isEmpty()){
        	 this.eventDAO.saveEventImages(eventImages);
         }
	}
	
}
