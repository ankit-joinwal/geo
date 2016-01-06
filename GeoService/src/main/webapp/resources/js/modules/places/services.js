'use strict';

angular.module('Home')


.factory('PlacesService',
	['$http',function($http){
		var service = {};
		
		service.searchNearby = function(categoryId,callback) {
			var location = '28.5396,77.2477';
			console.log('Inside PlacesService.searchNearby');
			$http({
				method:'GET',
				url: '/GeoService/places/nearby?cid='+categoryId+ '&location='+location,
				headers: {
						"X-Login-Ajax-call": 'true',
						"Accept" : "application/json"
				}
			}).then(function(response) {
			   console.log('Search Data for searchNearby : '+categoryId+' = '+response.data.results);
			  callback(response);
			});
		};
		
		service.textSearch = function(query,callback) {
			
			console.log('Inside PlacesService.textSearch');
			$http({
				method:'GET',
				url: '/GeoService/places/tsearch?query='+query,
				headers: {
						"X-Login-Ajax-call": 'true',
						"Accept" : "application/json"
				}
			}).then(function(response) {
			   console.log('Search Data for text search  : '+query+' = '+response.data.results);
			  callback(response);
			});
		};
		
		service.placeDetail = function(referenceId,callback) {
			
			console.log('Inside PlacesService.placeDetail');
			$http({
				method:'GET',
				url: '/GeoService/places/place/'+referenceId+'/detail',
				headers: {
						"X-Login-Ajax-call": 'true',
						"Accept" : "application/json"
				}
			}).then(function(response) {
			   console.log('Search Data for text search  : '+referenceId+' = '+response.data);
			  callback(response);
			});
		};
		
		
		
		 return service;
	
	}
	]);