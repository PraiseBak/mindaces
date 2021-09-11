let isValidID = false;
let isValidEamil = false;
let isValidPassword = false;
var userID = document.querySelector("#userID");
var userPassword = document.querySelector("#userPassword");
var userEmail = document.querySelector("#userEmail");


function checkDuplicateUserID (data)
{
    var userDTO=
    {
        userID : data
    };
    $.ajax({
        url: "/API/sendIDAPI",
        data: userDTO,
        type: "POST"

    }).done(function (fragment){
        var alarmArea = $("#alarmArea");
        alarmArea.replaceWith(fragment);
        if(fragment.search("중복")=== -1)
        {
            isValidID = true;
        }
        else
        {
            isValidID = false;
        }
    });
}

function checkDuplicateUserEmail (data)
{
    var userDTO=
    {
        userEmail : data
    };
    $.ajax({
        url: "/API/sendIDAPI",
        data: userDTO,
        type: "POST"

    }).done(function (fragment){
        var alarmArea = $("#resultArea");
        alarmArea.replaceWith(fragment);
        if(fragment.search("중복")=== -1)
        {
            isValidEmail = true;
        }
        else
        {
            isValidEmail = false;
        }
    });
}