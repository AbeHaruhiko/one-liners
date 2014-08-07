var drawNodes = []; 
var svgWrapper = null; 
var start = null; 
var outline = null; 
var offset = null; 
  
/* Remember where we started */ 
function startDrag(event) {
    event.preventDefault(); 
    event = getCoordinates(event);
    offset = $('#svgArea').offset(); 
    start = {X: event.clientX - offset.left, Y: event.clientY - offset.top}; 
} 
 
/* Provide feedback as we drag */ 
function dragging(event) { 
    event.preventDefault(); 
    event = getCoordinates(event);
    if (!start) { 
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
            .fill('none')
            .stroke({color: '#c0c0c0', width: 2, dasharray: '2,2'})
            .move(0, 0)
            .mouseup(endDrag)
            .touchend(endDrag);

    } 
    // jquery.svg.jsバージョン
    // svgWrapper.change(outline, {x: Math.min(event.clientX - offset.left, start.X), 
    //     y: Math.min(event.clientY - offset.top, start.Y), 
    //     width: Math.abs(event.clientX - offset.left - start.X), 
    //     height: Math.abs(event.clientY - offset.top - start.Y)});

    // svg.jsバージョン
    // outline.x(Math.min(event.clientX - offset.left, start.X))
    //     .y(Math.min(event.clientY - offset.top, start.Y))
    //     .width(Math.abs(event.clientX - offset.left - start.X))
    //     .height(Math.abs(event.clientY - offset.top - start.Y));

    outline.plot(start.X, start.Y, event.clientX - offset.left, event.clientY - offset.top);

    // debug
    var $scope = angular.element('body').scope();
    $scope.$apply(function() {
        $scope.pt = {start: start, nowX: event.clientX - offset.left, nowY: event.clientY - offset.top};
    });
} 
 
/* Draw where we finish */ 
function endDrag(event) { 
    event.preventDefault(); 
    event = getCoordinates(event);
    if (!start) { 
        return; 
    } 
    // jquery.svg.jsバージョン
    // $(outline).remove(); 

    // svg.jsバージョン
    outline.remove();

    outline = null; 
    var end = {X: event.clientX - offset.left, Y:event.clientY - offset.top};
    if (start.X != end.X) {
        // 開始／終了の予定X座標が同じ場合はなにもしない（幅がない）
        drawLine(start.X, start.Y, end.X, end.Y); 

        // var $scope = angular.element('#content').scope();
        // if ($scope.conf.autoSave) {
        //   $scope.savePNG();
        // }
    }
    start = null; 
} 
 
/* Draw the selected element on the canvas */ 
function drawLine(x1, y1, x2, y2) { 
    var left = Math.min(x1, x2); 
    var top = Math.min(y1, y2); 
    var right = Math.max(x1, x2); 
    var bottom = Math.max(y1, y2); 
    var settings = {fill: $('#fill').val(),
        stroke: $('#stroke').val(), 
        strokeWidth: $('#swidth').val()}; 
    var shape = $('#shape').val(); 
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
    shape = 'line';
    if (shape == 'line') { 
        // jquery.svg.jsバージョン  
        // node = svgWrapper.line(x1, y1, x2, y2, settings); 

        // svg.jsバージョン
        node = svgWrapper.line(x1, y1, x2, y2)
                .stroke({color: 'red', width: 2})
                .draggable();
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

function getCoordinates(event) {
    event.clientX = event.clientX ? event.clientX : event.originalEvent.changedTouches[0].pageX;
    event.clientY = event.clientY ? event.clientY : event.originalEvent.changedTouches[0].pageY;
    return event;
}
