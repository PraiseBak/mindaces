function checkEmailAPI()
{
    var userID;
    var userEmail;
    var userDto;
    userID = $("#userID").val();
    userEmail = $("#userEmail").val();

    userDto =
    {
        userID : userID,
        userEmail : userEmail
    };


    $.ajax({
        data : userDto,
        url : "/API/checkEmail",
        type : "POST",
        async : false

    }).done(function (result){
        if (result)
        {
            sendEmailAPI(userDto);
            $("#resultArea").text("입력된 이메일로 임시 비밀번호를 보냈습니다");

        }
        else
        {
            $("#resultArea").text("존재하지 않는 이메일입니다");
        }
    });
}

function sendEmailAPI(userDto)
{

    $.ajax({
        data : userDto,
        url : "/API/sendEmail",
        type : "POST"
    });




}
