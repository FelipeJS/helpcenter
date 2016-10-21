(function() {
    'use strict';
    angular
        .module('helpcenterApp')
        .factory('Chamado', Chamado);

    Chamado.$inject = ['$resource', 'DateUtils'];

    function Chamado ($resource, DateUtils) {
        var resourceUrl =  'api/chamados/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dataDeAbertura = DateUtils.convertDateTimeFromServer(data.dataDeAbertura);
                        data.dataDeFechamento = DateUtils.convertDateTimeFromServer(data.dataDeFechamento);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
