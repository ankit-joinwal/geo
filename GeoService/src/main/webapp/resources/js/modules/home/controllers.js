'use strict';

var app = angular.module('Home');

app.controller('HomeController',
    ['$location','$rootScope','$scope',"$http",'CategoryService','PlacesService',
    function ($location,$rootScope,$scope,$http,CategoryService,PlacesService) {
    	
		
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
	   
	   CategoryService.allSubCategories( function (response) {
			console.log('Inside callback of HomeController. Response status '+response.status);
			if (response.status==200) {
				console.log('Inside HomeController ... Recieved success from Category service');
				
				$scope.allSubCats = response.data;
			} else {
				console.log('Inside HomeController ... Recieved failure from Category service');
				alert("Unable to get all sub Categories");
			}
		});
		
		
		 
		$scope.tsearch = function(){
				var searchbox = $scope.placesearch;
				var query = searchbox.split(' ').join('+');
				$location.path( '/places/'+query );
		};
	   
}]);

 app.filter('capitalize', function() {
    return function(input, all) {
      var reg = (all) ? /([^\W_]+[^\s-]*) */g : /([^\W_]+[^\s-]*)/;
      return (!!input) ? input.replace(reg, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();}) : '';
    }
  });
  
  