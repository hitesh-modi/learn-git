angular.module('modiTradersApp')
	.controller('InvoiceController', ['$http', '$scope', '$window',
	                                  function($http, $scope, $window) {
	                                	  var self = this;
	                                	  self.invoice = {};
	                                	  self.customers = [];
	                                	  $window.setSelectedCustomer = function(customer) {
	                                		  console.log('Selected Customer', customer);
	                                		  self.invoice.customer = customer;
	                                		  $scope.$digest();
	                                	  };
	                                	  console.log('Invoice controller loaded.');
	                                	  
	                                		  $http.get('/getInvoiceNumber')
                              		  		.then(
                              		  				function(response){
                              		  					self.invoice.invoiceNumber = response.data;
                              		  					console.log('Invoice number received from server', self.invoice);
                              		  				}, function(errResponse){
                              		  					console.log('Some error while fetching the invoice number from server');
                              		  				}
                              		  		);
	                                		  
	                                		self.submit = function() {
	                                			console.log('Invoice Submitted with values', self.invoice);
	                                		};
	                                		
	                                		$scope.getCustomers = function() {
	                                			console.log('Get Customers called.');
	                                			 $http.get('/getCustomers')
	                               		  		.then(
	                               		  				function(response){
	                               		  					self.customers = response.data;
	                               		  					console.log('Customers list received from server', self.customers);
	                               		  				    var $popup = $window.open("views/customers.html", "popup", "width=500,height=200,left=10,top=150");
	                               		  				    $popup.customers = self.customers;
	                               		  				    console.log('opened new window with customer list');
	                               		  				    
	                               		  				}, function(errResponse){
	                               		  					console.log('Some error while fetching the list of cutomers from server');
	                               		  				}
	                               		  		);
	                                		};
	                                		
	                                		$scope.clearCustomer = function() {
	                                			self.invoice.customer={};
	                                		};
	                                		
	                                		$scope.getConsignees = function() {
	                                			console.log('Get Consignees called.');
	                                		};
	                                	  
	                                  }
	                                  ]);