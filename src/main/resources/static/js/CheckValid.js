var userID = document.querySelector("#userID");
var userPassword = document.querySelector("#userPassword");
var userEmail = document.querySelector("#userEmail");
var submitBtn = document.querySelector("#submitBtn");



userEmail.onblur = function (e) {
    var data = $("#userEmail").val();
    isValidEmail = false;
    if (data.length < 4 || data.length > 40) {
        $("#resultArea").text("유효하지 않은 이메일입니다");

    }
    if (checkValidateEmail(data) === false)
    {
        $("#resultArea").text("유효하지 않은 이메일입니다");
    }
    else
    {
        checkDuplicateUserEmail(data);
        if(isValidEmail === true)
        {
            $("#resultArea").text("");
        }
    }
}

userPassword.onblur = function (e) {
    var data = $("#userPassword").val();
    isValidPassword = false;
    if (data.length < 8 || data.length > 20) {
        $("#resultArea").text("비밀번호는 최소 8자 최대 20자만 가능합니다");
    }
    else if (checkValidatePassword(data) === false) {
        $("#resultArea").text("비밀번호는 숫자와 문자가 1개 이상이어야 합니다 (특수문자 사용 가능)");
    }
    else
    {
        $("#resultArea").text("");
        isValidPassword = true;
    }

}

userID.onblur = function (e) {
    var data = $("#userID").val();
    isValidID = false;
    if (data.length < 6 || data.length > 20) {
        $("#alarmArea").text("입력은 최소 6자 최대 20자만 가능합니다");
    } else if (checkValidateID(data) === false) {
        $("#alarmArea").text("아이디는 영어로 시작하고 영어와 숫자만 가능합니다");
    }
    else
    {
        checkDuplicateUserID(data);
    }
}




function checkValidateID(id)
{
    var regex = id.search(/^[a-zA-Z]{1}[a-zA-Z0-9_]{5,19}$/);
    if(regex === -1)
    {
        return false;
    }
    return 1;
}

function checkValidatePassword(password)
{
    var regex = password.search(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d~!@#$%^&*()+|=]{7,19}$/);
    if(regex === -1)
    {
        return false;
    }
    return 1;
}

function checkValidateEmail(email)
{
    var regex = email.search(/^[_a-zA-Z0-9-.]+@[.a-zA-Z0-9-]+\.[a-zA-Z]+$/);
    if(regex === -1)
    {
        return false;
    }
    return 1;
}



function checkValid()
{
    if(isValidID && isValidEmail && isValidPassword)
    {
        return true;
    }
    $("#resultArea").text("입력란을 제대로 입력했는지 확인해주세요");
    return false;
}

