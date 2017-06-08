angular.module('modiTradersApp')
	.controller('ProductController', ['$http', '$scope',
	                                  function($http, $scope) {
	                                	  var self = this;
	                                	  self.productTypes=[];
	                                	 
	                                		  console.log('Calling server for fetching priduct types.');
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
	                                		  		.success(
	                                		  				function(data, status, header, config) {
	                                		  					$scope.PostDataResponse = data;
	                                		  				}
	                                		  		)
	                                		  		.error(
	                                		  				function(data, status, header, config) {
	                                		  					$scope.PostDataResponse = "Data: " + data +
	                                		                    "<hr />status: " + status +
	                                		                    "<hr />headers: " + header +
	                                		                    "<hr />config: " + config;
	                                		  				}
	                                		  		);
	                                	  };
	                                  }
	                                  ]);