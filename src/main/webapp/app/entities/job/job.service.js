(function() {
    'use strict';
    angular
        .module('poemdataApp')
        .factory('Job', Job);

    Job.$inject = ['$resource', 'DateUtils'];

    function Job ($resource, DateUtils) {
        var resourceUrl =  'api/jobs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.lastStart = DateUtils.convertDateTimeFromServer(data.lastStart);
                    data.lastStop = DateUtils.convertDateTimeFromServer(data.lastStop);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'start' : { method:'GET', url: 'api/jobs/start/:id'}
        });
    }
})();
