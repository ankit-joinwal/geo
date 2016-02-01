package com.geogenie.geo.service.transformers;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.geogenie.data.model.EventImage;
import com.geogenie.geo.service.exception.ServiceErrorCodes;
import com.geogenie.geo.service.exception.ServiceException;

public class MultipartToImageTransformer implements Transformer<EventImage,MultipartFile>{

	@Override
	public EventImage transform(MultipartFile file) throws ServiceException{
		EventImage eventImage = new EventImage();
		eventImage.setName(file.getOriginalFilename());
		try{
			eventImage.setData(file.getBytes());
		}catch(IOException ioException){
			ioException.printStackTrace();
			throw new ServiceException(ServiceErrorCodes.ERR_050, "Error while reading image data for {}"+file.getOriginalFilename());
		}
		return eventImage;
	}
}
