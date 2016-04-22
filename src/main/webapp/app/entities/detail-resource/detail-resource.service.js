(function() {
    'use strict';
    angular
        .module('poemdataApp')
        .factory('DetailResource', DetailResource);

    DetailResource.$inject = ['$resource'];

    function DetailResource ($resource) {
        var resourceUrl =  'api/detail-resources/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
