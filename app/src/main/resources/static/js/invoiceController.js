angular.module('modiTradersApp')
	.controller('InvoiceController', ['$http', '$scope', '$window',
	                                  function($http, $scope, $window) {
	                                	  var self = this;
	                                	  self.invoice = {};
	                                	  self.customers = [];
	                                	  self.products = [];
	                                	  self.invoice.grandTotal = 0.0;
	                                	  self.invoice.totalTax = 0.0;
	                                	  self.invoice.amountReceived = 0.0;
	                                	  self.invoice.invoiceItemDetails = [];
	                                	  self.tempGrandTotal = 0.0;
	                                	  self.tempTotalTax = 0.0;
	                                	  self.tempInvoiceDetails = [
	                                		  {serialNumber:1}
	                                	  ];
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
	                                			
	                                			self.tempGrandTotal = parseFloat(self.tempGrandTotal) + parseFloat(itemToAdd.total);
	                                			self.tempTotalTax = parseFloat(self.tempTotalTax) + parseFloat(itemToAdd.cgstAmount) + parseFloat(itemToAdd.sgstAmount);
	                                			
	                                			console.log('Adding invoiceItemDetail', self.invoice.invoiceItemDetails.length);
	                                		};
	                                		
	                                		$scope.calculateTotal = function(invoiceItem) {
	                                			console.log('Rate:'+invoiceItem.rate+', Quantity: ' + invoiceItem.quantity);
	                                			invoiceItem.total = parseFloat(invoiceItem.rate) * parseFloat(invoiceItem.quantity);
	                                		};
	                                		
	                                		$scope.calculateTaxableAmount = function(invoiceItem) {
	                                			if(invoiceItem.discount != "") {
	                                				invoiceItem.taxableValue = parseFloat(invoiceItem.total) - parseFloat(invoiceItem.discount);
	                                			}
	                                			else {
	                                				invoiceItem.taxableValue = invoiceItem.total;
	                                			}
	                                		};
	                                		
	                                		$scope.calculateCgst = function(invoiceItem) {
	                                			if(invoiceItem.cgstRate != "") {
	                                				invoiceItem.cgstAmount = parseFloat(invoiceItem.taxableValue) * parseFloat(invoiceItem.cgstRate) / 100;
	                                			}
	                                			else {
	                                				invoiceItem.cgstAmount = "";
	                                			}
	                                		};
	                                		
	                                		$scope.calculateSgst = function(invoiceItem) {
	                                			if(invoiceItem.sgstRate != "") {
	                                				invoiceItem.sgstAmount = parseFloat(invoiceItem.taxableValue) * parseFloat(invoiceItem.sgstRate) / 100;
	                                			}
	                                			else {
	                                				invoiceItem.sgstAmount = "";
	                                			}
	                                		};
	                                		
	                                		$scope.calculateIgst = function(invoiceItem) {
	                                			if(invoiceItem.igstRate != "") {
	                                				invoiceItem.igstAmount = parseFloat(invoiceItem.taxableValue) * parseFloat(invoiceItem.igstRate) / 100;
	                                			}
	                                			else {
	                                				invoiceItem.igstAmount = "";
	                                			}
	                                		};
	                                		
	                                		$scope.checkValidityOfInvoiceItem = function() {
	                                			console.log('Checking validity for add item button', $scope.invoiceForm.productCop.$valid);
	                                			if(
	                                					$scope.invoiceForm.productCop.$valid &&
	                                					$scope.invoiceForm.iquantCop.$valid &&
	                                					$scope.invoiceForm.irateCop.$valid &&
	                                					$scope.invoiceForm.itotalCop.$valid &&
	                                					$scope.invoiceForm.idiscCop.$valid &&
	                                					$scope.invoiceForm.itaxableValCop.$valid &&
	                                					$scope.invoiceForm.icgstRateCop.$valid &&
	                                					$scope.invoiceForm.isgstRateCop.$valid &&
	                                					$scope.invoiceForm.iigstRateCop.$valid
	                                			) {
	                                				console.log('Returning true');
	                                				return false;
	                                			}
	                                			else {
	                                				console.log('Returning true');
	                                				return true;
	                                			}
	                                		};
	                                		
	                                		$scope.deleteInvoiceItem = function(invoiceItemToDelete) {
	                                			var tempItem;
	                                			for (var i = 0; i < self.invoice.invoiceItemDetails; i++) {
	                                				tempItem = self.invoice.invoiceItemDetails[i];
	                                				if(tempItem.serialNumber == invoiceItemToDelete.serialNumber) {
	                                					self.invoice.invoiceItemDetails.splice(i,1);
	                                				}
	                                			}
	                                		};
	                                		
	                                  }
	                                  ]);