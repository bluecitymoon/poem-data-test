(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('poem', {
            parent: 'entity',
            url: '/poem?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Poems'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/poem/poems.html',
                    controller: 'PoemController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('poem-detail', {
            parent: 'entity',
            url: '/poem/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Poem'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/poem/poem-detail.html',
                    controller: 'PoemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Poem', function($stateParams, Poem) {
                    return Poem.get({id : $stateParams.id});
                }]
            }
        })
        .state('poem.new', {
            parent: 'poem',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/poem/poem-dialog.html',
                    controller: 'PoemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                anthorName: null,
                                title: null,
                                content: null,
                                year: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('poem', null, { reload: true });
                }, function() {
                    $state.go('poem');
                });
            }]
        })
        .state('poem.edit', {
            parent: 'poem',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/poem/poem-dialog.html',
                    controller: 'PoemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Poem', function(Poem) {
                            return Poem.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('poem', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('poem.delete', {
            parent: 'poem',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/poem/poem-delete-dialog.html',
                    controller: 'PoemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Poem', function(Poem) {
                            return Poem.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('poem', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
