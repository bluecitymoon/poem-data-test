(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tag', {
            parent: 'entity',
            url: '/tag?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tags'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tag/tags.html',
                    controller: 'TagController',
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
        .state('tag-detail', {
            parent: 'entity',
            url: '/tag/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tag'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tag/tag-detail.html',
                    controller: 'TagDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Tag', function($stateParams, Tag) {
                    return Tag.get({id : $stateParams.id});
                }]
            }
        })
        .state('tag.new', {
            parent: 'tag',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tag/tag-dialog.html',
                    controller: 'TagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                identifier: null,
                                count: null,
                                fontSize: null,
                                link: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tag', null, { reload: true });
                }, function() {
                    $state.go('tag');
                });
            }]
        })
        .state('tag.edit', {
            parent: 'tag',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tag/tag-dialog.html',
                    controller: 'TagDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tag', function(Tag) {
                            return Tag.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('tag', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tag.delete', {
            parent: 'tag',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tag/tag-delete-dialog.html',
                    controller: 'TagDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tag', function(Tag) {
                            return Tag.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('tag', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
