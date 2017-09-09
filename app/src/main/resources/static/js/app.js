angular.module('modiTradersApp',['ngRoute', 'angularUtils.directives.dirPagination'])
	.config(
			['$routeProvider',
			function($routeProvider) {
				$routeProvider.when('/home', {
					templateUrl: 'home.html'
				})
				.when('/createProduct', {
					templateUrl: 'productForm.html' 
				})
				.when('/createService', {
					templateUrl: 'serviceForm.html'
				})
				.when('/serviceRecord', {
					templateUrl: 'serviceRecord.html'
				})
				.when('/inventoryRefill', {
					templateUrl: 'inventoryRefill.html'
				})
				.when('/contactUs', {
					templateUrl: 'contactUs.html'
				})
				.when('/createInvoice', {
					templateUrl: 'invoiceForm.html'
				})
				.when('/productReport', {
					templateUrl: 'productReport.html'
				})
				.when('/salesReport', {
					templateUrl: 'salesReport.html'
				})
				.when('/serviceReport', {
					templateUrl: 'serviceReport.html'
				})
				.otherwise({redirectTo: '/home'});
				
			}
		]
	)
	.controller(
			'UserController',
			[	'$http',
				function($http) {
					var self = this;
					self.userDetails;
					$http.get('/getUserName').then(
							function(successResponse){
								console.log('Success Response for get user name', successResponse);
								self.userDetails = successResponse.data;
							},
							function(failedResponse){
								console.log('Failure Response for get user name', failedResponse);
							}
					);
				}
			]
	);
	