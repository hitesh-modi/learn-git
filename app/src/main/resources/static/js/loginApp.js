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
	.controller('UserController', function($http, $window, $scope) {
		
		var self = this;
		self.credentials;
		self.stateList;
		self.showRegistrationResponse;
		self.registrationResponse;
		$scope.arePasswordsSame = true;
		
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
		
		$scope.checkPasswords = function(obj1, obj2) {
			
			console.log("Compare",obj1, obj2);
			if(obj1 == obj2)
				$scope.arePasswordsSame = true;
			else 
				$scope.arePasswordsSame = false;
		}
		
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
		
		self.register = function() {
			console.log('Register Called', self.user);
			
			var password = self.user.password;
			var encodedPassword = "Basic "
				+ btoa(self.user.password)
			self.user.password = encodedPassword;
			
			$http.post('/registerUser', self.user).
			then(
					function(successResponse) {
						console.log('Success', successResponse.data);
						if(successResponse.data.status == 'user_email_exists') {
							console.log('User with same email id exists.');
							self.registrationSuccess = false;
							self.showRegistrationResponse = true;
							self.registrationResponse = 'User Already Exists with the Email Id';
							$window.alert(self.registrationResponse);
							
						}
						if(successResponse.data.status == 'user_mobile_exists') {
							console.log('User with same mobile number exists.');
							self.registrationSuccess = false;
							self.showRegistrationResponse = true;
							self.registrationResponse = 'User Already Exists with the Contact Number';
							$window.alert(self.registrationResponse);
						}
						if(successResponse.data.status == 'success') {
							console.log('User registered successfully');
							self.registrationSuccess = true;
							self.showRegistrationResponse = true;
							self.registrationResponse = 'User registered successfully, email notification sent.';
							$window.alert(self.registrationResponse);
						}
					},
					function(failureResponse) {
						console.log('Error', failureResponse)
					}
			);
			
		};
				
	});
