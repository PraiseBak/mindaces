

function checkCommentValid()
{
    var user = document.getElementById("commentUser").value;
    var content = document.getElementById("commentContent").value;
    var password = document.getElementById("commentPassword").value;



    var data=
    {
        user : user,
        content : content,
        commentPassword : password
    };

    $.ajax({
        url: "/API/checkCommentValidAPI",
        type : "POST",
        data : data,
        async : false
    }).done(function (result){
        if(result == false)
        {
            alert("유효하지 않은 댓글입니다");
            return false;
        }
        else
        {
            return true;
        }
    });
}


