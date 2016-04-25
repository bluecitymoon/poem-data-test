(function() {
    'use strict';
    angular
        .module('poemdataApp')
        .factory('Author', Author);

    Author.$inject = ['$resource'];

    function Author ($resource) {
        var resourceUrl =  'api/authors/:id';

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
