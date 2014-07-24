angular.module('ParseServices', [])
.factory('ParseSDK', function() {

  // pro-tip: swap these keys out for PROD keys automatically on deploy using grunt-replace
  Parse.initialize("JMUkE2OUUNB6qud14tfuDZT7o0rHxlVYCdBmNbtT", "dK1XXtKVjxMNTjuIRev09y8cCyvL1ZE18JiF6EDS");

  // FACEBOOK init
  window.fbPromise.then(function() {

    Parse.FacebookUtils.init({

      // pro-tip: swap App ID out for PROD App ID automatically on deploy using grunt-replace
      appId: 710106092393598, // Facebook App ID
      channelUrl: '', // Channel File
      cookie: true, // enable cookies to allow Parse to access the session
      xfbml: true, // parse XFBML
      frictionlessRequests: true // recommended

    });

  });

});