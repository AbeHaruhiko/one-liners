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

      Drawingservice.init(width, height);
  
      $scope.lineType = 'line';
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
    $scope.toggleLineType = function(currentLineType) {
      if (currentLineType === 'line') {
        $scope.lineType = 'wave';
      } else if (currentLineType === 'wave'){
        $scope.lineType = 'rect';
      } else if (currentLineType === 'rect'){
        $scope.lineType = 'line';        
      }
      Drawingservice.setLineType($scope.lineType);
    };

    $scope.consoleLog = Drawingservice.consoleLog;

  });
