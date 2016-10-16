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
                        data.dataAberto = DateUtils.convertLocalDateFromServer(data.dataAberto);
                        data.dataFechado = DateUtils.convertLocalDateFromServer(data.dataFechado);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dataAberto = DateUtils.convertLocalDateToServer(copy.dataAberto);
                    copy.dataFechado = DateUtils.convertLocalDateToServer(copy.dataFechado);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dataAberto = DateUtils.convertLocalDateToServer(copy.dataAberto);
                    copy.dataFechado = DateUtils.convertLocalDateToServer(copy.dataFechado);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
