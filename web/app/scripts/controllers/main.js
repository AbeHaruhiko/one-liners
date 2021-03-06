'use strict';

/**
 * @ngdoc function
 * @name one-linsersApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the one-linsersApp
 */
angular.module('one-linsersApp')
  .controller('MainCtrl', function ($scope, Drawingservice, Debug) {

    // 画面初期化
    $scope.init = function(width, height) {

      // Drawingservice.init(width, height);
  
      // $scope.lineType = 'line';
      $scope.lineTypeList = ['line', 'wave', 'rect', 'rect-filled'];
      $scope.colorList = ['red', 'blue', 'green'];
    };

    // svgエリア初期化
    $scope.initSVG = function(width, height) {
      Drawingservice.init(width, height);
    };

    // 削除ボタン
    $scope.removeLine = Drawingservice.removeLine;

    // Tools表示・非表示
    $scope.slideToggleTools = Drawingservice.slideToggleTools;

    // 色リスト
    // $scope.slideToggleColor = Drawingservice.slideToggleColor;

    // 線リスト
    // $scope.slideToggleLineType = Drawingservice.slideToggleLineType;

    // 画像セット
    $scope.setSvgImage = Drawingservice.setSvgImage;
    $scope.setImg = Drawingservice.setImg;
    
    $scope.selectColor = function(color, $event) {
      $scope.color = color;
      $scope.colorHEX = angular.element($event.target).css('background-color');
      Drawingservice.setLineColor($scope.colorHEX);
    };

    $scope.selectLineType = function(lineType) {
      $scope.lineType = lineType;
      Drawingservice.setLineType($scope.lineType);
    };

    $scope.isSelectedColor = function(color) {
      return $scope.color === color;
    }

    $scope.isSelectedLineType = function(lineType) {
      return $scope.lineType === lineType;
    }

    $scope.consoleLog = Drawingservice.consoleLog;

    $scope.debug = Debug;
  //   $scope.$watch('debug', function(){
  //   // The model has changed. Do something
  // });
    $scope.$on('debugMessageUpdated', function() {
      $scope.$apply(function() {
        $scope.debug = Debug;
      })
    });

    $scope.init();
  });
