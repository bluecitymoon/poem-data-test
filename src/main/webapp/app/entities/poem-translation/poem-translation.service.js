(function() {
    'use strict';
    angular
        .module('poemdataApp')
        .factory('PoemTranslation', PoemTranslation);

    PoemTranslation.$inject = ['$resource'];

    function PoemTranslation ($resource) {
        var resourceUrl =  'api/poem-translations/:id';

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
