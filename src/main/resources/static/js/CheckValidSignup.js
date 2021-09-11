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
    if (data.length < 2 || data.length > 10) {
        $("#alarmArea").text("입력은 최소 2자 최대 10자만 가능합니다");
    } else if (checkValidateID(data) === false) {
        $("#alarmArea").text("닉네임은 한글, 영문, 숫자만 가능합니다");
    }
    else
    {
        checkDuplicateUserID(data);
    }
}


function checkValidateID(id)
{
    //닉네임은 한글, 영문, 숫자만 가능하며 2-10자리 가능.
    var regex = id.search(/^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]).{1,10}$/);
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

function checkValidSignup()
{
    if(isValidID && isValidEmail && isValidPassword)
    {
        return true;
    }
    $("#resultArea").text("입력란을 제대로 입력했는지 확인해주세요");
    return false;
}



