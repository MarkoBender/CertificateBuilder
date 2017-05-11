app.controller("dodajSPS", function($scope,$http){

    $scope.message="NAPRAVI NOVI SAMOPOTPISAN SERTIFIKAT";

    $scope.add = function(certificate) {
        var myObj = { "commonName":certificate.commonName, "surename":certificate.surename, "givenname":certificate.givenname, "organisation":certificate.organisation, "organisationUnit":certificate.organisationUnit, "country":certificate.country,  "email":certificate.email, "uid":certificate.uid  };

        var myJSON = JSON.stringify(myObj);
        alert(myJSON);
        /*$http.post("http://localhost:8080/sps/create",
                    data=myJSON
        ).success(function(data){
            console.log("Uspesno dodat novi samopotpisani sertifikat!");
        });*/
    }
});

app.controller("dodajCAS", function($scope,$http){

    $scope.message="NAPRAVI NOVI CA SERTIFIKAT";


    var konjoslav=[{ "givenname"  : "Scala for the Impatient", "price" : 1000 },
                   { "givenname"  : "Scala in Depth", "price" : 1300 }];
    $scope.cas=konjoslav;

    /*$http.get('/ss/all')
                .success(function(response){
                    $scope.cas = response;
                });*/

    $scope.add = function(certificate) {
        var myObj = { "commonName":certificate.commonName, "surename":certificate.surename, "givenname":certificate.givenname, "organisation":certificate.organisation, "organisationUnit":certificate.organisationUnit, "country":certificate.country,  "email":certificate.email, "uid":certificate.uid, "signedBy":certificate.signedBy  };

        var myJSON = JSON.stringify(myObj);
        alert(myJSON);
        /*$http.post("http://localhost:8080/cas/create",
                    data=myJSON
        ).success(function(data){
            console.log("Uspesno dodat novi CA sertifikat!");
        });*/
    }
});

app.controller("dodajOS", function($scope,$http){

    $scope.message="NAPRAVI NOVI OBICNI SERTIFIKAT";

    var konjoslav=[{ "givenname"  : "Scala for the Impatient", "price" : 1000 },
                       { "givenname"  : "Scala in Depth", "price" : 1300 }];
    $scope.cas=konjoslav;

    /*$http.get('/ss/all')
        .success(function(response){
            $scope.cas = response;
        });*/

    $scope.add = function(certificate) {
        var myObj = { "commonName":certificate.commonName, "surename":certificate.surename, "givenname":certificate.givenname, "organisation":certificate.organisation, "organisationUnit":certificate.organisationUnit, "country":certificate.country,  "email":certificate.email, "uid":certificate.uid, "signedBy":certificate.signedBy  };

        var myJSON = JSON.stringify(myObj);
        alert(myJSON);
        /*$http.post("http://localhost:8080/os/create",
                    data=myJSON
        ).success(function(data){
            console.log("Uspesno dodat novi obicni sertifikat!");
        });*/
    }
});

app.controller("obrisiS", function($scope,$http){

    $scope.message="OBRISI SERTIFITAK PO SERIJSKOM BROJU";

    $scope.delete = function() {
        alert($scope.serialNumber);
        /*$http.post("http://localhost:8080/ss/delete/"+$scope.serialNumber
        ).success(function(data){
            console.log("Uspesno obrisan sertifikat!");
        });*/
    }
});

app.controller("nadjiS", function($scope,$http){

    $scope.message="NADJI SERTIFITAK PO SERIJSKOM BROJU";

    $scope.search = function() {
        alert($scope.serialNumber);
        /*$http.post("http://localhost:8080/ss/search/"+$scope.serialNumber
        ).success(function(data){
            console.log("Uspesno pronadjen sertifikat!");
        });*/
    }
});

app.controller("povuciS", function($scope,$http){

    $scope.message="POVUCI SERTIFITAK PO SERIJSKOM BROJU";

    $scope.withdraw = function() {
        alert($scope.serialNumber);
        /*$http.post("http://localhost:8080/ss/withdraw/"+$scope.serialNumber
        ).success(function(data){
            console.log("Uspesno povucen sertifikat!");
        });*/
    }
});