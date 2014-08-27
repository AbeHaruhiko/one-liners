'use strict';

/*	参考
	http://keith-wood.name/svg.html
	http://www.slideshare.net/tonosamart/e-semi-2720130123
*/

angular.module('one-linsersApp')
  .service('Drawingservice', function Drawingservice() {
    // AngularJS will instantiate a singleton by calling "new" on this function

	var svgWrapper = null;
    var drawNodes = [];
    var startPoint = null;
    var outline = null;
    var offset = null;
	var selectedLineObject = null;
	var lineColor = null;
	var lineType = 'line';

    /* init svg */
    this.init = function(width, height) {
    	angular.element('#svgArea').css("width", width + "px").css("height", height + "px");
		// svg.jsバージョン
		svgWrapper = SVG('svgArea').width(width).height(height);
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
	    event.preventDefault();
	    event = getCoordinates(event);
	    offset = $('#svgArea').offset();
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
	        // jquery.svg.jsバージョン
	        // outline = svgWrapper.rect(0, 0, 0, 0,
	        //     {fill: 'none', stroke: '#c0c0c0', strokeWidth: 1, strokeDashArray: '2,2'});
	        // $(outline).mouseup(endDrag);

	        // svg.jsバージョン
	        // outline = svgWrapper
	        //     .rect(0, 0)
	        //     .fill('none')
	        //     .stroke({color: '#c0c0c0', width: 1, dasharray: '2,2'})
	        //     .move(0, 0)
	        //     .mouseup(endDrag)
	        //     .touchend(endDrag);
	        outline = svgWrapper
	        .line()
	        .attr({stroke: '#c0c0c0', 'stroke-width': 2, 'stroke-dasharray': '2,2'})
	        .move(0, 0)
	        .mouseup(this.endDrag)
	        .touchend(this.endDrag);
	    }
	    // jquery.svg.jsバージョン
	    // svgWrapper.change(outline, {x: Math.min(event.clientX - offset.left, startPoint.X),
	    //     y: Math.min(event.clientY - offset.top, startPoint.Y),
	    //     width: Math.abs(event.clientX - offset.left - startPoint.X),
	    //     height: Math.abs(event.clientY - offset.top - startPoint.Y)});

	    // svg.jsバージョン
	    // outline.x(Math.min(event.clientX - offset.left, startPoint.X))
	    //     .y(Math.min(event.clientY - offset.top, startPoint.Y))
	    //     .width(Math.abs(event.clientX - offset.left - startPoint.X))
	    //     .height(Math.abs(event.clientY - offset.top - startPoint.Y));
	    outline.plot(startPoint.X, startPoint.Y, event.clientX - offset.left, event.clientY - offset.top);
	}

	/* Draw where we finish */
	this.endDrag = function(event) {
	    event.preventDefault();
	    event = getCoordinates(event);
	    if (!startPoint) {
	        return;
	    }
	    // jquery.svg.jsバージョン
	    // $(outline).remove();

	    // svg.jsバージョン
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


	    // var scale = ((right - left)/95);
	    // var $scope = angular.element('#content').scope();
	    // var mayugeColor = $('select[name="colorpicker4mayuge"]').val();
	    // var rinkakuColor = $('select[name="colorpicker4rinkaku"]').val();
	    // var rinkakuWidth = $('select[name="rinkakuWidth"]').val();
	    // if ($scope.conf.optionsLR == "r") {
	    //     node = svgWrapper.group({class_: "draggable", transform: "translate(" + right + "," + bottom + ")"});
	    //     svgWrapper.use(node, "#path-r-mayuge-" + $scope.conf.mayugeType, {fill: mayugeColor, transform: "scale(" + scale + ")", stroke: rinkakuColor, strokeWidth: rinkakuWidth});
	    // } else {
	    //     node = svgWrapper.group({class_: "draggable", transform: "translate(" + (right - (right - left)) + "," + bottom + ")"});
	    //     svgWrapper.use(node, "#path-r-mayuge-" + $scope.conf.mayugeType, {fill: mayugeColor, transform: "scale(-" + scale + "," + scale + ")", stroke: rinkakuColor, strokeWidth: rinkakuWidth});
	    // }

	    // settingsのデフォルト値（仮）
	    // settings = {fill: null, stroke: 'red', strokeWidth: '4px'};

	    // 直線以外も引けるようにするための分岐
	    lineType = lineType ? lineType : 'line';
	    if (lineType === 'line') {
	        // jquery.svg.jsバージョン 
	        // node = svgWrapper.line(x1, y1, x2, y2, settings);

	        // svg.jsバージョン
	        var lineBorder = svgWrapper.line(x1, y1, x2, y2)
	                    .attr({stroke: 'white', 'stroke-width': 0});
	        var line = svgWrapper.line(x1, y1, x2, y2)
	                    .attr({stroke: lineColor, 'stroke-width': 2});
	        var lineSet = svgWrapper.group();
	        lineSet.add(lineBorder).add(line);
	        lineSet.selectable(x1, y1, x2, y2);

	    } else if (lineType === 'wave') {

	        var waveLength = 10;    // 一周期の長さ
	        var theta = Math.PI * 2 / waveLength;
	        // for (i = left; i < right; i++) {
	        //     yy0=Math.sin(theta*i);
	        //     yy1=Math.sin(theta*(i+1));

	        //     svgWrapper.line(i, yy0, i + 1, yy1)
	        //         .stroke({color: 'red', width: 2});
	        // }

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
	            waveBorder.add(svgWrapper.line(tmpX + x1, tmpY1 + y1, tmpX + 1 + x1, tmpY2 + y1)
	                .stroke({color: 'white', width: 0}));
	            wave.add(svgWrapper.line(tmpX + x1, tmpY1 + y1, tmpX + 1 + x1, tmpY2 + y1)
	                .stroke({color: lineColor, width: 2}));
	        }
	        wave.rotate(angle, x1, y1);
	        waveBorder.rotate(angle, x1, y1);
	    }

	    // drag-and-drop.jsバージョン
	    // var makeSVGElementDraggable = svgDrag.setupCanvasForDragging();
	    // makeSVGElementDraggable(node);
	    // node.addEventListener("mouseup", $scope.export2canvas);

	    // node.addEventListener("click", function() {$scope.selectMayuge($(node))});
	    // node.addEventListener("dblclick", function() {$scope.removeMayuge($(node));});

	    // $scope.export2canvas();
	    // if ($scope.conf.autoSave) {
	    //   $scope.savePNG();
	    // }


	    drawNodes[drawNodes.length] = node;
	    
	    // $(node).mousedown(startDrag).mousemove(dragging).mouseup(endDrag);
	    $('#svgArea').focus();
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
	  svgWrapper.image('data:image/jpeg;base64,' + base64data);
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
	                    .stroke({ color: 'black', width: 1})
	                    .fill('white')
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

	this.setLineType = function(val) {
		lineType = val;
	}

	/* 色選択 */
	angular.element('.color').on('click', function() {
	    lineColor = angular.element(this).css('background-color');
	    angular.element('.color').removeClass('select');
	    angular.element(this).addClass('select');
	});

	SVG.extend(SVG.G, selectableFunc);

	this.consoleLog = function(message) {
		console.log(message);
	};


  });
