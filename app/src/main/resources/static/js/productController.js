angular.module('modiTradersApp')
	.controller('ProductController', ['$http', '$scope','$window',
	                                  function($http, $scope, $window) {
	                                	  var self = this;
	                                	  self.showMessage = false;
	                                	  self.message = "";
	                                	  self.productTypes=[];
	                                	  self.productId="";
	                                	  self.hsnCodes=[];
	                                		  console.log('Calling server for fetching product types.');
	                                		  $http.get('/getProductTypes')
	                                		  		.then(
	                                		  				function(response){
	                                		  					self.productTypes = response.data;
	                                		  					console.log('data received from server', response.data);
	                                		  				}, function(errResponse){
	                                		  					console.log('Some error while fetching the data from server');
	                                		  				}
	                                		  		);
	                                	  
	                                	  
	                                	  self.submitProductForm = function() {
	                                		  console.log('User clicked submit with product ', self.product);
	                                		  $http.post('/createProduct', self.product)
	                                		  .then(
	                                		  				function(response){
	                                		  					self.productId = response.data;
	                                		  					self.message = "Product created with Product Id :"+self.productId;
	                                		  					self.showMessage = true;
	                                		  					console.log('data received from server', response.data);
	                                		  				}, function(errResponse){
	                                		  					console.log('Some error while fetching the data from server');
	                                		  					self.message = "Problem creating Product";
	                                		  					self.showMessage = true;
	                                		  				}
	                                		  		);
	                                		  self.product={};
	                                		  		
	                                	  };
	                                	  
	                                	  $scope.clearMessage = function() {
	                                		  console.log('Clearing message');
	                                		  self.showMessage = false;
	                                	  };
	                                	  
	                                	  $window.setSelectedHSN = function(hsnCode) {
	                                		  console.log('Selected HSN Code', hsnCode);
	                                		  self.product.hsnCode = hsnCode.hsnCode;
	                                		  $scope.$digest();
	                                	  };
	                                	  
	                                	  $scope.getHSNForProduct = function(keyword) {
	                                			console.log('Get HSN called for input', keyword);
	                                			 $http.get('/getHSNCodes?keyword='+keyword)
	                               		  		.then(
	                               		  				function(response){
	                               		  					self.hsnCodes = response.data;
	                               		  					console.log('HSN Received from Server', self.hsnCodes);
	                               		  				    var $popup = $window.open("views/hsn.html", "popup", "width=500,height=200,left=10,top=150");
	                               		  				    $popup.hsnCodes = self.hsnCodes;
	                               		  				    console.log('opened new window with hsn list');
	                               		  				    
	                               		  				}, function(errResponse){
	                               		  					console.log('Some error while fetching the list of hsn from server');
	                               		  				}
	                               		  		);
	                                		};
	                                  }
	                                  ]);