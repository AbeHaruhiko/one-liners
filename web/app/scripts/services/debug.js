'use strict';

/**
 * @ngdoc service
 * @name one-linsersApp.Debug
 * @description
 * # Debug
 * Service in the one-linsersApp.
 */
angular.module('one-linsersApp')
  .service('Debug', function Debug($rootScope) {
  	this.message = '';
  
	this.setMessage = function(message) {
	  this.message = message;
	  $rootScope.$broadcast('debugMessageUpdated');
	}

  });
