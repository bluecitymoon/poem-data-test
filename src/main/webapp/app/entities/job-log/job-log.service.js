(function() {
    'use strict';
    angular
        .module('poemdataApp')
        .factory('JobLog', JobLog);

    JobLog.$inject = ['$resource', 'DateUtils'];

    function JobLog ($resource, DateUtils) {
        var resourceUrl =  'api/job-logs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.start = DateUtils.convertDateTimeFromServer(data.start);
                    data.end = DateUtils.convertDateTimeFromServer(data.end);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
