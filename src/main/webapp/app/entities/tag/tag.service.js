(function() {
    'use strict';
    angular
        .module('poemdataApp')
        .factory('Tag', Tag);

    Tag.$inject = ['$resource'];

    function Tag ($resource) {
        var resourceUrl =  'api/tags/:id';

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
