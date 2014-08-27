'use strict';

// var drawNodes = [];
// var svgWrapper = null;
// var start = null;
// var outline = null;
// var offset = null;
// var selectedLineObject = null;


// /* Remember where we started */
// function startDrag(event) {
//     event = getCoordinates(event);
//     offset = $('#svgArea').offset();
//     start = {X: event.clientX - offset.left, Y: event.clientY - offset.top};
//     event.preventDefault();
// }

// /* Provide feedback as we drag */
// function dragging(event) {
//     event.preventDefault();
//     event = getCoordinates(event);
//     if (!start) {
//         return;
//     }
//     if (!outline) {
//         // jquery.svg.jsバージョン
//         // outline = svgWrapper.rect(0, 0, 0, 0,
//         //     {fill: 'none', stroke: '#c0c0c0', strokeWidth: 1, strokeDashArray: '2,2'});
//         // $(outline).mouseup(endDrag);

//         // svg.jsバージョン
//         // outline = svgWrapper
//         //     .rect(0, 0)
//         //     .fill('none')
//         //     .stroke({color: '#c0c0c0', width: 1, dasharray: '2,2'})
//         //     .move(0, 0)
//         //     .mouseup(endDrag)
//         //     .touchend(endDrag);
//         outline = svgWrapper
//         .line()
//         .fill('none')
//         .stroke({color: '#c0c0c0', width: 2, dasharray: '2,2'})
//         .move(0, 0)
//         .mouseup(endDrag)
//         .touchend(endDrag);
//     }
//     // jquery.svg.jsバージョン
//     // svgWrapper.change(outline, {x: Math.min(event.clientX - offset.left, start.X),
//     //     y: Math.min(event.clientY - offset.top, start.Y),
//     //     width: Math.abs(event.clientX - offset.left - start.X),
//     //     height: Math.abs(event.clientY - offset.top - start.Y)});

//     // svg.jsバージョン
//     // outline.x(Math.min(event.clientX - offset.left, start.X))
//     //     .y(Math.min(event.clientY - offset.top, start.Y))
//     //     .width(Math.abs(event.clientX - offset.left - start.X))
//     //     .height(Math.abs(event.clientY - offset.top - start.Y));

//     outline.plot(start.X, start.Y, event.clientX - offset.left, event.clientY - offset.top);

//     // debug
//     var $scope = angular.element('body').scope();
//     $scope.$apply(function() {
//         $scope.pt = {start: start, nowX: event.clientX - offset.left, nowY: event.clientY - offset.top};
//     });
// }

// /* Draw where we finish */
// function endDrag(event) {

//     event = getCoordinates(event);
//     if (!start) {
//         return;
//     }
//     // jquery.svg.jsバージョン
//     // $(outline).remove();

//     // svg.jsバージョン
//     if (outline) {
//         outline.remove();
//     }

//     outline = null;
//     var end = {X: event.clientX - offset.left, Y:event.clientY - offset.top};
//     if (start.X !== end.X) {
//         // 開始／終了の予定X座標が同じ場合はなにもしない（幅がない）
//         drawLine(start.X, start.Y, end.X, end.Y);

//         // var $scope = angular.element('#content').scope();
//         // if ($scope.conf.autoSave) {
//         //   $scope.savePNG();
//         // }
//     }
//     start = null;
//     event.preventDefault();
// }

// /* Draw the selected element on the canvas */
// function drawLine(x1, y1, x2, y2, lineType) {
//     var left = Math.min(x1, x2);
//     var top = Math.min(y1, y2);
//     var right = Math.max(x1, x2);
//     var bottom = Math.max(y1, y2);
//     var settings = {fill: $('#fill').val(),
//     stroke: $('#stroke').val(),
//     strokeWidth: $('#swidth').val()};
//     // var shape = $('#shape').val();
//     var node = null;


//     // var scale = ((right - left)/95);
//     // var $scope = angular.element('#content').scope();
//     // var mayugeColor = $('select[name="colorpicker4mayuge"]').val();
//     // var rinkakuColor = $('select[name="colorpicker4rinkaku"]').val();
//     // var rinkakuWidth = $('select[name="rinkakuWidth"]').val();
//     // if ($scope.conf.optionsLR == "r") {
//     //     node = svgWrapper.group({class_: "draggable", transform: "translate(" + right + "," + bottom + ")"});
//     //     svgWrapper.use(node, "#path-r-mayuge-" + $scope.conf.mayugeType, {fill: mayugeColor, transform: "scale(" + scale + ")", stroke: rinkakuColor, strokeWidth: rinkakuWidth});
//     // } else {
//     //     node = svgWrapper.group({class_: "draggable", transform: "translate(" + (right - (right - left)) + "," + bottom + ")"});
//     //     svgWrapper.use(node, "#path-r-mayuge-" + $scope.conf.mayugeType, {fill: mayugeColor, transform: "scale(-" + scale + "," + scale + ")", stroke: rinkakuColor, strokeWidth: rinkakuWidth});
//     // }

//     // settingsのデフォルト値（仮）
//     // settings = {fill: null, stroke: 'red', strokeWidth: '4px'};

//     // 直線以外も引けるようにするための分岐
//     lineType = lineType ? lineType : 'line';
//     if (lineType === 'line') {
//         // jquery.svg.jsバージョン 
//         // node = svgWrapper.line(x1, y1, x2, y2, settings);

//         // svg.jsバージョン
//         var lineBorder = svgWrapper.line(x1, y1, x2, y2)
//                     .stroke({color: 'white', width: 4});
//         var line = svgWrapper.line(x1, y1, x2, y2)
//                     .stroke({color: 'red', width: 2});
//         var lineSet = svgWrapper.group();
//         lineSet.add(lineBorder).add(line);
//         lineSet.selectable(x1, y1, x2, y2);

//     } else if (lineType === 'wave') {

//         var waveLength = 10;    // 一周期の長さ
//         var theta = Math.PI * 2 / waveLength;
//         // for (i = left; i < right; i++) {
//         //     yy0=Math.sin(theta*i);
//         //     yy1=Math.sin(theta*(i+1));

//         //     svgWrapper.line(i, yy0, i + 1, yy1)
//         //         .stroke({color: 'red', width: 2});
//         // }

//         // 波線
//         var waveBorder = svgWrapper.group();
//         var wave = svgWrapper.group();
//         var waveSet = svgWrapper.group();
//         waveSet.add(waveBorder).add(wave);
//         // waveSet.draggable();
//         // waveSet.selectable(setMarker);
//         waveSet.selectable(x1, y1, x2, y2);

//         // 二点間の距離
//         var distance = Math.sqrt(Math.pow(right - left, 2) + Math.pow(bottom - top, 2));
//         // 波線の角度
//         var p1 = new SVG.math.Point(x1, y1);
//         var p2 = new SVG.math.Point(x2, y2);
//         var angle = SVG.math.deg(SVG.math.angle(p1, p2));


//         for (var tmpX = 0; tmpX < distance; tmpX++) {
//             var tmpY1 = Math.sin(theta * tmpX);
//             var tmpY2 = Math.sin(theta * (tmpX + 1));

//             // 座標(0, 0)からのsin波になっているので、ドラッグ開始点(x1, y1)を起点にする（加算する）
//             waveBorder.add(svgWrapper.line(tmpX + x1, tmpY1 + y1, tmpX + 1 + x1, tmpY2 + y1)
//                 .stroke({color: 'white', width: 4}));
//             wave.add(svgWrapper.line(tmpX + x1, tmpY1 + y1, tmpX + 1 + x1, tmpY2 + y1)
//                 .stroke({color: 'red', width: 2}));
//         }
//         wave.rotate(angle, x1, y1);
//         waveBorder.rotate(angle, x1, y1);
//     }

//     // drag-and-drop.jsバージョン
//     // var makeSVGElementDraggable = svgDrag.setupCanvasForDragging();
//     // makeSVGElementDraggable(node);
//     // node.addEventListener("mouseup", $scope.export2canvas);

//     // node.addEventListener("click", function() {$scope.selectMayuge($(node))});
//     // node.addEventListener("dblclick", function() {$scope.removeMayuge($(node));});

//     // $scope.export2canvas();
//     // if ($scope.conf.autoSave) {
//     //   $scope.savePNG();
//     // }


//     drawNodes[drawNodes.length] = node;
//     // $(node).mousedown(startDrag).mousemove(dragging).mouseup(endDrag);
//     $('#svgArea').focus();
// };

/* Remove the last drawn element */
$('#undo').click(function() {
    if (!drawNodes.length) {
        return;
    }
    svgWrapper.remove(drawNodes[drawNodes.length - 1]);
    drawNodes.splice(drawNodes.length - 1, 1);
});

/* Clear the canvas */
$('#clear2').click(function() {
    while (drawNodes.length) {
        $('#undo').trigger('click');
    }
});

/* Convert to text */
$('#toSVG').click(function() {
    alert(svgWrapper.toSVG());
});

// /* remove line */
// var removeLine = function() {
//     if (selectedLineObject) {
//         selectedLineObject.remove();
//     }
// }

// function getCoordinates(event) {
//     // event.originalEvent.changedTouches[0]で取得するのはjqueryのeventオブジェクトのとき
//     event.clientX = event.clientX ? event.clientX : event.changedTouches[0].pageX;
//     event.clientY = event.clientY ? event.clientY : event.changedTouches[0].pageY;
//     return event;
// }

// function setSvgImage(base64data) {
//   svgWrapper.image('data:image/jpeg;base64,' + base64data, 200, 200);
// }

// function setMarker(target, x1, y1, x2, y2) {
//     if (selectedLineObject) {
//         // 今クリックされたオブジェクトの前に選択されていたオブジェクト

//         // ドラッグできなくする。
//         selectedLineObject.fixed();
        
//         // markerを削除する。
//         var arrayInLine = selectedLineObject.children();
//         for(var arrayIndex = arrayInLine.length - 1; arrayIndex >= 0; arrayIndex--) {
//             if (arrayInLine[arrayIndex].hasClass('marker')) {
//                 arrayInLine[arrayIndex].remove();
//             }
//         }
//     }

//     // 今クリックされたオブジェクトにmarkerを表示する。
//     selectedLineObject = target;
//     selectedLineObject.draggable();
//     console.dir(selectedLineObject);
//     var marker1 = svgWrapper
//                     .rect(3, 3)
//                     .stroke({ color: 'black', width: 1})
//                     .fill('white')
//                     .move(x1, y1)
//                     .addClass('marker');
//     var marker2 = marker1.clone().move(x2, y2).addClass('marker');
//     selectedLineObject
//         .add(marker1)
//         .add(marker2);
// }

// var selectableFunc = {
//     // selectable: function(actionFn) {
//     selectable: function(x1, y1, x2, y2) {
//         var touchStartedWithoutMove = false;

//         var startEvent, dragEvent, endEvent;
//         var isTouch = 'ontouchstart' in window || (navigator.msMaxTouchPoints && !navigator.msPointerEnabled );
//         if (isTouch) {
//             startEvent = 'touchstart';
//             dragEvent = 'touchmove';
//             endEvent = 'touchend';
//         } else {
//             startEvent = 'mousedown';
//             dragEvent = 'mousemove';
//             endEvent = 'mouseup';
//         }

//         return this.on(startEvent, function() { touchStartedWithoutMove = true; })
//                 .on(dragEvent, function() { touchStartedWithoutMove = false; })
//                 .on(endEvent, function() {
//                     if (touchStartedWithoutMove) {
//                       // 発火させたいイベントを発火させる。
//                       // 引数に対象DOMを指す要素を渡す。
//                       setMarker(this, x1, y1, x2, y2);
//                     }
//                 });
//     }
// }
// SVG.extend(SVG.G, selectableFunc);
// SVG.extend(SVG.Shape, selectableFunc);

function hoge() {
    console.log('hoge');
    var targetScope = angular.element('#content').scope();
    targetScope.$apply(function() {
        targetScope.consoleLog('hoge');
        targetScope.init(100, 100);
        targetScope.setImg(0, '/9j/4AAQSkZJRgABAQEASABIAAD/4ge4SUNDX1BST0ZJTEUAAQEAAAeoYXBwbAIgAABtbnRyUkdCIFhZWiAH2QACABkACwAaAAthY3NwQVBQTAAAAABhcHBsAAAAAAAAAAAAAAAAAAAAAAAA9tYAAQAAAADTLWFwcGwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAtkZXNjAAABCAAAAG9kc2NtAAABeAAABWxjcHJ0AAAG5AAAADh3dHB0AAAHHAAAABRyWFlaAAAHMAAAABRnWFlaAAAHRAAAABRiWFlaAAAHWAAAABRyVFJDAAAHbAAAAA5jaGFkAAAHfAAAACxiVFJDAAAHbAAAAA5nVFJDAAAHbAAAAA5kZXNjAAAAAAAAABRHZW5lcmljIFJHQiBQcm9maWxlAAAAAAAAAAAAAAAUR2VuZXJpYyBSR0IgUHJvZmlsZQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAbWx1YwAAAAAAAAAeAAAADHNrU0sAAAAoAAABeGhySFIAAAAoAAABoGNhRVMAAAAkAAAByHB0QlIAAAAmAAAB7HVrVUEAAAAqAAACEmZyRlUAAAAoAAACPHpoVFcAAAAWAAACZGl0SVQAAAAoAAACem5iTk8AAAAmAAAComtvS1IAAAAWAAACyGNzQ1oAAAAiAAAC3mhlSUwAAAAeAAADAGRlREUAAAAsAAADHmh1SFUAAAAoAAADSnN2U0UAAAAmAAAConpoQ04AAAAWAAADcmphSlAAAAAaAAADiHJvUk8AAAAkAAADomVsR1IAAAAiAAADxnB0UE8AAAAmAAAD6G5sTkwAAAAoAAAEDmVzRVMAAAAmAAAD6HRoVEgAAAAkAAAENnRyVFIAAAAiAAAEWmZpRkkAAAAoAAAEfHBsUEwAAAAsAAAEpHJ1UlUAAAAiAAAE0GFyRUcAAAAmAAAE8mVuVVMAAAAmAAAFGGRhREsAAAAuAAAFPgBWAWEAZQBvAGIAZQBjAG4A/QAgAFIARwBCACAAcAByAG8AZgBpAGwARwBlAG4AZQByAGkBDQBrAGkAIABSAEcAQgAgAHAAcgBvAGYAaQBsAFAAZQByAGYAaQBsACAAUgBHAEIAIABnAGUAbgDoAHIAaQBjAFAAZQByAGYAaQBsACAAUgBHAEIAIABHAGUAbgDpAHIAaQBjAG8EFwQwBDMEMAQ7BEwEPQQ4BDkAIAQ/BEAEPgREBDAEOQQ7ACAAUgBHAEIAUAByAG8AZgBpAGwAIABnAOkAbgDpAHIAaQBxAHUAZQAgAFIAVgBCkBp1KAAgAFIARwBCACCCcl9pY8+P8ABQAHIAbwBmAGkAbABvACAAUgBHAEIAIABnAGUAbgBlAHIAaQBjAG8ARwBlAG4AZQByAGkAcwBrACAAUgBHAEIALQBwAHIAbwBmAGkAbMd8vBgAIABSAEcAQgAg1QS4XNMMx3wATwBiAGUAYwBuAP0AIABSAEcAQgAgAHAAcgBvAGYAaQBsBeQF6AXVBeQF2QXcACAAUgBHAEIAIAXbBdwF3AXZAEEAbABsAGcAZQBtAGUAaQBuAGUAcwAgAFIARwBCAC0AUAByAG8AZgBpAGwAwQBsAHQAYQBsAOEAbgBvAHMAIABSAEcAQgAgAHAAcgBvAGYAaQBsZm6QGgAgAFIARwBCACBjz4/wZYdO9k4AgiwAIABSAEcAQgAgMNcw7TDVMKEwpDDrAFAAcgBvAGYAaQBsACAAUgBHAEIAIABnAGUAbgBlAHIAaQBjA5MDtQO9A7kDugPMACADwAPBA78DxgOvA7sAIABSAEcAQgBQAGUAcgBmAGkAbAAgAFIARwBCACAAZwBlAG4A6QByAGkAYwBvAEEAbABnAGUAbQBlAGUAbgAgAFIARwBCAC0AcAByAG8AZgBpAGUAbA5CDhsOIw5EDh8OJQ5MACAAUgBHAEIAIA4XDjEOSA4nDkQOGwBHAGUAbgBlAGwAIABSAEcAQgAgAFAAcgBvAGYAaQBsAGkAWQBsAGUAaQBuAGUAbgAgAFIARwBCAC0AcAByAG8AZgBpAGkAbABpAFUAbgBpAHcAZQByAHMAYQBsAG4AeQAgAHAAcgBvAGYAaQBsACAAUgBHAEIEHgQxBEkEOAQ5ACAEPwRABD4ERAQ4BDsETAAgAFIARwBCBkUGRAZBACAGKgY5BjEGSgZBACAAUgBHAEIAIAYnBkQGOQYnBkUARwBlAG4AZQByAGkAYwAgAFIARwBCACAAUAByAG8AZgBpAGwAZQBHAGUAbgBlAHIAZQBsACAAUgBHAEIALQBiAGUAcwBrAHIAaQB2AGUAbABz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');
    });
}
