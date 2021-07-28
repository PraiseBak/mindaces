function sendUserID()
{
    var data=$("#userID").val();


    if(data.length < 6 || data.length > 20)
    {
        $("#alarmArea").text("입력은 최소 6자 최대 20자만 가능합니다");
    }
    else if(!checkValidateID(data))
    {
        $("#alarmArea").text("영어와 숫자만 입력해주세요");
    }
    else
    {
        var userDTO=
        {
            userID : data
        };
        $.ajax({
            url: "/sendIDAPI",
            data: userDTO,
            type: "POST"
            //,
            //success:function(fragment){
            //  alert(fragment);
            //const element = document.getElementById("resultDiv");
            //element.innerText = "쉥스농";
            //}

        }).done(function (fragment){
            $("#alarmArea").replaceWith(fragment);
        });
    }
}

function checkValidateID(id)
{
    var spe = id.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
    var korean = id.search(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/gi);
    if (id.search(/₩s/) != -1)
    {
        return false;
    }
    if (spe > 0 || korean > 0) {
        return false;
    }
    return true;

}

