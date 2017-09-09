angular.module('modiTradersApp')
	.controller('ProductReportController', ['$http', '$scope',
	                                  function($http, $scope) {
	                                	  var self = this;
	                                	  
	                                	  $scope.sort = function(keyName) {
	                                		  console.log('sorting based on ', keyName);
	                                		  $scope.sortKey = keyName;
	                                		  $scope.reverse = !$scope.reverse;
	                                	  };
	                                	  
	                                	  $http.get('/services/getProducts')
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