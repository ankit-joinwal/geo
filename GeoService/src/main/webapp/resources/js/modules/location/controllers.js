'use strict';

var app = angular.module('Home');


  
  app.controller('LocationController',
    ['$location','$rootScope','$scope',"$http",'LocationService',
    function ($location,$rootScope,$scope,$http,LocationService) {
		console.log('Inside LocationController');
    	$scope.auto_location_error = "";
		
		$scope.showLocation = function () {
			console.log('$scope.auto_location_error'+ $scope.auto_location_error);
			if($scope.auto_location_error == ""){
				if($scope.locality==""){
					$scope.getLocation();
				}
				return true;
			}else{
					return false;
			}
            
        }
 
		$scope.showPosition = function (position) {
            $scope.user_lat = position.coords.latitude;
            $scope.user_lng = position.coords.longitude;
			
			LocationService.cnvrtCordToAddress($scope.user_lat, $scope.user_lng, function (response) {
				console.log('Inside callback of LocationController. Response status '+response.status);
				if (response.status==200) {
					console.log('Inside LocationController ... Recieved success from Location Service');
					
					$scope.locality = response.data;
				} else {
					console.log('Inside LocationController ... Recieved failure from Location Service');
					$scope.auto_location_error = response.data ;
				}
			});
			
			LocationService.storeUserLocInCookies($scope.user_lat,$scope.user_lng,$scope.locality );
            $scope.$apply();
 
            
        }
		
		$scope.showError = function (error) {
            switch (error.code) {
                case error.PERMISSION_DENIED:
                    $scope.auto_location_error = "User denied the request for Geolocation."
                    break;
                case error.POSITION_UNAVAILABLE:
                    $scope.auto_location_error = "Location information is unavailable."
                    break;
                case error.TIMEOUT:
                    $scope.auto_location_error = "The request to get user location timed out."
                    break;
                case error.UNKNOWN_ERROR:
                    $scope.auto_location_error = "An unknown error occurred."
                    break;
            }
            $scope.$apply();
        }
		
		 $scope.getLocation = function () {
			$scope.locality=="Fetching location...";
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition($scope.showPosition, $scope.showError);
            }
            else {
                $scope.auto_location_error = "Geolocation is not supported by this browser.";
            }
        }
		
		
		
	   $scope.getLocation();
		
	   
}]);