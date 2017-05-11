var app = angular.module('app', ['ngRoute','ngResource']);
app.config(function($routeProvider){
    $routeProvider

        .when('/dodajSPS',{
            templateUrl:'/views/dodajSPS.html',
            controller: 'dodajSPS'
        })
        .when('/dodajCAS',{
            templateUrl:'/views/dodajCAS.html',
            controller: 'dodajCAS'
        })
        .when('/dodajOS',{
            templateUrl:'/views/dodajOS.html',
            controller: 'dodajOS'
        })


        .when('/nadjiS',{
            templateUrl:'/views/nadjiS.html',
            controller: 'nadjiS'
        })
        .when('/obrisiS',{
            templateUrl:'/views/obrisiS.html',
            controller: 'obrisiS'
        })
        .when('/povuciS',{
            templateUrl:'/views/povuciS.html',
            controller: 'povuciS'
        })
        .otherwise(
            { redirectTo: '/'}
        );
});