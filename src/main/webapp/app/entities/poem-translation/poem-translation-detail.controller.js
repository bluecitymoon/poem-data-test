(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('PoemTranslationDetailController', PoemTranslationDetailController);

    PoemTranslationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PoemTranslation'];

    function PoemTranslationDetailController($scope, $rootScope, $stateParams, entity, PoemTranslation) {
        var vm = this;
        vm.poemTranslation = entity;
        
        var unsubscribe = $rootScope.$on('poemdataApp:poemTranslationUpdate', function(event, result) {
            vm.poemTranslation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
