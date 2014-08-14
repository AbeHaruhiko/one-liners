"use strict";function startDrag(a){a=getCoordinates(a),offset=$("#svgArea").offset(),start={X:a.clientX-offset.left,Y:a.clientY-offset.top},a.preventDefault()}function dragging(a){if(a.preventDefault(),a=getCoordinates(a),start){outline||(outline=svgWrapper.line().fill("none").stroke({color:"#c0c0c0",width:2,dasharray:"2,2"}).move(0,0).mouseup(endDrag).touchend(endDrag)),outline.plot(start.X,start.Y,a.clientX-offset.left,a.clientY-offset.top);var b=angular.element("body").scope();b.$apply(function(){b.pt={start:start,nowX:a.clientX-offset.left,nowY:a.clientY-offset.top}})}}function endDrag(a){if(a=getCoordinates(a),start){outline&&outline.remove(),outline=null;var b={X:a.clientX-offset.left,Y:a.clientY-offset.top};start.X!=b.X&&drawLine(start.X,start.Y,b.X,b.Y),start=null,a.preventDefault()}}function drawLine(a,b,c,d,e){var f=Math.min(a,c),g=Math.min(b,d),h=Math.max(a,c),i=Math.max(b,d),j=({fill:$("#fill").val(),stroke:$("#stroke").val(),strokeWidth:$("#swidth").val()},null);if(e=e?e:"wave","line"==e)j=svgWrapper.line(a,b,c,d).stroke({color:"red",width:2}).draggable();else if("wave"==e){var k=10,l=2*Math.PI/k;wave=svgWrapper.group(),wave.draggable();for(var m=Math.sqrt(Math.pow(h-f,2)+Math.pow(i-g,2)),n=new SVG.math.Point(a,b),o=new SVG.math.Point(c,d),p=SVG.math.deg(SVG.math.angle(n,o)),q=0;m>q;q++)tmpY1=Math.sin(l*q),tmpY2=Math.sin(l*(q+1)),wave.add(svgWrapper.line(q+a,tmpY1+b,q+1+a,tmpY2+b).stroke({color:"red",width:2}));wave.rotate(p,a,b)}drawNodes[drawNodes.length]=j,$("#svgArea").focus()}function getCoordinates(a){return a.clientX=a.clientX?a.clientX:a.changedTouches[0].pageX,a.clientY=a.clientY?a.clientY:a.changedTouches[0].pageY,a}function setSvgImage(a){svgWrapper.image("data:image/jpeg;base64,"+a,200,200)}angular.module("one-linsersApp",["ngCookies","ngResource","ngSanitize","ngRoute"]).config(["$routeProvider",function(a){a.when("/",{templateUrl:"views/main.html",controller:"MainCtrl"}).otherwise({redirectTo:"/"})}]),angular.module("one-linsersApp").controller("MainCtrl",["$scope",function(a){a.init=function(){svgWrapper=SVG("svgArea").width(200).height(200);var a,b,c,d="ontouchstart"in window||navigator.msMaxTouchPoints&&!navigator.msPointerEnabled;d?(a="touchstart",b="touchmove",c="touchend"):(a="mousedown",b="mousemove",c="mouseup"),svgWrapper.on(a,startDrag).on(b,dragging).on(c,endDrag)},a.init()}]),function(a){function b(){this._settings=[],this._extensions=[],this.regional=[],this.regional[""]={errorLoadingText:"Error loading",notSupportedText:"This browser does not support SVG"},this.local=this.regional[""],this._uuid=(new Date).getTime(),this._renesis=c("RenesisX.RenesisCtrl")}function c(a){try{return!(!window.ActiveXObject||!new ActiveXObject(a))}catch(b){return!1}}function d(b,c){this._svg=b,this._container=c;for(var d=0;d<a.svg._extensions.length;d++){var e=a.svg._extensions[d];this[e[0]]=new e[1](this)}}function e(){this._path=""}function f(){this._parts=[]}function g(a){return a&&a.constructor==Array}var h="svgwrapper";a.extend(b.prototype,{markerClassName:"hasSVG",svgNS:"http://www.w3.org/2000/svg",xlinkNS:"http://www.w3.org/1999/xlink",_wrapperClass:d,_attrNames:{class_:"class",in_:"in",alignmentBaseline:"alignment-baseline",baselineShift:"baseline-shift",clipPath:"clip-path",clipRule:"clip-rule",colorInterpolation:"color-interpolation",colorInterpolationFilters:"color-interpolation-filters",colorRendering:"color-rendering",dominantBaseline:"dominant-baseline",enableBackground:"enable-background",fillOpacity:"fill-opacity",fillRule:"fill-rule",floodColor:"flood-color",floodOpacity:"flood-opacity",fontFamily:"font-family",fontSize:"font-size",fontSizeAdjust:"font-size-adjust",fontStretch:"font-stretch",fontStyle:"font-style",fontVariant:"font-variant",fontWeight:"font-weight",glyphOrientationHorizontal:"glyph-orientation-horizontal",glyphOrientationVertical:"glyph-orientation-vertical",horizAdvX:"horiz-adv-x",horizOriginX:"horiz-origin-x",imageRendering:"image-rendering",letterSpacing:"letter-spacing",lightingColor:"lighting-color",markerEnd:"marker-end",markerMid:"marker-mid",markerStart:"marker-start",stopColor:"stop-color",stopOpacity:"stop-opacity",strikethroughPosition:"strikethrough-position",strikethroughThickness:"strikethrough-thickness",strokeDashArray:"stroke-dasharray",strokeDashOffset:"stroke-dashoffset",strokeLineCap:"stroke-linecap",strokeLineJoin:"stroke-linejoin",strokeMiterLimit:"stroke-miterlimit",strokeOpacity:"stroke-opacity",strokeWidth:"stroke-width",textAnchor:"text-anchor",textDecoration:"text-decoration",textRendering:"text-rendering",underlinePosition:"underline-position",underlineThickness:"underline-thickness",vertAdvY:"vert-adv-y",vertOriginY:"vert-origin-y",wordSpacing:"word-spacing",writingMode:"writing-mode"},_attachSVG:function(b,c){var d=b.namespaceURI==this.svgNS?b:null,b=d?null:b;if(!a(b||d).hasClass(this.markerClassName)){"string"==typeof c?c={loadURL:c}:"function"==typeof c&&(c={onLoad:c}),a(b||d).addClass(this.markerClassName);try{d||(d=document.createElementNS(this.svgNS,"svg"),d.setAttribute("version","1.1"),b.clientWidth>0&&d.setAttribute("width",b.clientWidth),b.clientHeight>0&&d.setAttribute("height",b.clientHeight),b.appendChild(d)),this._afterLoad(b,d,c||{})}catch(e){a.support.opacity?b.innerHTML='<p class="svg_error">'+this.local.notSupportedText+"</p>":(b.id||(b.id="svg"+this._uuid++),this._settings[b.id]=c,b.innerHTML='<embed type="image/svg+xml" width="100%" height="100%" src="'+(c.initPath||"")+'blank.svg" pluginspage="http://www.adobe.com/svg/viewer/install/main.html"/>')}}},_registerSVG:function(){for(var b=0;b<document.embeds.length;b++){var c=document.embeds[b].parentNode;if(a(c).hasClass(a.svg.markerClassName)&&!a.data(c,h)){var d=null;try{d=document.embeds[b].getSVGDocument()}catch(e){return void setTimeout(a.svg._registerSVG,250)}d=d?d.documentElement:null,d&&a.svg._afterLoad(c,d)}}},_afterLoad:function(b,c,d){var d=d||this._settings[b.id];this._settings[b?b.id:""]=null;var e=new this._wrapperClass(c,b);a.data(b||c,h,e);try{d.loadURL&&e.load(d.loadURL,d),d.settings&&e.configure(d.settings),d.onLoad&&!d.loadURL&&d.onLoad.apply(b||c,[e])}catch(f){alert(f)}},_getSVG:function(b){return b="string"==typeof b?a(b)[0]:b.jquery?b[0]:b,a.data(b,h)},_destroySVG:function(b){var c=a(b);c.hasClass(this.markerClassName)&&(c.removeClass(this.markerClassName),b.namespaceURI!=this.svgNS&&c.empty(),a.removeData(b,h))},addExtension:function(a,b){this._extensions.push([a,b])},isSVGElem:function(b){return 1==b.nodeType&&b.namespaceURI==a.svg.svgNS}}),a.extend(d.prototype,{_width:function(){return this._container?this._container.clientWidth:this._svg.width},_height:function(){return this._container?this._container.clientHeight:this._svg.height},root:function(){return this._svg},configure:function(b,c,d){if(b.nodeName||(d=c,c=b,b=this._svg),d)for(var e=b.attributes.length-1;e>=0;e--){var f=b.attributes.item(e);"onload"!=f.nodeName&&"version"!=f.nodeName&&"xmlns"!=f.nodeName.substring(0,5)&&b.attributes.removeNamedItem(f.nodeName)}for(var g in c)b.setAttribute(a.svg._attrNames[g]||g,c[g]);return this},getElementById:function(a){return this._svg.ownerDocument.getElementById(a)},change:function(b,c){if(b)for(var d in c)null==c[d]?b.removeAttribute(a.svg._attrNames[d]||d):b.setAttribute(a.svg._attrNames[d]||d,c[d]);return this},_args:function(b,c,d){c.splice(0,0,"parent"),c.splice(c.length,0,"settings");var e={},f=0;null!=b[0]&&b[0].jquery&&(b[0]=b[0][0]),null==b[0]||"object"==typeof b[0]&&b[0].nodeName||(e.parent=null,f=1);for(var g=0;g<b.length;g++)e[c[g+f]]=b[g];return d&&a.each(d,function(a,b){"object"==typeof e[b]&&(e.settings=e[b],e[b]=null)}),e},title:function(){var a=this._args(arguments,["text"]),b=this._makeNode(a.parent,"title",a.settings||{});return b.appendChild(this._svg.ownerDocument.createTextNode(a.text)),b},describe:function(){var a=this._args(arguments,["text"]),b=this._makeNode(a.parent,"desc",a.settings||{});return b.appendChild(this._svg.ownerDocument.createTextNode(a.text)),b},defs:function(){var b=this._args(arguments,["id"],["id"]);return this._makeNode(b.parent,"defs",a.extend(b.id?{id:b.id}:{},b.settings||{}))},symbol:function(){var b=this._args(arguments,["id","x1","y1","width","height"]);return this._makeNode(b.parent,"symbol",a.extend({id:b.id,viewBox:b.x1+" "+b.y1+" "+b.width+" "+b.height},b.settings||{}))},marker:function(){var b=this._args(arguments,["id","refX","refY","mWidth","mHeight","orient"],["orient"]);return this._makeNode(b.parent,"marker",a.extend({id:b.id,refX:b.refX,refY:b.refY,markerWidth:b.mWidth,markerHeight:b.mHeight,orient:b.orient||"auto"},b.settings||{}))},style:function(){var b=this._args(arguments,["styles"]),c=this._makeNode(b.parent,"style",a.extend({type:"text/css"},b.settings||{}));return c.appendChild(this._svg.ownerDocument.createTextNode(b.styles)),a.browser.opera&&a("head").append('<style type="text/css">'+b.styles+"</style>"),c},script:function(){var b=this._args(arguments,["script","type"],["type"]),c=this._makeNode(b.parent,"script",a.extend({type:b.type||"text/javascript"},b.settings||{}));return c.appendChild(this._svg.ownerDocument.createTextNode(b.script)),a.browser.mozilla||a.globalEval(b.script),c},linearGradient:function(){var b=this._args(arguments,["id","stops","x1","y1","x2","y2"],["x1"]),c=a.extend({id:b.id},null!=b.x1?{x1:b.x1,y1:b.y1,x2:b.x2,y2:b.y2}:{});return this._gradient(b.parent,"linearGradient",a.extend(c,b.settings||{}),b.stops)},radialGradient:function(){var b=this._args(arguments,["id","stops","cx","cy","r","fx","fy"],["cx"]),c=a.extend({id:b.id},null!=b.cx?{cx:b.cx,cy:b.cy,r:b.r,fx:b.fx,fy:b.fy}:{});return this._gradient(b.parent,"radialGradient",a.extend(c,b.settings||{}),b.stops)},_gradient:function(b,c,d,e){for(var f=this._makeNode(b,c,d),g=0;g<e.length;g++){var h=e[g];this._makeNode(f,"stop",a.extend({offset:h[0],stopColor:h[1]},null!=h[2]?{stopOpacity:h[2]}:{}))}return f},pattern:function(){var b=this._args(arguments,["id","x","y","width","height","vx","vy","vwidth","vheight"],["vx"]),c=a.extend({id:b.id,x:b.x,y:b.y,width:b.width,height:b.height},null!=b.vx?{viewBox:b.vx+" "+b.vy+" "+b.vwidth+" "+b.vheight}:{});return this._makeNode(b.parent,"pattern",a.extend(c,b.settings||{}))},clipPath:function(){var b=this._args(arguments,["id","units"]);return b.units=b.units||"userSpaceOnUse",this._makeNode(b.parent,"clipPath",a.extend({id:b.id,clipPathUnits:b.units},b.settings||{}))},mask:function(){var b=this._args(arguments,["id","x","y","width","height"]);return this._makeNode(b.parent,"mask",a.extend({id:b.id,x:b.x,y:b.y,width:b.width,height:b.height},b.settings||{}))},createPath:function(){return new e},createText:function(){return new f},svg:function(){var b=this._args(arguments,["x","y","width","height","vx","vy","vwidth","vheight"],["vx"]),c=a.extend({x:b.x,y:b.y,width:b.width,height:b.height},null!=b.vx?{viewBox:b.vx+" "+b.vy+" "+b.vwidth+" "+b.vheight}:{});return this._makeNode(b.parent,"svg",a.extend(c,b.settings||{}))},group:function(){var b=this._args(arguments,["id"],["id"]);return this._makeNode(b.parent,"g",a.extend({id:b.id},b.settings||{}))},use:function(){var b=this._args(arguments,["x","y","width","height","ref"]);"string"==typeof b.x&&(b.ref=b.x,b.settings=b.y,b.x=b.y=b.width=b.height=null);var c=this._makeNode(b.parent,"use",a.extend({x:b.x,y:b.y,width:b.width,height:b.height},b.settings||{}));return c.setAttributeNS(a.svg.xlinkNS,"href",b.ref),c},link:function(){var b=this._args(arguments,["ref"]),c=this._makeNode(b.parent,"a",b.settings);return c.setAttributeNS(a.svg.xlinkNS,"href",b.ref),c},image:function(){var b=this._args(arguments,["x","y","width","height","ref"]),c=this._makeNode(b.parent,"image",a.extend({x:b.x,y:b.y,width:b.width,height:b.height},b.settings||{}));return c.setAttributeNS(a.svg.xlinkNS,"href",b.ref),c},path:function(){var b=this._args(arguments,["path"]);return this._makeNode(b.parent,"path",a.extend({d:b.path.path?b.path.path():b.path},b.settings||{}))},rect:function(){var b=this._args(arguments,["x","y","width","height","rx","ry"],["rx"]);return this._makeNode(b.parent,"rect",a.extend({x:b.x,y:b.y,width:b.width,height:b.height},b.rx?{rx:b.rx,ry:b.ry}:{},b.settings||{}))},circle:function(){var b=this._args(arguments,["cx","cy","r"]);return this._makeNode(b.parent,"circle",a.extend({cx:b.cx,cy:b.cy,r:b.r},b.settings||{}))},ellipse:function(){var b=this._args(arguments,["cx","cy","rx","ry"]);return this._makeNode(b.parent,"ellipse",a.extend({cx:b.cx,cy:b.cy,rx:b.rx,ry:b.ry},b.settings||{}))},line:function(){var b=this._args(arguments,["x1","y1","x2","y2"]);return this._makeNode(b.parent,"line",a.extend({x1:b.x1,y1:b.y1,x2:b.x2,y2:b.y2},b.settings||{}))},polyline:function(){var a=this._args(arguments,["points"]);return this._poly(a.parent,"polyline",a.points,a.settings)},polygon:function(){var a=this._args(arguments,["points"]);return this._poly(a.parent,"polygon",a.points,a.settings)},_poly:function(b,c,d,e){for(var f="",g=0;g<d.length;g++)f+=d[g].join()+" ";return this._makeNode(b,c,a.extend({points:a.trim(f)},e||{}))},text:function(){var b=this._args(arguments,["x","y","value"]);return"string"==typeof b.x&&arguments.length<4&&(b.value=b.x,b.settings=b.y,b.x=b.y=null),this._text(b.parent,"text",b.value,a.extend({x:b.x&&g(b.x)?b.x.join(" "):b.x,y:b.y&&g(b.y)?b.y.join(" "):b.y},b.settings||{}))},textpath:function(){var b=this._args(arguments,["path","value"]),c=this._text(b.parent,"textPath",b.value,b.settings||{});return c.setAttributeNS(a.svg.xlinkNS,"href",b.path),c},_text:function(b,c,d,e){var f=this._makeNode(b,c,e);if("string"==typeof d)f.appendChild(f.ownerDocument.createTextNode(d));else for(var g=0;g<d._parts.length;g++){var h=d._parts[g];if("tspan"==h[0]){var i=this._makeNode(f,h[0],h[2]);i.appendChild(f.ownerDocument.createTextNode(h[1])),f.appendChild(i)}else if("tref"==h[0]){var i=this._makeNode(f,h[0],h[2]);i.setAttributeNS(a.svg.xlinkNS,"href",h[1]),f.appendChild(i)}else if("textpath"==h[0]){var j=a.extend({},h[2]);j.href=null;var i=this._makeNode(f,h[0],j);i.setAttributeNS(a.svg.xlinkNS,"href",h[2].href),i.appendChild(f.ownerDocument.createTextNode(h[1])),f.appendChild(i)}else f.appendChild(f.ownerDocument.createTextNode(h[1]))}return f},other:function(){var a=this._args(arguments,["name"]);return this._makeNode(a.parent,a.name,a.settings||{})},_makeNode:function(b,c,d){b=b||this._svg;var e=this._svg.ownerDocument.createElementNS(a.svg.svgNS,c);for(var c in d){var f=d[c];null==f||null==f||"string"==typeof f&&""==f||e.setAttribute(a.svg._attrNames[c]||c,f)}return b.appendChild(e),e},add:function(b){var c=this._args(1==arguments.length?[null,b]:arguments,["node"]),d=this;c.parent=c.parent||this._svg,c.node=c.node.jquery?c.node:a(c.node);try{if(a.svg._renesis)throw"Force traversal";c.parent.appendChild(c.node.cloneNode(!0))}catch(e){c.node.each(function(){var a=d._cloneAsSVG(this);a&&c.parent.appendChild(a)})}return this},clone:function(b){var c=this,d=this._args(1==arguments.length?[null,b]:arguments,["node"]);d.parent=d.parent||this._svg,d.node=d.node.jquery?d.node:a(d.node);var e=[];return d.node.each(function(){var a=c._cloneAsSVG(this);a&&(a.id="",d.parent.appendChild(a),e.push(a))}),e},_cloneAsSVG:function(b){var c=null;if(1==b.nodeType){c=this._svg.ownerDocument.createElementNS(a.svg.svgNS,this._checkName(b.nodeName));for(var d=0;d<b.attributes.length;d++){var e=b.attributes.item(d);"xmlns"!=e.nodeName&&e.nodeValue&&("xlink"==e.prefix?c.setAttributeNS(a.svg.xlinkNS,e.localName||e.baseName,e.nodeValue):c.setAttribute(this._checkName(e.nodeName),e.nodeValue))}for(var d=0;d<b.childNodes.length;d++){var f=this._cloneAsSVG(b.childNodes[d]);f&&c.appendChild(f)}}else if(3==b.nodeType)a.trim(b.nodeValue)&&(c=this._svg.ownerDocument.createTextNode(b.nodeValue));else if(4==b.nodeType&&a.trim(b.nodeValue))try{c=this._svg.ownerDocument.createCDATASection(b.nodeValue)}catch(g){c=this._svg.ownerDocument.createTextNode(b.nodeValue.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;"))}return c},_checkName:function(a){return a=a.substring(0,1)>="A"&&a.substring(0,1)<="Z"?a.toLowerCase():a,"svg:"==a.substring(0,4)?a.substring(4):a},load:function(b,c){c="boolean"==typeof c?{addTo:c}:"function"==typeof c?{onLoad:c}:"string"==typeof c?{parent:c}:"object"==typeof c&&c.nodeName?{parent:c}:"object"==typeof c&&c.jquery?{parent:c}:c||{},c.parent||c.addTo||this.clear(!1);var d=[this._svg.getAttribute("width"),this._svg.getAttribute("height")],e=this,f=function(b){b=a.svg.local.errorLoadingText+": "+b,c.onLoad?c.onLoad.apply(e._container||e._svg,[e,b]):e.text(null,10,20,b)},g=function(a){var b=new ActiveXObject("Microsoft.XMLDOM");return b.validateOnParse=!1,b.resolveExternals=!1,b.async=!1,b.loadXML(a),0!=b.parseError.errorCode?(f(b.parseError.reason),null):b},h=function(b){if(b){if("svg"!=b.documentElement.nodeName){var g=b.getElementsByTagName("parsererror"),h=g.length?g[0].getElementsByTagName("div"):[];return void f(g.length?(h.length?h[0]:g[0]).firstChild.nodeValue:"???")}for(var i=c.parent?a(c.parent)[0]:e._svg,j={},k=0;k<b.documentElement.attributes.length;k++){var l=b.documentElement.attributes.item(k);"version"!=l.nodeName&&"xmlns"!=l.nodeName.substring(0,5)&&(j[l.nodeName]=l.nodeValue)}e.configure(i,j,!c.parent);for(var m=b.documentElement.childNodes,k=0;k<m.length;k++)try{if(a.svg._renesis)throw"Force traversal";i.appendChild(e._svg.ownerDocument.importNode(m[k],!0)),"script"==m[k].nodeName&&a.globalEval(m[k].textContent)}catch(n){e.add(i,m[k])}c.changeSize||e.configure(i,{width:d[0],height:d[1]}),c.onLoad&&c.onLoad.apply(e._container||e._svg,[e])}};return b.match("<svg")?h(a.support.opacity?(new DOMParser).parseFromString(b,"text/xml"):g(b)):a.ajax({url:b,dataType:a.support.opacity?"xml":"text",success:function(b){h(a.support.opacity?b:g(b))},error:function(a,b,c){f(b+(c?" "+c.message:""))},cache:!1}),this},remove:function(a){return a=a.jquery?a[0]:a,a.parentNode.removeChild(a),this},clear:function(a){for(a&&this.configure({},!0);this._svg.firstChild;)this._svg.removeChild(this._svg.firstChild);return this},toSVG:function(a){return a=a||this._svg,"undefined"==typeof XMLSerializer?this._toSVG(a):(new XMLSerializer).serializeToString(a)},_toSVG:function(b){var c="";if(!b)return c;if(3==b.nodeType)c=b.nodeValue;else if(4==b.nodeType)c="<![CDATA["+b.nodeValue+"]]>";else{if(c="<"+b.nodeName,b.attributes)for(var d=0;d<b.attributes.length;d++){var e=b.attributes.item(d);""==a.trim(e.nodeValue)||e.nodeValue.match(/^\[object/)||e.nodeValue.match(/^function/)||(c+=" "+(e.namespaceURI==a.svg.xlinkNS?"xlink:":"")+e.nodeName+'="'+e.nodeValue+'"')}if(b.firstChild){c+=">";for(var f=b.firstChild;f;)c+=this._toSVG(f),f=f.nextSibling;c+="</"+b.nodeName+">"}else c+="/>"}return c}}),a.extend(e.prototype,{reset:function(){return this._path="",this},move:function(a,b,c){return c=g(a)?b:c,this._coords(c?"m":"M",a,b)},line:function(a,b,c){return c=g(a)?b:c,this._coords(c?"l":"L",a,b)},horiz:function(a,b){return this._path+=(b?"h":"H")+(g(a)?a.join(" "):a),this},vert:function(a,b){return this._path+=(b?"v":"V")+(g(a)?a.join(" "):a),this},curveC:function(a,b,c,d,e,f,h){return h=g(a)?b:h,this._coords(h?"c":"C",a,b,c,d,e,f)},smoothC:function(a,b,c,d,e){return e=g(a)?b:e,this._coords(e?"s":"S",a,b,c,d)},curveQ:function(a,b,c,d,e){return e=g(a)?b:e,this._coords(e?"q":"Q",a,b,c,d)},smoothQ:function(a,b,c){return c=g(a)?b:c,this._coords(c?"t":"T",a,b)},_coords:function(a,b,c,d,e,f,h){if(g(b))for(var i=0;i<b.length;i++){var j=b[i];this._path+=(0==i?a:" ")+j[0]+","+j[1]+(j.length<4?"":" "+j[2]+","+j[3]+(j.length<6?"":" "+j[4]+","+j[5]))}else this._path+=a+b+","+c+(null==d?"":" "+d+","+e+(null==f?"":" "+f+","+h));return this},arc:function(a,b,c,d,e,f,h,i){if(i=g(a)?b:i,this._path+=i?"a":"A",g(a))for(var j=0;j<a.length;j++){var k=a[j];this._path+=(0==j?"":" ")+k[0]+","+k[1]+" "+k[2]+" "+(k[3]?"1":"0")+","+(k[4]?"1":"0")+" "+k[5]+","+k[6]}else this._path+=a+","+b+" "+c+" "+(d?"1":"0")+","+(e?"1":"0")+" "+f+","+h;return this},close:function(){return this._path+="z",this},path:function(){return this._path}}),e.prototype.moveTo=e.prototype.move,e.prototype.lineTo=e.prototype.line,e.prototype.horizTo=e.prototype.horiz,e.prototype.vertTo=e.prototype.vert,e.prototype.curveCTo=e.prototype.curveC,e.prototype.smoothCTo=e.prototype.smoothC,e.prototype.curveQTo=e.prototype.curveQ,e.prototype.smoothQTo=e.prototype.smoothQ,e.prototype.arcTo=e.prototype.arc,a.extend(f.prototype,{reset:function(){return this._parts=[],this},string:function(a){return this._parts[this._parts.length]=["text",a],this},span:function(a,b){return this._parts[this._parts.length]=["tspan",a,b],this},ref:function(a,b){return this._parts[this._parts.length]=["tref",a,b],this},path:function(b,c,d){return this._parts[this._parts.length]=["textpath",c,a.extend({href:b},d||{})],this}}),a.fn.svg=function(b){var c=Array.prototype.slice.call(arguments,1);return"string"==typeof b&&"get"==b?a.svg["_"+b+"SVG"].apply(a.svg,[this[0]].concat(c)):this.each(function(){"string"==typeof b?a.svg["_"+b+"SVG"].apply(a.svg,[this].concat(c)):a.svg._attachSVG(this,b||{})})},a.svg=new b}(jQuery),function(a){var b=/[\t\r\n]/g,c=/\s+/,d="[\\x20\\t\\r\\n\\f]";a.fn.addClass=function(){return function(b){var d,e,f,g,h,i,j;if(jQuery.isFunction(b))return this.each(function(a){jQuery(this).addClass(b.call(this,a,this.className))});if(b&&"string"==typeof b)for(d=b.split(c),e=0,f=this.length;f>e;e++)if(g=this[e],1===g.nodeType)if(g.className&&g.getAttribute("class")||1!==d.length){for(h=a.svg.isSVGElem(g)?g.className?g.className.baseVal:g.getAttribute("class"):g.className,h=" "+h+" ",i=0,j=d.length;j>i;i++)h.indexOf(" "+d[i]+" ")<0&&(h+=d[i]+" ");h=jQuery.trim(h),a.svg.isSVGElem(g)?g.className?g.className.baseVal=h:g.setAttribute("class",h):g.className=h}else a.svg.isSVGElem(g)?g.className?g.className.baseVal=b:g.setAttribute("class",b):g.className=b;return this}}(a.fn.addClass),a.fn.removeClass=function(){return function(d){var e,f,g,h,i,j,k;if(jQuery.isFunction(d))return this.each(function(a){jQuery(this).removeClass(d.call(this,a,this.className))});if(d&&"string"==typeof d||void 0===d)for(e=(d||"").split(c),f=0,g=this.length;g>f;f++)if(h=this[f],1===h.nodeType&&(h.className||h.getAttribute("class"))){if(d){for(i=a.svg.isSVGElem(h)?h.className?h.className.baseVal:h.getAttribute("class"):h.className,i=(" "+i+" ").replace(b," "),j=0,k=e.length;k>j;j++)for(;i.indexOf(" "+e[j]+" ")>=0;)i=i.replace(" "+e[j]+" "," ");i=jQuery.trim(i)}else i="";a.svg.isSVGElem(h)?h.className?h.className.baseVal=i:h.setAttribute("class",i):h.className=i}return this}}(a.fn.removeClass),a.fn.toggleClass=function(b){return function(c,d){return this.each(function(){a.svg.isSVGElem(this)?("boolean"!=typeof d&&(d=!a(this).hasClass(c)),a(this)[(d?"add":"remove")+"Class"](c)):b.apply(a(this),[c,d])})}}(a.fn.toggleClass),a.fn.hasClass=function(){return function(c){for(var d,e,f=" "+c+" ",g=0,h=this.length;h>g;g++)if(d=this[g],1===d.nodeType&&(e=a.svg.isSVGElem(d)?d.className?d.className.baseVal:d.getAttribute("class"):d.className,(" "+e+" ").replace(b," ").indexOf(f)>-1))return!0;return!1}}(a.fn.hasClass),a.fn.attr=function(b){return function(c,d,e){var f=arguments;if("string"==typeof c&&void 0===d){var g=b.apply(this,f);if(g&&g.baseVal&&null!=g.baseVal.numberOfItems)if(d="",g=g.baseVal,"transform"==c){for(var h=0;h<g.numberOfItems;h++){var i=g.getItem(h);switch(i.type){case 1:d+=" matrix("+i.matrix.a+","+i.matrix.b+","+i.matrix.c+","+i.matrix.d+","+i.matrix.e+","+i.matrix.f+")";break;case 2:d+=" translate("+i.matrix.e+","+i.matrix.f+")";break;case 3:d+=" scale("+i.matrix.a+","+i.matrix.d+")";break;case 4:d+=" rotate("+i.angle+")";break;case 5:d+=" skewX("+i.angle+")";break;case 6:d+=" skewY("+i.angle+")"}}g=d.substring(1)}else g=g.getItem(0).valueAsString;return g&&g.baseVal?g.baseVal.valueAsString:g}var j=c;return"string"==typeof c&&(j={},j[c]=d),a(this).each(function(){if(a.svg.isSVGElem(this))for(var c in j){var d=a.isFunction(j[c])?j[c]():j[c];e?this.style[c]=d:this.setAttribute(c,d)}else b.apply(a(this),f)})}}(a.fn.attr),a.fn.removeAttr=function(b){return function(c){return this.each(function(){a.svg.isSVGElem(this)?this[c]&&this[c].baseVal?this[c].baseVal.value="":this.setAttribute(c,""):b.apply(a(this),[c])})}}(a.fn.removeAttr),a.extend(a.cssNumber,{stopOpacity:!0,strokeMitrelimit:!0,strokeOpacity:!0}),a.cssProps&&(a.css=function(b){return function(c,d,e,f){var g=d.match(/^svg.*/)?a(c).attr(a.cssProps[d]||d):"";return g||b(c,d,e,f)}}(a.css)),a.find.isXML=function(b){return function(c){return a.svg.isSVGElem(c)||b(c)}}(a.find.isXML);var e=document.createElement("div");e.appendChild(document.createComment("")),e.getElementsByTagName("*").length>0&&(a.expr.find.TAG=function(a,b){var c=b.getElementsByTagName(a[1]);if("*"===a[1]){for(var d=[],e=0;c[e]||c.item(e);e++)1===(c[e]||c.item(e)).nodeType&&d.push(c[e]||c.item(e));c=d}return c}),a.expr.filter.CLASS=function(b){var c=new RegExp("(^|"+d+")"+b+"("+d+"|$)");return function(b){var d=a.svg.isSVGElem(b)?b.className?b.className.baseVal:b.getAttribute("class"):b.className||"undefined"!=typeof b.getAttribute&&b.getAttribute("class")||"";return c.test(d)}}}(jQuery);var drawNodes=[],svgWrapper=null,start=null,outline=null,offset=null;$("#undo").click(function(){drawNodes.length&&(svgWrapper.remove(drawNodes[drawNodes.length-1]),drawNodes.splice(drawNodes.length-1,1))}),$("#clear2").click(function(){for(;drawNodes.length;)$("#undo").trigger("click")}),$("#toSVG").click(function(){alert(svgWrapper.toSVG())});var svgDrag;svgDrag=function(){var a,b,c,d;return c="http://www.w3.org/2000/svg",d=function(a,b,c){var d,e,f,g;return g=a.transform.baseVal,f=g.numberOfItems?g.getItem(0):a.ownerSVGElement.createSVGTransform(),d=f.matrix,e=a.ownerSVGElement.createSVGMatrix().translate(b,c).multiply(d),f.setMatrix(e),g.initialize(f)},b=function(a,b){var c,d,e,f,g,h,i,j,k,l;return j=a.ownerSVGElement,c=a.getBBox(),k=[],l=[],e=function(){var a;return a=d.matrixTransform(b),k.push(a.x),l.push(a.y)},d=j.createSVGPoint(),d.x=c.x,d.y=c.y,e(),d.x+=c.width,e(),d.y+=c.height,e(),d.x-=c.width,e(),h=Math.min.apply(this,k),i=Math.min.apply(this,l),f=Math.max.apply(this,k),g=Math.max.apply(this,l),{x:h,y:i,width:f-h,height:g-i}},a=function(a){return b(a,a.getTransformToElement(a.ownerSVGElement))},{setupCanvasForDragging:function(b,e){var f,g,h,i,j,k,l;return null==b&&(b=document.documentElement),j=!1,h=l=f=null,k=function(a){return a&&(g.setAttributeNS(null,"x",a.x),g.setAttributeNS(null,"y",a.y),g.setAttributeNS(null,"width",a.width),g.setAttributeNS(null,"height",a.height)),g.removeAttributeNS(null,"display")},i=function(){return g.setAttributeNS(null,"display","none")},e&&(g=document.createElementNS(c,"rect"),g.setAttributeNS(null,"class","simple-dragging-rect"),i(),b.appendChild(g)),b.addEventListener("mousemove",function(a){var b,c,i;return a.preventDefault(),j?(b=a.clientX-h.clientX,c=a.clientY-h.clientY,i=e?g:f,d(i,b,c),h=a):void 0},!1),b.addEventListener("mouseup",function(a){var b,c;if(a.preventDefault(),j){if(j=!1,b=a.clientX-l.clientX,c=a.clientY-l.clientY,e)return i(),d(f,b,c),g.removeAttributeNS(null,"transform");if(0!=b||0!=c){var h=angular.element("#content").scope();h.export2canvas()}}},!1),function(b){return b.addEventListener("mousedown",function(c){var d;return c.preventDefault(),c.stopPropagation(),j=!0,f=b,e&&(d=a(f),k(d)),l=h=c},!1)}}}}(),function(){SVG.extend(SVG.Element,{draggable:function(a){var b,c,d,e,f,g,h=this,i=this.parent._parent(SVG.Nested)||this._parent(SVG.Doc),j="ontouchstart"in window||navigator.msMaxTouchPoints&&!navigator.msPointerEnabled,k=navigator.userAgent.match(/(ip(hone|od|ad))/i)?!0:!1;return"function"==typeof this.fixed&&this.fixed(),a=a||{},j?(e="touchstart",f="touchmove",g="touchend"):(e="mousedown",f="mousemove",g="mouseup"),b=function(a){a=a||window.event,h.beforedrag&&h.beforedrag(a);var b=h.bbox();h instanceof SVG.G?(b.x=h.x(),b.y=h.y()):h instanceof SVG.Nested&&(b={x:h.x(),y:h.y(),width:h.width(),height:h.height()}),h.startEvent=a,h.startPosition={x:b.x,y:b.y,width:b.width,height:b.height,zoom:i.viewbox().zoom,rotation:h.transform("rotation")*Math.PI/180},SVG.on(window,f,c),SVG.on(window,g,d),h.dragstart&&h.dragstart({x:0,y:0,zoom:h.startPosition.zoom},a),a.preventDefault?a.preventDefault():a.returnValue=!1},c=function(b){if(b=b||window.event,h.startEvent){var c,d,e,f=h.startPosition.rotation,g=h.startPosition.width,i=h.startPosition.height;if(e=j?k?{x:b.pageX-h.startEvent.pageX,y:b.pageY-h.startEvent.pageY,zoom:h.startPosition.zoom}:{x:b.changedTouches[0].pageX-h.startEvent.changedTouches[0].pageX,y:b.changedTouches[0].pageY-h.startEvent.changedTouches[0].pageY,zoom:h.startPosition.zoom}:{x:b.pageX-h.startEvent.pageX,y:b.pageY-h.startEvent.pageY,zoom:h.startPosition.zoom},c=h.startPosition.x+(e.x*Math.cos(f)+e.y*Math.sin(f))/h.startPosition.zoom,d=h.startPosition.y+(e.y*Math.cos(f)+e.x*Math.sin(-f))/h.startPosition.zoom,"function"==typeof a){var l=a(c,d);"object"==typeof l?(("boolean"!=typeof l.x||l.x)&&h.x("number"==typeof l.x?l.x:c),("boolean"!=typeof l.y||l.y)&&h.y("number"==typeof l.y?l.y:d)):"boolean"==typeof l&&l&&h.move(c,d)}else"object"==typeof a&&(null!=a.minX&&c<a.minX?c=a.minX:null!=a.maxX&&c>a.maxX-g&&(c=a.maxX-g),null!=a.minY&&d<a.minY?d=a.minY:null!=a.maxY&&d>a.maxY-i&&(d=a.maxY-i),h.move(c,d));h.dragmove&&h.dragmove(e,b)}},d=function(a){a=a||window.event;var b;b=j?k?{x:a.pageX-h.startEvent.pageX,y:a.pageY-h.startEvent.pageY,zoom:h.startPosition.zoom}:{x:a.changedTouches[0].pageX-h.startEvent.changedTouches[0].pageX,y:a.changedTouches[0].pageY-h.startEvent.changedTouches[0].pageY,zoom:h.startPosition.zoom}:{x:a.pageX-h.startEvent.pageX,y:a.pageY-h.startEvent.pageY,zoom:h.startPosition.zoom},h.startEvent=null,h.startPosition=null,SVG.off(window,f,c),SVG.off(window,g,d),h.dragend&&h.dragend(b,a)},h.on(e,b),h.fixed=function(){return h.off(e,b),SVG.off(window,f,c),SVG.off(window,g,d),b=c=d=null,h},this}})}.call(this);