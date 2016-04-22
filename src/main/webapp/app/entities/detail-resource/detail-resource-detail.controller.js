(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('DetailResourceDetailController', DetailResourceDetailController);

    DetailResourceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'DetailResource'];

    function DetailResourceDetailController($scope, $rootScope, $stateParams, entity, DetailResource) {
        var vm = this;
        vm.detailResource = entity;
        
        var unsubscribe = $rootScope.$on('poemdataApp:detailResourceUpdate', function(event, result) {
            vm.detailResource = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
