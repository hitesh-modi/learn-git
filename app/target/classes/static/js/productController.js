angular.module('modiTradersApp')
	.controller('ProductController', [
	                                  function() {
	                                	  var self = this;
	                                	  self.submit = function() {
	                                		  console.log('User clicked submit with product ', self.product);
	                                	  };
	                                  }
	                                  ]);