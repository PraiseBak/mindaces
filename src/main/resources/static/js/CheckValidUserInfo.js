var userID = document.querySelector("#userID");
var userPassword = document.querySelector("#userPassword");
var userEmail = document.querySelector("#userEmail");
var submitBtn = document.querySelector("#submitBtn");


function addBlurCheckEvent(mode)
{
    if (mode === "signup")
    {
        userID.addEventListener("blur",displayValidateID);
        userPassword.addEventListener("blur",function (){displayValidatePassword(userPassword)});
        userEmail.addEventListener("blur",displayValidateEmail);
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
    var data = userEmail.value;
    isValidEmail = false;
    if (data.length < 4 || data.length > 40) {
        $("#resultEmail").text("유효하지 않은 이메일입니다");
    }
    else if (checkValidateEmail(data) === false)
    {
        $("#resultEmail").text("유효하지 않은 이메일입니다");
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


function displayValidatePassword()
{
    var data;
    data = userPassword.value;

    if (data.length < 8 || data.length > 20) {
        $("#resultPW").text("비밀번호는 최소 8자 최대 20자만 가능합니다");
    }
    else if (checkValidatePassword(data) === false) {
        $("#resultPW").text("비밀번호는 숫자와 문자가 1개 이상이어야 합니다");
    }
    else
    {
        $("#resultPW").text("사용가능한 비밀번호입니다");
        isValidPassword = true;
    }

}

function displayValidateID()
{
    var data = userID.value;
    isValidID = false;
    if (data.length < 2 || data.length > 20) {
        $("#resultID").text("입력은 최소 2자 최대 20자만 가능합니다");
    } else if (checkValidateID(data) === false) {
        $("#resultID").text("닉네임은 한글, 영문, 숫자만 가능합니다");
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
    //닉네임은 한글, 영문, 숫자만 가능하며 2-20자리 가능.
    var regex = id.search(/^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣])/);
    //var regex = id.search(/^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]).{1,10}$/);
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
