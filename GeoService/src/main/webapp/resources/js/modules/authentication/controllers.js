'use strict';

var app = angular.module('Authentication');

app.controller('LoginController',
    ['$scope', '$rootScope', '$location', 'AuthenticationService',
    function ($scope, $rootScope, $location, AuthenticationService) {
        // reset login status
    	/*
        AuthenticationService.ClearCredentials();
        
        $scope.login = function () {
        	console.log('Inside on login');
            $scope.dataLoading = true;
            console.log('Inside Login controller ... Caling Auth Service ');
            AuthenticationService.Login($scope.username, $scope.password, function (response) {
				console.log('Inside callback of LoginController. Response status '+response.status);
                if (response.status==200) {
                	console.log('Inside Login Controller ... Recieved success from auth service');
                    AuthenticationService.SetCredentials($scope.username, $scope.password);
					$rootScope.authenticated = true;
                    $location.path('/');
                } else {
                	console.log('Inside Login Controller ... Recieved failure from auth service');
                    $scope.error = response.message;
                    $scope.dataLoading = false;
                }
            });
        };
		*/
		
    }]);
	

app.controller('LogoutController',
    ['$scope', '$rootScope', '$location', 'AuthenticationService',
    function ($scope, $rootScope, $location, AuthenticationService) {
        // reset login status
        AuthenticationService.ClearCredentials();
			console.log('Logout');
			$rootScope.authenticated = false;
			$location.path("/login");
		
    }]);