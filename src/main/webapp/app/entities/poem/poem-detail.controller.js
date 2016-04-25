(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('PoemDetailController', PoemDetailController);

    PoemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Poem', 'Author'];

    function PoemDetailController($scope, $rootScope, $stateParams, entity, Poem, Author) {
        var vm = this;
        vm.poem = entity;
        
        var unsubscribe = $rootScope.$on('poemdataApp:poemUpdate', function(event, result) {
            vm.poem = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
