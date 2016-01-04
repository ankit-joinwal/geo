'use strict';

angular.module('Home')

.controller('HomeController',
    ['$scope',"$http",'CategoryService',
    function ($scope,$http,CategoryService) {
    	
	   CategoryService.rootCategories( function (response) {
			console.log('Inside callback of HomeController. Response status '+response.status);
			if (response.status==200) {
				console.log('Inside HomeController ... Recieved success from Category service');
				
				$scope.categories = response.data;
			} else {
				console.log('Inside HomeController ... Recieved failure from Category service');
				alert("Unable to get Categories");
			}
		});
		
    	
    }]);