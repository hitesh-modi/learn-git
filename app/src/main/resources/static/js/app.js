angular.module('modiTradersApp',['ngRoute', 'angularUtils.directives.dirPagination'])
	.config(
			['$routeProvider',
			function($routeProvider) {
				$routeProvider.when('/home', {
					templateUrl: 'views/home.html'
				})
				.when('/createProduct', {
					templateUrl: 'views/productForm.html' 
				})
				.when('/createService', {
					templateUrl: 'views/serviceForm.html'
				})
				.when('/serviceRecord', {
					templateUrl: 'views/serviceRecord.html'
				})
				.when('/inventoryRefill', {
					templateUrl: 'views/inventoryRefill.html'
				})
				.when('/contactUs', {
					templateUrl: 'views/contactUs.html'
				})
				.when('/createInvoice', {
					templateUrl: 'views/invoiceForm.html'
				})
				.when('/productReport', {
					templateUrl: 'views/productReport.html'
				})
				.when('/salesReport', {
					templateUrl: 'views/salesReport.html'
				})
				.when('/serviceReport', {
					templateUrl: 'views/serviceReport.html'
				})
				.otherwise({redirectTo: '/home'});
				
			}
		]
	);
	