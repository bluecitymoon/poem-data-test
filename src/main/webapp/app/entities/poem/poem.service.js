(function() {
    'use strict';
    angular
        .module('poemdataApp')
        .factory('Poem', Poem);

    Poem.$inject = ['$resource'];

    function Poem ($resource) {
        var resourceUrl =  'api/poems/:id';

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
