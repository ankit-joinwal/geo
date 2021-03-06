package com.geogenie.data.model.requests;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.geogenie.data.model.EventImage;
import com.geogenie.data.model.EventTag;
import com.geogenie.data.model.Location;
import com.geogenie.data.model.User;
import com.geogenie.data.model.ext.PlaceDetails;

@XmlRootElement(name="createEvent")
@XmlAccessorType(XmlAccessType.NONE)
public class CreateEventRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotNull(message="error.title.mandatory")
	private String title;
	
	@NotNull(message="error.description.mandatory")
	private String description;
	
	private EventImage image;
	
	@NotNull(message="error.event.details.mandatory")
	private MockEventDetails eventDetails;
	
	public static final class MockEventDetails{

		private Location location;

		private User organizer;
		
		
		private Set<PlaceDetails.Result.AddressComponent> addressComponents = new HashSet<>();
		
		public Set<PlaceDetails.Result.AddressComponent> getAddressComponents() {
			return addressComponents;
		}

		public void setAddressComponents(Set<PlaceDetails.Result.AddressComponent> addressComponents) {
			this.addressComponents = addressComponents;
		}

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

		public User getOrganizer() {
			return organizer;
		}

		public void setOrganizer(User organizer) {
			this.organizer = organizer;
		}

	}
	
	@NotNull(message="error.start.date.mandatory")
	private String startDate;
	
	@NotNull(message="error.end.date.mandatory")
	private String endDate;
	
	@NotNull(message="error.event.tags.mandatory")
	private Set<EventTag> tags = new HashSet<>();
	
	@NotNull(message="error.event.organizer.mandatory")
	private String organizerId;

	
	public Set<EventTag> getTags() {
		return tags;
	}

	public void setTags(Set<EventTag> tags) {
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EventImage getImage() {
		return image;
	}

	public void setImage(EventImage image) {
		this.image = image;
	}

	public MockEventDetails getEventDetails() {
		return eventDetails;
	}

	public void setEventDetails(MockEventDetails eventDetails) {
		this.eventDetails = eventDetails;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getOrganizerId() {
		return organizerId;
	}

	public void setOrganizerId(String organizerId) {
		this.organizerId = organizerId;
	}

	@Override
	public String toString() {
		return "CreateEventRequest [title="+this.title+ " , description="+this.description
				+" , eventDetails="+this.eventDetails+" , image="+this.image+ " , start="+this.startDate+" , ends="+this.endDate+ " ]";
	}
	
}
