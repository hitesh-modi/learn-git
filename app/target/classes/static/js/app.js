angular.module('modiTradersApp',[])
	.controller('MainCtrl', [
	                         	function() {
	                         		var self = this;
	                         		self.productForm = "views/productForm.html";
	                         		self.serviceForm = "views/serviceForm.html";
	                         		self.serviceRecord = "views/serviceRecord.html";
	                         		self.inventoryRefill = "views/inventoryRefill.html";
	                         		self.contactUs = "views/contactUs.html";
	                         	}
	                         ]);
	