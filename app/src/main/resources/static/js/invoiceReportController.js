angular.module('modiTradersApp')
	.controller('InvoiceReportController', ['$http', '$scope',
	function($http, $scope) {
		var self = this;
		
		var fromDate = new Date();
		var dateOneMonthBack = fromDate.getMonth()-1;
		var toDate = new Date();
		toDate.setMonth(dateOneMonthBack);
		
		console.log('From Date: ', fromDate);
		console.log('To Date: ', toDate);
		
		$http.post('/services/getInvoieReport', {fromDate: fromDate, toDate: toDate})
   	  	 .then(
	  				function(response){
 					self.products = response.data;
 					console.log('data received from server', response.data);
 				}, function(errResponse){
 					console.log('Some error while fetching the data from server');
 				}
 		);
		
	}
]);