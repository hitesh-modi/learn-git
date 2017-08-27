/**
 * 
 */
angular.module('loginApp', [])
	.controller('UserController', function() {
		
		var self = this;
		
		self.credentials = {};
		
		self.login = function() {
			console.log('User Login');
		};
		
	});
