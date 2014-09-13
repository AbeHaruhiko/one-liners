'use strict';

/**
 * @ngdoc function
 * @name tempApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the tempApp
 */
angular.module('one-linsersApp')
  .controller('MainCtrl', function ($scope, Drawingservice) {

    // 画面初期化
    $scope.init = function(width, height) {

      // Drawingservice.init(width, height);
  
      // $scope.lineType = 'line';
      $scope.lineTypeList = ['line', 'wave', 'rect'];
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
    $scope.slideToggleColor = Drawingservice.slideToggleColor;

    // 線リスト
    $scope.slideToggleLineType = Drawingservice.slideToggleLineType;

    // 画像セット
    $scope.setSvgImage = Drawingservice.setSvgImage;
    $scope.setImg = Drawingservice.setImg;
    // $scope.toggleLineType = function(currentLineType) {
    //   if (currentLineType === 'line') {
    //     $scope.lineType = 'wave';
    //   } else if (currentLineType === 'wave'){
    //     $scope.lineType = 'rect';
    //   } else if (currentLineType === 'rect'){
    //     $scope.lineType = 'line';        
    //   }
    //   Drawingservice.setLineType($scope.lineType);
    // };
    $scope.selectColor = function(color) {
      $scope.color = color;
      // Drawingservice.setLineType($scope.lineType);
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

    $scope.init();
  });
