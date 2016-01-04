'use strict';

angular.module('Authentication')

.controller('LoginController',
    ['$scope', '$rootScope', '$location', 'AuthenticationService',
    function ($scope, $rootScope, $location, AuthenticationService) {
        // reset login status
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
                    $location.path('/');
                } else {
                	console.log('Inside Login Controller ... Recieved failure from auth service');
                    $scope.error = response.message;
                    $scope.dataLoading = false;
                }
            });
        };
    }]);