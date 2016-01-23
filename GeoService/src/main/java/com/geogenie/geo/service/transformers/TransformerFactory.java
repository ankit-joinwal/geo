package com.geogenie.geo.service.transformers;

public class TransformerFactory {
	
	public enum Transformer_Types{
		MEETUP_TRANS , EVENT_TRANS
	}
	
	public static Transformer<?, ?> getTransformer(Transformer_Types types){
		if(types.equals(Transformer_Types.MEETUP_TRANS)){
			return new MeetupTransformer();
		}if(types.equals(Transformer_Types.EVENT_TRANS)){
			return new EventTransformer();
		}
		
		throw new IllegalArgumentException("Wrong input to factory");
	}
}
