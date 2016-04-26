(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('configuration', {
            parent: 'entity',
            url: '/configuration',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Configurations'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/configuration/configurations.html',
                    controller: 'ConfigurationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('configuration-detail', {
            parent: 'entity',
            url: '/configuration/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Configuration'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/configuration/configuration-detail.html',
                    controller: 'ConfigurationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Configuration', function($stateParams, Configuration) {
                    return Configuration.get({id : $stateParams.id});
                }]
            }
        })
        .state('configuration.new', {
            parent: 'configuration',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/configuration/configuration-dialog.html',
                    controller: 'ConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                identifier: null,
                                content: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('configuration', null, { reload: true });
                }, function() {
                    $state.go('configuration');
                });
            }]
        })
        .state('configuration.edit', {
            parent: 'configuration',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/configuration/configuration-dialog.html',
                    controller: 'ConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Configuration', function(Configuration) {
                            return Configuration.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('configuration', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('configuration.delete', {
            parent: 'configuration',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/configuration/configuration-delete-dialog.html',
                    controller: 'ConfigurationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Configuration', function(Configuration) {
                            return Configuration.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('configuration', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
