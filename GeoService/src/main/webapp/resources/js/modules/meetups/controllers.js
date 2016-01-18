'use strict';

angular.module('Home')

.controller('MeetupsController',
    ['$scope',"$http",'$routeParams','$location','$window','$facebook','MeetupService','AuthenticationService',
    function ($scope,$http,$routeParams,$location,$window,$facebook,MeetupService,AuthenticationService) {
    	
		//Function to navigate to CreateMeetup page
		$scope.newMeetup = function(){
			console.log('Inside MeetupsController.newMeetup');
			$location.path('/meetups/create');
			
		};

		$scope.initNewMeetup = function(){
			MeetupService.getMeetupTimeSlots(function(response){
			   if(response.status == 200){
				$scope.timeSlots = response.data;   
			   }
			});
		   
			var left = 500;
			$('#text_counter').text('Characters left: ' + left);

				$('#desc').keyup(function () {

				left = 500 - $(this).val().length;

				if(left < 0){
					$('#text_counter').addClass("overlimit");
					 
				}else{
					$('#text_counter').removeClass("overlimit");
					
				}

				$('#text_counter').text('Characters left: ' + left);
			});
	   };
	   
	   $scope.initEditMeetup = function(){
			console.log('Inside initEditMeetup  ');
		
			 var meetupId = $routeParams.meetupId;
			 MeetupService.getMeetup(meetupId,function(response){
				 if(response.status == 200){
					 console.log('Found Meetup');
					 $scope.meetup = response.data;
					 if($.isArray($scope.meetup.attendees) && $scope.meetup.attendees.length){
						 $scope.attendeesExist = true;
					 }
					//Check if current user is organizer , only then allow inviting button
					var userProfile = {};
					AuthenticationService.getUserProfile(function (getProfileResponse){
						if(getProfileResponse.status == 200){
							userProfile = getProfileResponse.data;
							console.log("userProfile.email = "+userProfile.email);
							console.log("Organizer Id "+response.data.organizer.emailId);
							if(response.data.organizer.emailId == userProfile.email){
								$scope.isOrganizerLogin = true;
								$scope.getFriends();
							}
						}else{
							console.log('Unable to get User Profile from cookies');
						}
					});
					
					 
				 }else{
					 alert('Meetup Not Found');
				 }
			 });
	   };
	  
		//Function to get Facebook Friends
    	$scope.getFriends = function() {
		
		  $scope.loginStatus = $facebook.isConnected();
		  if(!$scope.loginStatus){
			  //$facebook.login();
			  alert('Please login via facebook');
			  return;
			}
			console.log('inside getFriends');
			  $facebook.api('/me/friends').then(function(friends) {
				  console.log('Friends.data :'+ JSON.stringify(friends.data));
				$scope.foundfriends = true;
				//$scope.fbfriends = friends.data;
				
				var friendsData = [];
				$(friends.data).each(function(idx, user){ 
					
					$facebook.api(user.id+'/picture').then(function(response){
						var friend = {"id":user.id , "name":user.name, "pic":response.data.url};
						friendsData.push(friend);
						
					});
				});
				$scope.fbfriends = friendsData;
				
			  });
		};
		
		
		$scope.inviteFriends = function(){
				
				
				var attendees = [];
			
				var friends = $scope.fbfriends;
				
				$(friends).each(function(idx, friend){ 
				
					var attendee = '{'
									+	 '"social_detail": {'
									+		'"system": "FACEBOOK",'
									+		'"detail": "'+friend.id+'",'
									+		'"detailType": "USER_EXTERNAL_ID"'
									+	  '},'
									+	  '"response": "NO",'
									+	  '"is_admin": "false"'
									+'}';
					attendees.push(attendee);
					
				});
				var meetupId = $scope.meetup.uuid;
			
				MeetupService.addAttendees(meetupId,attendees,function(response){
					
					if(response.status == 200){
						console.log('Edit Meetup Successful');
						var url = encodeURI($window.location);
						console.log('$window.location ='+url);
						$window.FB.ui({
						  method: 'send',
						  link: url,
						}, function(shareResponse){
							$scope.meetup.attendees = response.data.attendees;
							$scope.attendeesExist = true;
							$scope.foundfriends = false;
							$scope.$apply();
						});
					}
				});
				
				
				
			};
		
		//Function to new create Meetup
		$scope.createMeetup = function(){
			
			$scope.loginStatus = $facebook.isConnected();
			if(!$scope.loginStatus){
			  //$facebook.login();
			  alert('Please login via facebook');
			  return;
			}
			
			
			var title = $scope.title;
			var desc = $scope.desc;
			var startDate = $('#startDate').val();
			var startTime = $scope.startTime;
			var endDate = $('#endDate').val();
			var endTime = $scope.endTime;
			
			var userProfile = {};
			AuthenticationService.getUserProfile(function (response){
				if(response.status == 200){
					userProfile = response.data;
				}else{
					console.log('Unable to get User Profile from cookies');
				}
				
			});
			
			//Get Location
			var locationName = $scope.chosenPlace;
			var locLat = $scope.meetup_place_lat;
			var locLng = $scope.meetup_place_lng;
			var location = '{"name": "'+locationName + '" ,"longitude" :"'+locLng+'" ,"lattitude" : "'+locLat+'"}';
			
			console.log('Create Meetup Data :');
			var attendees = [];
			/* Not allowing user to invite friends while creating meetup.
			This will be available in Edit Meetup screen.
			var friends = $scope.fbfriends;
			$(friends).each(function(idx, friend){ 
				attendees.push('"'+friend.id+'"');
			});
			*/
			console.log('Title:'+title);
			console.log('Description:'+desc);
			console.log('Location Name :'+locationName);
			console.log('Location Lat:'+locLat);
			console.log('Location Lng:'+locLng);
			console.log('User Profile :'+JSON.stringify(userProfile));
			console.log('StatDate:'+startDate);
			console.log('startTime:'+startTime);
			console.log('endDate:'+endDate);
			console.log('endTime:'+endTime);
			
			
			MeetupService.createMeetup(title,desc,userProfile.email,location,startDate,startTime,endDate,endTime,attendees,function(response){
				if(response.status == 201){
					
					console.log('Meetup Id : '+response.data.uuid);
					
					$location.path('meetups/'+response.data.uuid);
				}
			});
			
		};
		
		$scope.updateResponse = function($event, id) {
		  var checkbox = $event.target;
		  var action = (checkbox.checked ? 'YES' : 'NO');
		  var meetupId = $scope.meetup.uuid;
		  MeetupService.saveAttendeeResponse(meetupId,id,action,function(response){
			  if(response.status == 200){
				  alert('Response saved successfully');
			  }
			  
		  });
		};
    }]);