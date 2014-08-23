'use strict';

describe('Service: drawingService', function () {

  // load the service's module
  beforeEach(module('publicApp'));

  // instantiate service
  var drawingService;
  beforeEach(inject(function (_drawingService_) {
    drawingService = _drawingService_;
  }));

  it('should do something', function () {
    expect(!!drawingService).toBe(true);
  });

});
