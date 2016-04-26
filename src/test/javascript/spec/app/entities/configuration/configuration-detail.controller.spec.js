'use strict';

describe('Controller Tests', function() {

    describe('Configuration Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockConfiguration;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockConfiguration = jasmine.createSpy('MockConfiguration');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Configuration': MockConfiguration
            };
            createController = function() {
                $injector.get('$controller')("ConfigurationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'poemdataApp:configurationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
