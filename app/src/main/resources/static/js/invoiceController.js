angular.module('modiTradersApp')
	.controller('InvoiceController', ['$http', '$scope', '$window',
	                                  function($http, $scope, $window) {
	                                	  var self = this;
	                                	  self.invoice = {};
	                                	  self.customers = [];
	                                	  self.products = [];
	                                	  self.invoice.invoiceItemDetails = [];
	                                	  self.tempInvoiceDetails = [
	                                		  {serialNumber:1}
	                                	  ];
	                                	  
	                                	  $window.setSelectedConsignee = function(consignee) {
	                                		  console.log('Selected Consignee', consignee);
	                                		  self.invoice.consignee = consignee;
	                                		  $scope.$digest();
	                                	  };
	                                	  
	                                	  $scope.setSelectedCustomer = function(customer) {
	                                		  console.log('Selected Customer', customer);
	                                		  self.invoice.customer = customer;
	                                	  };
	                                	  console.log('Invoice controller loaded.');
	                                	  
	                                		  $http.get('/services/getInvoiceNumber')
                              		  		.then(
                              		  				function(response){
                              		  					self.invoice.invoiceNumber = response.data;
                              		  					console.log('Invoice number received from server', self.invoice);
                              		  				}, function(errResponse){
                              		  					console.log('Some error while fetching the invoice number from server');
                              		  				}
                              		  		);
	                                		  
	                                		  $http.get('/services/getProducts')
	                              		  		.then(
	                              		  				function(response){
	                              		  					self.products = response.data;
	                              		  					console.log('Products received from server', self.products);
	                              		  				}, function(errResponse){
	                              		  					console.log('Some error while fetching the invoice number from server');
	                              		  				}
	                              		  		);
	                                		  
	                                		self.submit = function() {
	                                			console.log('Invoice Submitted with values', self.invoice);
	                                		};
	                                		
	                                		$scope.getCustomers = function() {
	                                			console.log('Get Customers called.');
	                                			 $http.get('/services/getCustomers')
	                               		  		.then(
	                               		  				function(response){
	                               		  					self.customers = response.data;
	                               		  					console.log('Customers list received from server', self.customers);
	                               		  				  //  var $popup = $window.open("views/customers.html", "popup", "width=500,height=200,left=10,top=150");
	                               		  				  //  $popup.customers = self.customers;
	                               		  				    console.log('opened new window with customer list');
	                               		  				    
	                               		  				}, function(errResponse){
	                               		  					console.log('Some error while fetching the list of cutomers from server');
	                               		  				}
	                               		  		);
	                                		};
	                                		
	                                		
	                                		
	                                		$scope.clearCustomer = function() {
	                                			self.invoice.customer={};
	                                		};
	                                		
	                                		$scope.clearConsignee = function() {
	                                			self.invoice.consignee={};
	                                		};
	                                		
	                                		$scope.getConsignees = function() {
	                                			console.log('Get Consignee called.');
	                                			 $http.get('/services/getConsignees')
	                               		  		.then(
	                               		  				function(response){
	                               		  					self.customers = response.data;
	                               		  					console.log('Consignee list received from server', self.customers);
	                               		  				    var $popup = $window.open("views/consignees.html", "popup", "width=500,height=200,left=10,top=150");
	                               		  				    $popup.customers = self.customers;
	                               		  				    console.log('opened new window with consignee list');
	                               		  				    
	                               		  				}, function(errResponse){
	                               		  					console.log('Some error while fetching the list of consignee from server');
	                               		  				}
	                               		  		);
	                                		};
	                                		
	                                		$scope.copyCustomerToConsignee = function() {
	                                			console.log('Copy the customer to consignee');
	                                			self.invoice.consignee = self.invoice.customer;
	                                		};
	                                		
	                                		self.addInvoiceItem = function(itemToAdd) {
	                                			console.log('Adding invoice item.', itemToAdd);
	                                			 
	                                			var	serialNumber = self.invoice.invoiceItemDetails.length + 2;
	                                			var invoiceItem = {};
	                                			invoiceItem.serialNumber = serialNumber;
	                                			self.invoice.invoiceItemDetails.push(itemToAdd);
	                                			self.tempInvoiceDetails = [];
	                                			self.tempInvoiceDetails.push(invoiceItem);
	                                			console.log('Adding invoiceItemDetail', self.invoice.invoiceItemDetails.length);
	                                		};
	                                		
	                                  }
	                                  ]);