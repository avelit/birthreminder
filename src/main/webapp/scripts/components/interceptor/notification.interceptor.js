 'use strict';

angular.module('birthreminderApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-birthreminderApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-birthreminderApp-params')});
                }
                return response;
            }
        };
    });
