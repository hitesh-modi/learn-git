/**
 * 
 */
angular.module('loginApp', ['ngRoute'])
.config(
			['$routeProvider',
			function($routeProvider) {
				$routeProvider.when('/dashboard', {
					templateUrl: 'home.html'
				})
				.otherwise({redirectTo: '/home'});
				
			}
		]
	)
	.controller('UserController', function($http, $window) {
		
		var self = this;
		self.credentials;
		self.stateList;
		console.log('Getting state list');
		$http.get('/getStates')
			 .then(
					function(successResponse){
						console.log('Got states from server', successResponse);
						self.stateList = successResponse.data;
					},
					function(failureResponse){
						console.log('Request for fetching states failed', successResponse);
					}
			);
		
		
		
		self.login = function() {
			console.log('User Login');
			var headers = self.credentials ? {
				authorization : "Basic "
						+ btoa(self.credentials.username + ":"
								+ self.credentials.password)
			} : {};
			console.log('Header', headers);
			$http.post('/login', headers).
			then(
					function(successResponse) {
						console.log('Login Success Response', successResponse);
						if(successResponse.data.status == 'success') {
							$window.location.href = '/greeting';
						}
					},
					function(failedResponse) {
						console.log('Login Failure Response', failedResponse);
					}
			);
		};
		
		getStates = function() {
			console.log('Getting state list');
			$http.get(
					'/getStates'
			)
			.then(
					function(successResponse){
						console.log('Got states from server', successResponse);
					},
					function(failureResponse){
						console.log('Request for fetching states failed', successResponse);
					}
			);
		};
		
	});
