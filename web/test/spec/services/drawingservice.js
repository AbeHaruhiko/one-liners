'use strict';

describe('Service: Drawingservice', function () {

  // load the service's module
  beforeEach(module('tempApp'));

  // instantiate service
  var Drawingservice;
  beforeEach(inject(function (_Drawingservice_) {
    Drawingservice = _Drawingservice_;
  }));

  it('should do something', function () {
    expect(!!Drawingservice).toBe(true);
  });

});
