angular.module('modiTradersApp')
	.controller('ProductController', ['$http', '$scope',
	                                  function($http, $scope) {
	                                	  var self = this;
	                                	  self.showMessage = false;
	                                	  self.message = "";
	                                	  self.productTypes=[];
	                                	  self.productId="";
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
	                                	  
	                                	  
	                                	  self.submit = function() {
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
	                                	  
	                                	  self.clearMessage = function() {
	                                		  console.log('Clearing message');
	                                		  self.showMessage = false;
	                                	  };
	                                  }
	                                  ]);