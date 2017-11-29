angular.module('modiTradersApp')
	.controller('InvoiceController', ['$http', '$scope', '$window',
	                                  function($http, $scope, $window) {
	                                	  var self = this;
	                                	  self.invoice = {};
	                                	  self.customers = [];
	                                	  self.products = [];
	                                	  self.invoice.amountReceived = 0.0;
	                                	  self.invoice.invoiceItemDetails = [];
	                                	  self.invoice.grandTotal = 0.0;
	                                	  self.invoice.totalTax = 0.0;
	                                	  
	                                	  $scope.tempInvoiceDetails = [
	                                		 {serialNumber:1}
	                                	  ];
	                                	  
	                                	  $scope.invoiceItemNumbers = 0;
	                                	  
	                                	  $scope.invoiceItemDetails = [];
	                                	  
	                                	  self.stateList;
	                                	  $scope.addtionalTaxes = [];
	                                	  self.additionalTaxType = [
	                                		  'Service Tax',
	                                		  'VAT',
	                                		  'Excise Duty'
	                                	  ];
	                                	  
	                                	  self.tempAdditionalTax = [
	                                		  {type:{taxType:'Service Tax'}}
	                                	  ];

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
	                                			self.invoice.invoiceItemDetails = $scope.invoiceItemDetails;
                                                        console.log('Invoice Submitted with values', self.invoice);
                                                        $http.post('/services/createInvoice', self.invoice)
                                                        	                                		  .then(
                                                        	                                		  				function(response){
                                                        	                                		  					console.log('Invoice created successfully.');
                                                        	                                		  				}, function(errResponse){
                                                        	                                		  					console.log('Some error while creating invoice');
                                                        	                                		  				}
                                                        	                                		  		);
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
	                                			self.invoice.consignee = {};
	                                			self.invoice.consignee.consigneeId = self.invoice.customer.customerId;
                                                self.invoice.consignee.name = self.invoice.customer.name;
                                                self.invoice.consignee.address = self.invoice.customer.address;
                                                self.invoice.consignee.state = self.invoice.customer.state;
                                                self.invoice.consignee.stateCode = self.invoice.customer.stateCode;
                                                self.invoice.consignee.gstin = self.invoice.customer.gstin;
                                                self.invoice.consignee.email = self.invoice.customer.email;
                                                self.invoice.consignee.mobileNo = self.invoice.customer.mobileNo;
                                                self.invoice.consignee.phoneNo = self.invoice.customer.phoneNo;

	                                		};
	                                		
	                                		self.addInvoiceItem = function(itemToAdd) {
	                                			console.log('Adding invoice item.', itemToAdd);
	                                			itemToAdd.additionalTaxes = $scope.addtionalTaxes;
	                                			$scope.addtionalTaxes = [];
	                                			$scope.invoiceItemDetails.push(itemToAdd);
	                                			$scope.tempInvoiceDetails = [];
	                                			self.tempGrandTotal = parseFloat(self.tempGrandTotal) + parseFloat(itemToAdd.total);
	                                			self.tempTotalTax = parseFloat(self.tempTotalTax) + parseFloat(itemToAdd.cgstAmount) + parseFloat(itemToAdd.sgstAmount);
	                                			console.log('Adding invoiceItemDetail', $scope.invoiceItemDetails.length);
	                                		};
	                                		
	                                		$scope.$watchCollection(
	                                				"invoiceItemDetails",
	                                				function(newValue, oldValue) {
	                                					console.log('New Length',newValue.length);
	                                					$scope.invoiceItemNumbers = newValue.length+1;

	                                					if(newValue.length == 0) {
                                                            self.createInvoiceItem();
	                                					}

	    	                                			self.invoice.grandTotal = 0.0;
	    	                                			self.invoice.totalTax = 0.0;
	    	                                			
	    	                                			self.calculateGrandTotal(newValue);
	    	                                			self.calculateToTalTax(newValue);
	                                				}
	                                		);
	                                		
	                                		self.calculateGrandTotal = function(invoiceItems) {
	                                			var tempInvoiceItem;
	                                			for(var i = 0 ; i < invoiceItems.length ; i++) {
	                                				tempInvoiceItem = invoiceItems[i];
	                                				self.invoice.grandTotal = self.invoice.grandTotal + parseFloat(tempInvoiceItem.taxableValue);
	                                			}
	                                		};
	                                		
	                                		self.calculateToTalTax = function(invoiceItems) {
	                                			var tempInvoiceItem;
	                                			var tempCGSTAmount;
	                                			var tempSGSTAmount;
	                                			var tempIGSTAmount;
	                                			var tempTotalTax;
	                                			
	                                			for(var i = 0 ; i < invoiceItems.length ; i++) {
	                                				
	                                				tempInvoiceItem = invoiceItems[i];
	                                				
	                                				if(isNaN(parseFloat(tempInvoiceItem.cgstAmount))) {
	                                					tempCGSTAmount = 0.0;
	                                				} else {
	                                					tempCGSTAmount = parseFloat(tempInvoiceItem.cgstAmount);
	                                				}
	                                				
	                                				if(isNaN(parseFloat(tempInvoiceItem.sgstAmount))) {
	                                					tempSGSTAmount = 0.0;
	                                				} else {
	                                					tempSGSTAmount = parseFloat(tempInvoiceItem.sgstAmount);
	                                				}
	                                				
	                                				if(isNaN(parseFloat(tempInvoiceItem.igstAmount))) {
	                                					tempIGSTAmount = 0.0;
	                                				} else {
	                                					tempIGSTAmount = parseFloat(tempInvoiceItem.igstAmount);
	                                				}
	                                				
	                                				if(isNaN(parseFloat(self.invoice.totalTax))) {
	                                					tempTotalTax = 0.0;
	                                				} else {
	                                					tempTotalTax = parseFloat(self.invoice.totalTax);
	                                				}
	                                				
	                                				
	                                				self.invoice.totalTax = tempTotalTax + tempCGSTAmount
	                                				+ tempSGSTAmount
	                                				+ tempIGSTAmount;
	                                				
	                                				var tempAdditionalTax;
	                                				for(var j = 0 ; j < tempInvoiceItem.additionalTaxes.length ; j++) {
	                                					tempAdditionalTax = tempInvoiceItem.additionalTaxes[j];
	                                					self.invoice.totalTax = self.invoice.totalTax + parseFloat(tempAdditionalTax.amount);
	                                				}
	                                				
	                                			}
	                                		};
	                                		
	                                		
	                                		self.createInvoiceItem = function() {
	                                			console.log('Creating a temp invoice item.');
	                                			var invoiceItem = {};
                                                invoiceItem.serialNumber = $scope.invoiceItemNumbers;
                                                $scope.tempInvoiceDetails = [];
                                                $scope.tempInvoiceDetails.push(invoiceItem);

	                                		};

	                                		$scope.checkValidityOfCreateInvoiceItem = function() {
	                                		console.log('Checking validity for button Create new item');
	                                		    if(typeof invoiceItems == 'undefined') {
	                                		        return true;
	                                		    }
	                                		    else if(invoiceItems.length > 0) {
	                                		        return false;
	                                		    }
	                                		     else if(invoiceItems.length == 0){
	                                		        return true;
	                                		     }
	                                		};
	                                		
	                                		$scope.calculateTotal = function(invoiceItem) {
	                                			console.log('Rate:'+invoiceItem.rate+', Quantity: ' + invoiceItem.quantity);
	                                			invoiceItem.total = parseFloat(invoiceItem.rate) * parseFloat(invoiceItem.quantity);
	                                		};
	                                		
	                                		$scope.calculateTaxableAmount = function(invoiceItem) {
	                                			if(invoiceItem.discount != "" && typeof invoiceItem.discount != 'undefined') {
	                                				invoiceItem.taxableValue = parseFloat(invoiceItem.total) - parseFloat(invoiceItem.discount);
	                                			}
	                                			else {
	                                				invoiceItem.taxableValue = parseFloat(invoiceItem.total);
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
	                                		
	                                		$scope.calculateExtraTaxAmount = function(totalAmount, tempExtraTax) {
	                                			console.log('Calculating percentage from total amount, ', totalAmount, tempExtraTax);
	                                			if(tempExtraTax.rate != "") {
	                                				tempExtraTax.amount = parseFloat(totalAmount) * parseFloat(tempExtraTax.rate) / 100;
	                                			}
	                                			else {
	                                				tempExtraTax.amount = "";
	                                			}
	                                		};
	                                		
	                                		
	                                		$scope.checkValidityOfInvoiceItem = function() {
	                                		if($scope.tempInvoiceDetails.length == 0) {
	                                		    return true;
	                                		}
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
	                                				return false;
	                                			}
	                                			else {
	                                				return true;
	                                			}
	                                		};
	                                		
	                                		$scope.deleteInvoiceItem = function(invoiceItemToDelete) {
	                                			var tempItem;
	                                			console.log('Deleting item ', invoiceItemToDelete)
	                                			for (var i = 0; i < $scope.invoiceItemDetails.length; i++) {
	                                				tempItem = $scope.invoiceItemDetails[i];
	                                				if(tempItem.serialNumber == invoiceItemToDelete.serialNumber) {
	                                					console.log('Removing item', tempItem);
	                                					$scope.invoiceItemDetails.splice(i,1);
	                                				}
	                                			}
	                                			// Recalculate serial number
	                                			for (var i = 0; i < $scope.invoiceItemDetails.length; i++) {
	                                				$scope.invoiceItemDetails[i].serialNumber = i+1;
	                                			}
	                                		};
	                                		
	                                		$scope.addOtherTax = function(additionalTax) {
	                                			$scope.addtionalTaxes.push(additionalTax);
	                                			console.log('Adding Tax', additionalTax);
	                                			self.tempAdditionalTax = [
	                                				{type:{taxType:'Service Tax'}}
	                                			];
	                                			console.log('Added tax', $scope.addtionalTaxes);
	                                		};
	                                		
	                                		
	                                		$scope.deleteAdditionalTax = function(taxToDelete) {
	                                			var tempItem;
	                                			console.log('Deleting item ', taxToDelete)
	                                			for (var i = 0; i < $scope.addtionalTaxes.length; i++) {
	                                				tempItem = $scope.addtionalTaxes[i];
	                                				if(tempItem.type.taxType == taxToDelete.type.taxType
	                                						&& tempItem.amount == taxToDelete.amount) {
	                                					console.log('Removing item', tempItem);
	                                					$scope.addtionalTaxes.splice(i,1);
	                                				}
	                                			}
	                                		};
	                                		
	                                		$scope.checkTaxableAmount = function(taxableAmount) {
	                                			if(taxableAmount != "" && typeof taxableAmount != 'undefined') {
	                                				return false;
	                                			}
	                                			else {
	                                				return true;
	                                			}
	                                		};
	                                  }
	                                  ]);