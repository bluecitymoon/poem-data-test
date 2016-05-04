(function() {
    'use strict';
    angular
        .module('poemdataApp')
        .factory('Story', Story);

    Story.$inject = ['$resource'];

    function Story ($resource) {
        var resourceUrl =  'api/stories/:id';

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
