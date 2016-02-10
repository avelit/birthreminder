'use strict';

angular.module('birthreminderApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('person', {
                parent: 'entity',
                url: '/persons',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Persons'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/person/persons.html',
                        controller: 'PersonController'
                    }
                },
                resolve: {
                }
            })
            .state('person.detail', {
                parent: 'entity',
                url: '/person/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Person'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/person/person-detail.html',
                        controller: 'PersonDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Person', function($stateParams, Person) {
                        return Person.get({id : $stateParams.id});
                    }]
                }
            })
            .state('person.new', {
                parent: 'person',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/person/person-dialog.html',
                        controller: 'PersonDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    lastName: null,
                                    birthDay: null,
                                    info: null,
                                    remind: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('person', null, { reload: true });
                    }, function() {
                        $state.go('person');
                    })
                }]
            })
            .state('person.edit', {
                parent: 'person',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/person/person-dialog.html',
                        controller: 'PersonDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Person', function(Person) {
                                return Person.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('person', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('person.delete', {
                parent: 'person',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/person/person-delete-dialog.html',
                        controller: 'PersonDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Person', function(Person) {
                                return Person.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('person', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
