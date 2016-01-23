'use strict';

angular.module('Home')

.factory('EventService',
	['$http','$rootScope',function($http,$rootScope){
		
		var service = {};
		
		service.getEventTimeSlots = function(callback){
			var response = {};
			var slots = ["12:00 AM","12:30 AM","01:00 AM","01:30 AM","02:00 AM","02:30 AM","03:00 AM","03:30 AM","04:00 AM","04:30 AM","05:00 AM","05:30 AM",
						"06:00 AM","06:30 AM","07:00 AM","07:30 AM","08:00 AM","08:30 AM","09:00 AM","09:30 AM","10:00 AM","10:30 AM","11:00 AM","11:30 AM",
						"12:00 PM","12:30 PM","01:00 PM","01:30 PM","02:00 PM","02:30 PM","03:00 PM","03:30 PM","04:00 PM","04:30 PM","05:00 PM","05:30 PM",
						"06:00 PM","06:30 PM","07:00 PM","07:30 PM","08:00 PM","08:30 PM","09:00 PM","09:30 PM","10:00 PM","10:30 PM","11:00 PM","11:30 PM"
						];
			response.status = 200;
			response.data = slots;
			callback(response);
		};
		
		
	
		service.getAllTags = function(callback){
			console.log('inside EventService.getAllTags ');
			$http({
				method:'GET',
				url: '/GeoService/api/public/events/tags',
				headers: {
						"X-Login-Ajax-call": 'true',
						"Accept" : "application/json"
				}
			}).then(function(response) {
			   
			  callback(response);
			});
		};
		
		service.createEvent = function(title,description,organizerId,location,startDate,startTime,endDate,endTime,tags,callback ){
			var startDateTime = startDate + ' '+ startTime;
			var endDateTime = endDate +  ' ' + endTime;
			var postData = '{ 	"title" : "' +title+ '",'+
								'"description" : "' + description+'", '+
								'"eventDetails" : {'+
								'					"location" : ' +location+' '+
								'				 },'+
								'"startDate" : "' +startDateTime + '",'+
								'"endDate" : "' +endDateTime + '", '+
								'"organizerId" : "' +organizerId + '" , '+
								'"tags"		:	'+tags+
							'}';
							
			
			
			console.log('Request ody for create event = '+JSON.stringify(postData));
			$http({
				method:'POST',
				url: '/GeoService/api/public/events',
	            data: postData,
	            headers: {
	                    "Content-Type": "application/json",
						"accept":"application/json",
	                    "X-Login-Ajax-call": 'true'
	            }
			}).then(function(response) {
                if (response.status == 201) {
                	console.log('Create Event successfull-'+response.status);
                	 callback(response);
                }
                else {
                  alert("Create event failed");
                }
            });
			
		};
		
		service.getEvent = function(eventId, callback){
			console.log('inside EventService.getEvent for '+eventId);
			$http({
				method:'GET',
				url: '/GeoService/api/public/events/'+eventId,
				headers: {
						"X-Login-Ajax-call": 'true',
						"Accept" : "application/json"
				}
			}).then(function(response) {
			   
			  callback(response);
			});
		};
		
		 return service;
		
	}
	]);