'use strict';

/**
 * @ngdoc function
 * @name tempApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the tempApp
 */
angular.module('one-linsersApp')
  .controller('AboutCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
