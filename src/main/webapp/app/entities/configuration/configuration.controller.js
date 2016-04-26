(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('ConfigurationController', ConfigurationController);

    ConfigurationController.$inject = ['$scope', '$state', 'Configuration'];

    function ConfigurationController ($scope, $state, Configuration) {
        var vm = this;
        vm.configurations = [];
        vm.loadAll = function() {
            Configuration.query(function(result) {
                vm.configurations = result;
            });
        };

        vm.loadAll();
        
    }
})();
