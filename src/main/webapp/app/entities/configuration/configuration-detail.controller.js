(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('ConfigurationDetailController', ConfigurationDetailController);

    ConfigurationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Configuration'];

    function ConfigurationDetailController($scope, $rootScope, $stateParams, entity, Configuration) {
        var vm = this;
        vm.configuration = entity;
        
        var unsubscribe = $rootScope.$on('poemdataApp:configurationUpdate', function(event, result) {
            vm.configuration = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
