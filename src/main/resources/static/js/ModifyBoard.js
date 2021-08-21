
var modifyBtn = document.getElementById("modifyBtn");
var deleteBtn = document.getElementById("deleteBtn");

function getPassword(contentIdx,url)
{
    var inputPassword = prompt("비밀번호를 입력해주세요");
    //var hiddenPassword = document.getElementById("password");
    //hiddenPassword.setAttribute("value",inputPassword);
    checkBoardPasswordAjax(inputPassword,contentIdx,url);
}


function checkBoardPasswordAjax(inputPassword,contentIdx,url)
{
    alert(url);
    var boardDto =
    {
        contentIdx: contentIdx,
        password: inputPassword
    };

    $.ajax({
        url: "/checkBoardPasswordAPI",
        data: boardDto,
        type: "POST"
    }).done(function (result){
        if(result === true)
        {
            alert("통과");
            location.href= url;


        }
        else
        {
            alert("통과실패");
        }

    });
}

