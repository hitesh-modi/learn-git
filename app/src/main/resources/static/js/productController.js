angular.module('modiTradersApp')
	.controller('ProductController', ['$http', '$scope','$window',
	                                  function($http, $scope, $window) {
	                                	  var self = this;
	                                	  self.showMessage = false;
	                                	  self.message = "";
	                                	  self.productTypes=[];
	                                	  self.productId="";
	                                	  self.hsnCodes=[];
	                                	  self.sacHeadings=[];
	                                	  self.sacGroups=[];
	                                	  self.sacs=[];
	                                	  self.hsnSections=[];
	                                	  self.hsnChapters=[];
	                                	  self.hsns = [];
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
	                               		  				  //  var $popup = $window.open("views/hsn.html", "popup", "width=500,height=200,left=10,top=150");
	                               		  				   // $popup.hsnCodes = self.hsnCodes;
	                               		  				    console.log('opened new window with hsn list');
	                               		  				    
	                               		  				}, function(errResponse){
	                               		  					console.log('Some error while fetching the list of hsn from server');
	                               		  				}
	                               		  		);
	                                		};
	                                		
	                                		 $scope.getSacHeadings = function(keyword) {
		                                			console.log('Get Sac Headings called');
		                                			self.showHSNSections = false;
		                                			 $http.get('/getSacHeadings')
		                               		  		.then(
		                               		  				function(response){
		                               		  					self.sacHeadings = response.data;
		                               		  					self.showSacHeadings = true;
		                               		  					console.log('Sac Headings Received from server', self.sacHeadings);
		                               		  				    
		                               		  				}, function(errResponse){
		                               		  					console.log('Some error while fetching the list of hsn from server');
		                               		  				}
		                               		  		);
		                                		};
	                                		
		                                		$scope.getGroupsForHeading = function(keyword) {
		                                			console.log('Get Sac Groups called');
		                                			self.showSacGroups = false;
		                                			self.showSACCodes = false;
		                                			 $http.get('/getGroupsForHeading?headingId='+keyword)
		                               		  		.then(
		                               		  				function(response){
		                               		  					self.sacGroups = response.data;
		                               		  					self.showSacGroups = true;
		                               		  					console.log('Sac Groups Received from server', self.sacGroups);
		                               		  				    
		                               		  				}, function(errResponse){
		                               		  					console.log('Some error while fetching the list of hsn from server');
		                               		  				}
		                               		  		);
		                                		};
		                                		
		                                		$scope.getSacCodesForGroup = function(groupId) {
		                                			console.log('Get Sac called');
		                                			 $http.get('/getSacsFromGroupId?groupId='+groupId)
		                               		  		.then(
		                               		  				function(response){
		                               		  					self.sacs = response.data;
		                               		  					self.showSACCodes = true;
		                               		  					console.log('Sac Received from server', self.sacs);
		                               		  				    
		                               		  				}, function(errResponse){
		                               		  					console.log('Some error while fetching the list of hsn from server');
		                               		  				}
		                               		  		);
		                                		};
		                                		
		                                		$scope.getHsnSections = function() {
		                                			console.log('Get all HSN Sections called');
		                                			self.showSacHeadings = false;
		                                			 $http.get('/getHSNSections')
		                               		  		.then(
		                               		  				function(response){
		                               		  					self.hsnSections = response.data;
		                               		  					self.showHSNSections = true;
		                               		  					console.log('HSN Sections received from server', self.sacs);
		                               		  				    
		                               		  				}, function(errResponse){
		                               		  					console.log('Some error while fetching the list of hsn-sections from server');
		                               		  				}
		                               		  		);
		                                		};
		                                		
		                                		$scope.getHSNChapter = function(sectionId) {
		                                			console.log('Get Sac called');
		                                			self.showHSNChapters = false;
		                                			self.showHSN = false;
		                                			 $http.get('/getHsnChapter?sectionId='+sectionId)
		                               		  		.then(
		                               		  				function(response){
		                               		  					self.hsnChapters = response.data;
		                               		  					self.showHSNChapters = true;
		                               		  					console.log('HSN Chapters Received from server', self.sacs);
		                               		  				    
		                               		  				}, function(errResponse){
		                               		  					console.log('Some error while fetching the list of hsn chapters from server');
		                               		  				}
		                               		  		);
		                                		};
		                                		
		                                		$scope.getHSNs = function(chapterId) {
		                                			console.log('Get HSN called');
		                                			 $http.get('/getHsn?chapterId='+chapterId)
		                               		  		.then(
		                               		  				function(response){
		                               		  					self.hsns = response.data;
		                               		  					self.showHSN = true;
		                               		  					console.log('HSN Codes Received from server', self.sacs);
		                               		  				    
		                               		  				}, function(errResponse){
		                               		  					console.log('Some error while fetching the list of hsn chapters from server');
		                               		  				}
		                               		  		);
		                                		};
		                                		
	                                  }
	                                  ]);