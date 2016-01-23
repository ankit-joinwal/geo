'use strict';

angular.module('Home')

.controller('MeetupsController',
    ['$rootScope','$scope',"$http",'$routeParams','$location','$window','$facebook','MeetupService','AuthenticationService',
    function ($rootScope,$scope,$http,$routeParams,$location,$window,$facebook,MeetupService,AuthenticationService) {
    	
		//Function to navigate to CreateMeetup page
    	//TODO:Ask users to be logged in before creating meetup.
		$scope.newMeetup = function(){
			console.log('Inside MeetupsController.newMeetup');
			$location.path('/meetups/create');
		};
		
		//TODO: Ask users to be logged in before creating meetup.
		$scope.newMeetupAtPlace = function(){
			console.log('Inside MeetupsController.newMeetup');
			var placeGeometry = $scope.placeGeometry;
			if(typeof placeGeometry !== 'undefined'){
				console.log('Inside newMeetupAtPlace. Place Geometry :'+JSON.stringify(placeGeometry));
				$rootScope.chosenPlace = placeGeometry.name;
				$rootScope.meetup_place_lat = placeGeometry.lat;
				$rootScope.meetup_place_lng = placeGeometry.lng;
			}
			$location.path('/meetups/create');
			
		};
		
		//TODO: Currently place details are not accurate. It is opening anything.
		//Make use of place address components .
		$scope.openMap = function(){
			var place = $scope.chosenPlace;
			var placeLat = $rootScope.meetup_place_lat;
			var placeLng = $rootScope.meetup_place_lng;
			if(typeof place !== 'undefined'){
				var url = "http://maps.google.com/?q="+placeLat+","+placeLng;
				$window.open(url);
			}
		};

		//Function to init new meetup.
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
	   
	   //Function to display meetup after creation.
	   //TODO: Allow organizer to edit meetup.
	   //TODO: If user is neither organizer nor attendee , then do not give access.
	   $scope.initEditMeetup = function(){
		   //This is called after meetup is created successfully.
		   console.log('Inside initEditMeetup  ');
			//Check if user is logged in.
		   //This is required when attendees open the 'Meetup' link sent to them via FB or Push .
		   //To identify which attendee is accessing meetup, we will ask them to login before editing anything on meetup .
			var loginStatus = false;
			AuthenticationService.isUserLoggedIn(function(authStatus){
			   if(authStatus.status == 200){
				   loginStatus = true;
			   }
			});
			//If user is not logged in, we redirect to home page where they can login first and then will be automatically taken to this meetup.
			if(!loginStatus){
				console.log('User not login. Redirecting to login');
				var currLoc = $location.path();
				var newPath = '/rd'+currLoc;
				$location.path(newPath);
			}
			//After user is logged in , display meetup information.
			 var meetupId = $routeParams.meetupId;
			 //To Get Meetup information from uuid.
			 MeetupService.getMeetup(meetupId,function(response){
				 //If Meetup Found.
				 if(response.status == 200){
					 console.log('Found Meetup');
					 $scope.meetup = response.data;
					 //Reinitialize Message Box on meetup page. Otherwise it will display old value.
					 $scope.postMessage = "";
					 //Check if attendees exist for meetup or not.
					 //Based on yes/no diplay appropriate UI
					 if($.isArray($scope.meetup.attendees) && $scope.meetup.attendees.length){
						 $scope.attendeesExist = true;
					 }
					 //Check if messages exist for meetup or not.
					 //Based on yes/no display appropriate UI.
					 //TODO:Pagination for messages.
					 if($.isArray($scope.meetup.messages) && $scope.meetup.messages.length){
						 $scope.messagesPresent = true;
					 }
					 //We need to identify whether logged in user is organizer or subadmin.
					 //Only organizer or sub admins shloud be allowed to invite people for this meetup.
					 //To Identify, check Meetup.organizer.email with loggedinuser.email
					 //TODO: Sub admin functionality is not implemented right now.
					 var userProfile = {};
					 AuthenticationService.getUserProfile(function (getProfileResponse){
						if(getProfileResponse.status == 200 && (typeof getProfileResponse.data !== 'undefined')){
							//Get user profile stored in cookies to get user email id.
							userProfile = getProfileResponse.data;
							
							console.log("userProfile.email = "+userProfile.email);
							console.log("Organizer Id "+response.data.organizer.emailId);
							
							if(response.data.organizer.emailId == userProfile.email){
								$scope.isOrganizerLogin = true;
								//If organizer or sub admin login, automatically get friends of organizers to select.
								//TODO: 
								//1.Give user option to invite from FB or G+
								//2.Do not pre-populate friends on page.
								//3.Check which friends are already invited.(They should be visible or not?)
								$scope.loginStatus = $facebook.isConnected();
								if(!$scope.loginStatus){
									 AuthenticationService.promptUserToLogin("FACEBOOK").then(function(authResponse){
										console.log('Inside LoginController.fbLoginToggle Response :'+authResponse.status);
										$scope.getFriends();
									});
								}else{
									$scope.getFriends();
								}
								
							}else{
								//Case when user is not organizer.
								//TODO: Do not display invite friends tab.
								//Only show attendees and messages tab.
							}
						}else{
							console.log('Unable to get User Profile from cookies');
						}
					});
				 }else{
					 //TODO: If meetup not found redirect to error page.
					 //Meetup either doesnt exist or organizer has cancelled it.
					 alert('Meetup Not Found');
				 }
			 });
	   };
	  
		//Function to get Facebook Friends.
	   //TODO: Currently this API return only those friends which are using our APP.
	   //If after making APP public, all friends are retrieved in this API we need following 
	   //1.Pagination to diaplay friends
	   //2.If those users are attendees which are not there in APP, how to handle their case? Bcoz they need to be registered first.
	   //If after making APP public, only those friends are returned by this API which use our APP , then how to reach to those friends which are not using our APP.
	   //Suggestion : Allow user to invite friends using Facebook Dialog. 
	   //Dialog should contain link to this meetup url.
    	$scope.getFriends = function() {
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
		
		//Function to invite friends for meetup.
		//First call our API to add attendees to this meetup.
		//Then this has following points to cater.
		//TODO:
		//IMP: Currenlty check boxes are ugnored and all friends are being invited.
		//Only selected friends should be invited.
		//1.Use Push notifications for inviting people.
		//2.For those friends which are not using our app, they should be notified using FB dialog.
		//3.Allow attendees to be added any time not only just after meetup creation.
		//4.Here also , check whether user is logged in or not. If logged in , whether user is organizer or sub admin?
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
									+	  '"is_admin": "false" ,'
									+	  '"name":"'+friend.name+'"'
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
		//TODO : Validation are missing.
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
		
		//This function is used to update response of users for meetup.
		//TODO:
		//Check here that user is logged in or not?
		//If not logged in then redirect to login.
		//If logged in , he should be allowed to update own response only.
		//Allow comments for responses.
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
		
		//Function to post a message to meetup.
		//TODO:
		//1.Message should be refreshed on screen after posting is done.
		//2.All attendees should be notified(be careful not to irritate people to uninstall APP).
		//3.Display profile pic also.
		$scope.postMessageToMeetup = function(){
			
			var isUserLogIn = true;
			AuthenticationService.isUserLoggedIn(function(loginCheckResponse){
				if(loginCheckResponse.status != 200){
					console.log('MeetupsController.initEditMeetup : User not logged in.');
					isUserLogIn = false;
				}
			});
			if(!isUserLogIn){
				console.log('Calling AuthenticationService.promptUserToLogin');
				AuthenticationService.promptUserToLogin("FACEBOOK",function(authResponse){
			
				});
			}
			var meetupId = $scope.meetup.uuid;
			var userProfile = {};
			AuthenticationService.getUserProfile(function (response){
				if(response.status == 200){
					userProfile = response.data;
				}else{
					console.log('Unable to get User Profile from cookies');
				}
			});
			var socialId = userProfile.socialDetail;
			var message = $scope.postMessage;
			console.log('Message :'+message);
			var postSuccessFull = true;
			MeetupService.postMessageToMeetup(meetupId,socialId,message,function(postMessageResp){
				if(postMessageResp.status == 200){
					console.log('Post Message success');
				}else{
					postSuccessFull = false;
				}
			});
			if(postSuccessFull){
				MeetupService.getMeetupMessages(meetupId,function(getMsgsResp){
					if(getMsgsResp.status == 200){
						$scope.postMessage = "";
						$scope.meetup.messages = getMsgsResp.data;
						$scope.messagesPresent = true;
					}
				});
			}
		};
    }]);