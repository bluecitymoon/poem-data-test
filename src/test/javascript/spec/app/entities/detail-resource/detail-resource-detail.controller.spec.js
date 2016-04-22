'use strict';

describe('Controller Tests', function() {

    describe('DetailResource Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDetailResource;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDetailResource = jasmine.createSpy('MockDetailResource');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'DetailResource': MockDetailResource
            };
            createController = function() {
                $injector.get('$controller')("DetailResourceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'poemdataApp:detailResourceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
