'use strict';

// declare modules
angular.module('Authentication', []);
angular.module('Home', []);

angular.module('BasicHttpAuthExample', [
    'Authentication',
    'Home',
    'ngRoute',
    'ngCookies'
])

.config(['$routeProvider', function ($routeProvider) {

    $routeProvider
        .when('/login', {
            controller: 'LoginController',
            templateUrl: '/GeoService/resources/public/login.html'
        })

        .when('/', {
            controller: 'HomeController',
            templateUrl: '/GeoService/resources/public/home.html'
        })
		.when('/categories/:categoryId/:categoryDesc', {
			templateUrl: '/GeoService/resources/public/partials/catDetails.html',
			controller: 'CatDetailsController'
		})
		.when('/categories/:categoryId/:categoryDesc/places', {
			templateUrl: '/GeoService/resources/public/partials/places.html',
			controller: 'NearbySearchController'
		})
		.when('/places/:query', {
			templateUrl: '/GeoService/resources/public/partials/places.html',
			controller: 'TextSearchController'
		})
		.when('/places/place/:referenceId', {
			templateUrl: '/GeoService/resources/public/partials/place.html',
			controller: 'PlacesController'
		})
		.when('/logout', {
            controller: 'LogoutController',
            templateUrl: '/GeoService/resources/public/login.html'
        })
        .otherwise({ redirectTo: '/home' });
}])

.run(['$rootScope', '$location', '$cookieStore', '$http',
    function ($rootScope, $location, $cookieStore, $http) {
		console.log('Current location : ' +$location.path());
		
        // keep user logged in after page refresh
		/*
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in
            if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
                $location.path('/login');
            }
        });
        */
    }]);
