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
			var friends = $scope.fbfriends;
			$(friends).each(function(idx, friend){ 
				attendees.push('"'+friend.id+'"');
			});
			
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
					console.log('Create Meetup Successfull');
				}
			});
			
		};
		
		
    }]);