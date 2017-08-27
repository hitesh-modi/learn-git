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
	);
	