'use strict';

describe('Controller Tests', function() {

    describe('Poem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPoem, MockAuthor;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPoem = jasmine.createSpy('MockPoem');
            MockAuthor = jasmine.createSpy('MockAuthor');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Poem': MockPoem,
                'Author': MockAuthor
            };
            createController = function() {
                $injector.get('$controller')("PoemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'poemdataApp:poemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
