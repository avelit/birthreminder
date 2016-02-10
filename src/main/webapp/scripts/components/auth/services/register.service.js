'use strict';

angular.module('birthreminderApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


