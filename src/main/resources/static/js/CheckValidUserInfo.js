var userID = document.querySelector("#userID");
var userPassword = document.querySelector("#userPassword");
var userEmail = document.querySelector("#userEmail");
var submitBtn = document.querySelector("#submitBtn");
var checkMode = "signup";
var changePasswordValidZero = false;
var changePasswordValidOne = false;
var changePasswordValidTwo = false;


function addBlurCheckEvent(mode,id)
{
    if (mode === "signup")
    {
        userID.addEventListener("blur",displayValidateID,true);
        userPassword.addEventListener("blur",function (){displayValidatePassword(userPassword)},true);
        userEmail.addEventListener("blur",displayValidateEmail,true);
        return;
    }

    if(mode === "password")
    {
        checkMode = "changePassword";
        var tmpPasswordInput = document.querySelector(id);
        tmpPasswordInput.addEventListener("blur",function (){displayValidatePassword(tmpPasswordInput)},true);
    }
}

function displayAllValidateResult() {
    if (!isValidID) {
        displayValidateID();
    }
    else if (!isValidPassword)
    {
        displayValidatePassword();
    }
    else if (!isValidEmail)
    {
        displayValidateEmail();
    }
    if (isValidID && isValidPassword && isValidEmail)
    {
        $("#resultArea").text("가입 가능한 정보입니다");
    }
}

function displayValidateEmail()
{
    var data = userEmail.val;
    isValidEmail = false;
    if (data.length < 4 || data.length > 40) {
        $("#resultArea").text("유효하지 않은 이메일입니다");
    }
    else if (checkValidateEmail(data) === false)
    {
        $("#resultArea").text("유효하지 않은 이메일입니다");
    }
    else
    {
        checkDuplicateUserEmail(data);
        if(isValidEmail === true)
        {
            displayAllValidateResult();
        }
    }
}



function displayValidatePassword(passwordInput)
{
    isValidPassword = false;
    let data = passwordInput.value;
    setValidMarkByName(passwordInput.name,false);


    if (data.length < 8 || data.length > 20) {
        $("#resultArea").text("비밀번호는 최소 8자 최대 20자만 가능합니다");
    }
    else if (checkValidatePassword(data) === false) {
        $("#resultArea").text("비밀번호는 숫자와 문자가 1개 이상이어야 합니다");
    }
    else
    {
        $("#resultArea").text("");
        if(checkMode !== "changePassword")
        {
            displayAllValidateResult();
        }
        else
        {
            setValidMarkByName(passwordInput.name,true);
            checkChangePasswordValid();
        }

        isValidPassword = true;
    }

}

function isChangePasswordValid()
{
    return changePasswordValidOne && changePasswordValidTwo && changePasswordValidZero;
}



function checkChangePasswordValid()
{

    if (isChangePasswordValid())
    {
        $("#resultArea").text("");
        if (($("#objUserPasswordOne").val()) !== ($("#objUserPasswordTwo").val()) )
        {
            $("#resultArea").text("변경할 비밀번호가 동일하지 않습니다");
            return false;
        }
        return true;
    }

    if( ($("#originUserPassword").val() !== "")
        && ($("#objUserPasswordOne").val() !== "")
        && ($("#objUserPasswordTwo").val() !== ""  ))
    {
        $("#resultArea").text("입력란을 확인해주십시오");
    }


    return false;
}



function displayValidateID()
{
    var data = userID.value;
    isValidID = false;
    if (data.length < 2 || data.length > 20) {
        $("#resultArea").text("입력은 최소 2자 최대 10자만 가능합니다");
    } else if (checkValidateID(data) === false) {
        $("#resultArea").text("닉네임은 한글, 영문, 숫자만 가능합니다");
    }
    else
    {
        checkDuplicateUserID(data);
        if (isValidID === true)
        {
            displayAllValidateResult();
        }
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

function checkValidUserInfo()
{
    if(isValidID && isValidEmail && isValidPassword)
    {
        return true;
    }
    $("#resultArea").text("입력란을 제대로 입력했는지 확인해주세요");
    return false;
}



function setValidMarkByName(name,mode)
{
    if (name.search("origin") != -1)
    {
        changePasswordValidZero = mode;
    }

    else if (name.search("One") != -1)
    {
        changePasswordValidOne = mode;

    }
    else if (name.search("Two") != -1)
    {
        changePasswordValidTwo = mode;
    }
}



function doubleCheckPassword()
{
    addBlurCheckEvent("password","#originUserPassword");
    addBlurCheckEvent("password","#objUserPasswordOne");
    addBlurCheckEvent("password","#objUserPasswordTwo");

    var objPasswordOne = $("#objUserPasswordOne").value;
    var objPasswordTwo = $("#objUserPasswordTwo").value;
    if(objPasswordOne !== objPasswordTwo)
    {
        $("resultArea").text("변경하려는 비밀번호가 일치하지 않습니다");
        return false;
    }

    return true;
}





