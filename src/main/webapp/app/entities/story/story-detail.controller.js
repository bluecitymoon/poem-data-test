(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .controller('StoryDetailController', StoryDetailController);

    StoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Story'];

    function StoryDetailController($scope, $rootScope, $stateParams, entity, Story) {
        var vm = this;
        vm.story = entity;
        
        var unsubscribe = $rootScope.$on('poemdataApp:storyUpdate', function(event, result) {
            vm.story = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
