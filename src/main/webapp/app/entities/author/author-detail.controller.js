(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('AuthorDetailController', AuthorDetailController);

    AuthorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Author', 'Poem'];

    function AuthorDetailController($scope, $rootScope, $stateParams, entity, Author, Poem) {
        var vm = this;
        vm.author = entity;
        
        var unsubscribe = $rootScope.$on('poemdataApp:authorUpdate', function(event, result) {
            vm.author = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
