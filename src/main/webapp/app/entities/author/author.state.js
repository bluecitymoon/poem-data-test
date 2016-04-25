(function() {
    'use strict';

    angular
        .module('poemdataApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('author', {
            parent: 'entity',
            url: '/author?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Authors'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/author/authors.html',
                    controller: 'AuthorController',
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
        .state('author-detail', {
            parent: 'entity',
            url: '/author/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Author'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/author/author-detail.html',
                    controller: 'AuthorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Author', function($stateParams, Author) {
                    return Author.get({id : $stateParams.id});
                }]
            }
        })
        .state('author.new', {
            parent: 'author',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/author/author-dialog.html',
                    controller: 'AuthorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                birthYear: null,
                                dieYear: null,
                                zi: null,
                                hao: null,
                                avatarFileName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('author', null, { reload: true });
                }, function() {
                    $state.go('author');
                });
            }]
        })
        .state('author.edit', {
            parent: 'author',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/author/author-dialog.html',
                    controller: 'AuthorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Author', function(Author) {
                            return Author.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('author', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('author.delete', {
            parent: 'author',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/author/author-delete-dialog.html',
                    controller: 'AuthorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Author', function(Author) {
                            return Author.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('author', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
