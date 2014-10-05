'use strict';

/*     参考
       http://keith-wood.name/svg.html
       http://www.slideshare.net/tonosamart/e-semi-2720130123
*/

/**
 * @ngdoc service
 * @name one-linsersApp.Drawingservice
 * @description
 * # Drawingservice
 * Service in the one-linsersApp.
 */
angular.module('one-linsersApp')
  .service('Drawingservice', function Drawingservice($rootScope, Debug) {
    // AngularJS will instantiate a singleton by calling "new" on this function

	var svgWrapper = null;
    var drawNodes = [];
    var startPoint = null;
    var outline = null;
    var offset = null;
	var selectedLineObject = null;
	var lineColor = 'red';
	var lineType = 'line';

    /* init svg */
    this.init = function(width, height) {

    	Debug.setMessage('init.');

    	angular.element('#svg-area').css("width", '100%').css("height", '100%');
		// svg.jsバージョン
		var svgWrapperRoot = SVG('svg-area');

		// zoom-pan, hammer
		svgWrapper = svgWrapperRoot.group().addClass('viewport');
		var panZoom = svgPanZoom('#' + svgWrapperRoot.node.id, {
			panEnabled: false,
			refreshRate: 'auto',
			controlIconsEnabled: true,
			zoomScaleSensitivity: 0.1,
			maxZoom: 2.5
		});
		
		var hammertime = new Hammer(svgWrapperRoot.node);
		hammertime.on('pinchin', function(e) {
			e.preventDefault();
			console.log('pinchin');
			Debug.setMessage('pinchin');
			panZoom.zoomIn();
		});
		hammertime.on('pinchout', function(e) {
			e.preventDefault();
			console.log('pinchout')
			Debug.setMessage('pinchout');
			panZoom.zoomOut();
		});
		hammertime.on('doubletap', function(e) {
			e.preventDefault();
			console.log('doubletap')
			Debug.setMessage('doubletap');
			panZoom.zoomIn();
		});


		// mousedouwn等とtouchstart等は同時にセットするとうまく動かなかった。
		var startEvent, dragEvent, endEvent;
		var isTouch = 'ontouchstart' in window || (navigator.msMaxTouchPoints && !navigator.msPointerEnabled );

		if (isTouch) {
			startEvent = 'touchstart';
			dragEvent = 'touchmove';
			endEvent = 'touchend';
		} else {
			startEvent = 'mousedown';
			dragEvent = 'mousemove';
			endEvent = 'mouseup';
		}

		svgWrapper.on(startEvent, this.startDrag)
			.on(dragEvent, this.dragging)
			.on(endEvent, this.endDrag);
    };

	/* Remember where we started */
	this.startDrag = function(event) {
		if (event.touches.length > 1) console.log("more than 2 touches!");
	    event.preventDefault();
	    event = getCoordinates(event);
	    offset = $('#svg-area').offset();
	    startPoint = {X: event.clientX - offset.left, Y: event.clientY - offset.top};
	};

	/* Provide feedback as we drag */
	this.dragging = function(event) {
	    event.preventDefault();
	    event = getCoordinates(event);
	    if (!startPoint) {
	        return;
	    }
	    if (!outline) {

	    	if (lineType === 'line' || lineType === 'wave') {
		        outline = svgWrapper
			        .line()
			        .attr({stroke: '#c0c0c0', 'stroke-width': 2, 'stroke-dasharray': '2,2'})
			        .move(0, 0)
			        .mouseup(this.endDrag)
			        .touchend(this.endDrag);
		    } else if (lineType === 'rect' || lineType === 'rect-filled') {
		        outline = svgWrapper
		            .rect(0, 0)
		            .attr({stroke: '#c0c0c0', 'stroke-width': 1, 'stroke-dasharray': '2,2', fill: 'none'})
		            .mouseup(this.endDrag)
		            .touchend(this.endDrag);	
		    }
	    }

    	if (lineType === 'line' || lineType === 'wave') {
		    outline.plot(startPoint.X, startPoint.Y, event.clientX - offset.left, event.clientY - offset.top);
	    } else if (lineType === 'rect' || lineType === 'rect-filled') {
	    	outline
	    		.size(Math.abs(startPoint.X - (event.clientX - offset.left)),  Math.abs(startPoint.Y - (event.clientY - offset.top)))
	            .move(Math.min(startPoint.X, event.clientX - offset.left),  Math.min(startPoint.Y, event.clientY - offset.top));
	    }
	}	

	/* Draw where we finish */
	this.endDrag = function(event) {
	    event.preventDefault();
	    event = getCoordinates(event);
	    if (!startPoint) {
	        return;
	    }

	    if (outline) {
	        outline.remove();
	    }

	    outline = null;
	    var end = {X: event.clientX - offset.left, Y:event.clientY - offset.top};
        drawLine(startPoint.X, startPoint.Y, end.X, end.Y);

        // var $scope = angular.element('#content').scope();
        // if ($scope.conf.autoSave) {
        //   $scope.savePNG();
        // }
	    startPoint = null;
	}

	/* Draw the selected element on the canvas */
	var drawLine = function(x1, y1, x2, y2) {
	    var left = Math.min(x1, x2);
	    var top = Math.min(y1, y2);
	    var right = Math.max(x1, x2);
	    var bottom = Math.max(y1, y2);
	    var settings = {fill: $('#fill').val(),
	    stroke: $('#stroke').val(),
	    strokeWidth: $('#swidth').val()};
	    // var shape = $('#shape').val();
	    var node = null;



	    // 直線以外も引けるようにするための分岐
	    lineType = lineType ? lineType : 'rect';
	    if (lineType === 'line') {
	        // jquery.svg.jsバージョン 
	        // node = svgWrapper.line(x1, y1, x2, y2, settings);

	        // svg.jsバージョン
	        // var lineBorder = svgWrapper.line(x1, y1, x2, y2)
	        //             .attr({stroke: 'white', 'stroke-width': 0});
	        var line = svgWrapper.line(x1, y1, x2, y2)
	                    .attr({stroke: lineColor, 'stroke-width': 2, 'stroke-opacity': 0.6});
	        var lineSet = svgWrapper.group();
	        // lineSet.add(lineBorder).add(line);
	        // lineSet.add(lineBorder).add(line);
	        lineSet.selectable(x1, y1, x2, y2);

	    } else if (lineType === 'wave') {

	        var waveLength = 10;    // 一周期の長さ
	        var theta = Math.PI * 2 / waveLength;

	        // 波線
	        var waveBorder = svgWrapper.group();
	        var wave = svgWrapper.group();
	        var waveSet = svgWrapper.group();
	        waveSet.add(waveBorder).add(wave);
	        // waveSet.draggable();
	        // waveSet.selectable(setMarker);
	        waveSet.selectable(x1, y1, x2, y2);

	        // 二点間の距離
	        var distance = Math.sqrt(Math.pow(right - left, 2) + Math.pow(bottom - top, 2));
	        // 波線の角度
	        var p1 = new SVG.math.Point(x1, y1);
	        var p2 = new SVG.math.Point(x2, y2);
	        var angle = SVG.math.deg(SVG.math.angle(p1, p2));

	        for (var tmpX = 0; tmpX < distance; tmpX++) {
	            var tmpY1 = Math.sin(theta * tmpX);
	            var tmpY2 = Math.sin(theta * (tmpX + 1));

	            // 座標(0, 0)からのsin波になっているので、ドラッグ開始点(x1, y1)を起点にする（加算する）
	            // waveBorder.add(svgWrapper.line(tmpX + x1, tmpY1 + y1, tmpX + 1 + x1, tmpY2 + y1)
	            //     .attr({stroke: 'white', 'stroke-width': 0}));
	            wave.add(svgWrapper.line(tmpX + x1, tmpY1 + y1, tmpX + 1 + x1, tmpY2 + y1)
	                .attr({stroke: lineColor, 'stroke-width': 2, 'stroke-opacity': 0.6}));
	        }
            wave.transform({
                rotation: angle
                , cx: x1
                , cy: y1
            });
            // waveBorder.transform({
            //     rotation: angle
            //     , cx: x1
            //     , cy: y1
            // });
	    } else if (lineType === 'rect' || lineType === 'rect-filled') {

	    	if (lineType === 'rect') {
		    	var fillColor = 'none'; 
		    	var lineWidth = 2; 
	    	} else {
		    	var fillColor = lineColor;
		    	var lineWidth = 0; 
	    	}

	        // var rectBorder = svgWrapper.rect(Math.abs(x1 - x2), Math.abs(y1 - y2))
	        //             .attr({stroke: 'white', 'stroke-width': 0, fill: fillColor})
	        //             .move(Math.min(x1, x2), Math.min(y1, y2));
	        var rect = svgWrapper.rect(Math.abs(x1 - x2), Math.abs(y1 - y2))
	                    .attr({stroke: lineColor, 'stroke-width': lineWidth, 'stroke-opacity': 0.6, fill: fillColor, 'fill-opacity': 0.6})
	                    .move(Math.min(x1, x2), Math.min(y1, y2));
	        var lineSet = svgWrapper.group();
	        // lineSet.add(rectBorder).add(rect);
	        lineSet.add(rect);
	        lineSet.selectable(x1, y1, x2, y2);
	    }

	    drawNodes[drawNodes.length] = node;
	    
	    // $(node).mousedown(startDrag).mousemove(dragging).mouseup(endDrag);
	    $('#svg-area').focus();
	};

	/* remove line */
	this.removeLine = function() {
	    if (selectedLineObject) {
	        selectedLineObject.remove();
	    }
	};

	function getCoordinates(event) {
	    // event.originalEvent.changedTouches[0]で取得するのはjqueryのeventオブジェクトのとき
	    event.clientX = event.clientX ? event.clientX : event.changedTouches[0].pageX;
	    event.clientY = event.clientY ? event.clientY : event.changedTouches[0].pageY;
	    return event;
	}

	function setSvgImage(base64data) {
	  svgWrapper.image('data:image/jpeg;base64,' + base64data).width(svgWrapper.parent.node.clientWidth).height(svgWrapper.parent.node.clientHeight);
	};

	var base64data = "";
    this.setImg = function(count,data) {
	    base64data+=data;
	    if(count==0){
	        setSvgImage(base64data);
	     }else{
	        androidApp.getNextImgData(count+1);
	    }
	};

	function setMarker(target, x1, y1, x2, y2) {
	    if (selectedLineObject) {
	        // 今クリックされたオブジェクトの前に選択されていたオブジェクト

	        // ドラッグできなくする。
	        selectedLineObject.fixed();
	        
	        // markerを削除する。
	        var arrayInLine = selectedLineObject.children();
	        for(var arrayIndex = arrayInLine.length - 1; arrayIndex >= 0; arrayIndex--) {
	            if (arrayInLine[arrayIndex].hasClass('marker')) {
	                arrayInLine[arrayIndex].remove();
	            }
	        }
	    }

	    // 今クリックされたオブジェクトにmarkerを表示する。
	    selectedLineObject = target;
	    selectedLineObject.draggable();

	    var marker1 = svgWrapper
	                    .rect(3, 3)
	                    .attr({ stroke: 'black', 'stroke-width': 1 })
	                    .attr({ fill: 'white' })
	                    .move(x1, y1)
	                    .addClass('marker');
	    var marker2 = marker1.clone().move(x2, y2).addClass('marker');
	    selectedLineObject
	        .add(marker1)
	        .add(marker2);
	}

	var selectableFunc = {
	    // selectable: function(actionFn) {
	    selectable: function(x1, y1, x2, y2) {
	        var touchStartedWithoutMove = false;

	        var startEvent, dragEvent, endEvent;
	        var isTouch = 'ontouchstart' in window || (navigator.msMaxTouchPoints && !navigator.msPointerEnabled );
	        if (isTouch) {
	            startEvent = 'touchstart';
	            dragEvent = 'touchmove';
	            endEvent = 'touchend';
	        } else {
	            startEvent = 'mousedown';
	            dragEvent = 'mousemove';
	            endEvent = 'mouseup';
	        }

	        return this.on(startEvent, function() { touchStartedWithoutMove = true; })
	                .on(dragEvent, function() { touchStartedWithoutMove = false; })
	                .on(endEvent, function() {
	                    if (touchStartedWithoutMove) {
	                      // 発火させたいイベントを発火させる。
	                      // 引数に対象DOMを指す要素を渡す。
	                      setMarker(this, x1, y1, x2, y2);
	                    }
	                });
	    }
	}

	this.setLineColor = function(val) {
		lineColor = val;
	}

	this.setLineType = function(val) {
		lineType = val;
	}

	/* Toolsスライドアップ・ダウン　*/
	this.slideToggleTools = function() {
		// 2014/09/14 4.3以前のwebviewで表示がおかしい
		// angular.element('#tool-list').toggle("slide", {direction: 'down', easing: 'swing'}, 200);
		if (angular.element('#tool-list').is(':visible')) {
			slideToggleItem(200);
			angular.element('#tool-list').slideToggle(200, 'swing');
		} else {
			angular.element('#tool-list').slideToggle(50, 'swing', slideToggleItem);
		}
	};

	/* 色選択と線選択のトグル */
	var slideToggleItem = function(duration) {
		// slideToggleColor(duration);
		// slideToggleLineType(duration);
		angular.element('#color-list').toggle("slide", {direction: 'right', easing: 'easeOutElastic'}, duration || 1000);
		angular.element('#line-type-list').toggle("slide", {direction: 'right', easing: 'easeOutElastic'}, duration || 1000);
	}

	/* 色選択スライドアップ・ダウン　*/
	// var slideToggleColor = function(duration) {
	// 	angular.element('#color-list').toggle("slide", {direction: 'right', easing: 'easeOutElastic'}, duration || 1000);
	// };

	/* 線選択スライドアップ・ダウン　*/
	// var slideToggleLineType = function(duration) {
	// 	angular.element('#line-type-list').toggle("slide", {direction: 'right', easing: 'easeOutElastic'}, duration || 1000);
	// };

	SVG.extend(SVG.G, selectableFunc);

	this.consoleLog = function(message) {
		console.log(message);
	};

  });
