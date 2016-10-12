(function() {
    'use strict';
    angular
        .module('helpcenterApp')
        .factory('Chamado', Chamado);

    Chamado.$inject = ['$resource'];

    function Chamado ($resource) {
        var resourceUrl =  'api/chamados/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
