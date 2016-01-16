'use strict';

angular.module('Home')

.factory('MeetupService',
	['$http',function($http){
		
		var service = {};
		
		service.createMeetup = function(title,description,organizerId,location,startDate,startTime,endDate,endTime,attendees,callback ){
			var startDateTime = startDate + ' '+ startTime;
			var endDateTime = endDate +  ' ' + endTime;
			
			var postData = '{ 	"title" : "' +title+ '",'+
								'"description" : "' + description+'", '+
								'"location" : ' +location+', '+
								'"startDate" : "' +startDateTime + '",'+
								'"endDate" : "' +endDateTime + '", '+
								'"organizerId" : "' +organizerId + '" , '+
								'"attendees" : ['+ attendees+
							']}';
			console.log('Request ody for create meetup = '+JSON.stringify(postData));
			$http({
				method:'POST',
				url: '/GeoService/api/public/meetups',
	            data: postData,
	            headers: {
	                    "Content-Type": "application/json",
						"accept":"application/json",
	                    "X-Login-Ajax-call": 'true'
	            }
			}).then(function(response) {
                if (response.status == 201) {
                	console.log('Create meetup successfull-'+response.status);
                	 callback(response);
                }
                else {
                  alert("Create meetup failed");
                }
            });
			
			
		};
	
		 return service;
	}
	]);