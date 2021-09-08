var commentSubmitMode = "modify";
var curURL = window.location.pathname;
var prevInputTag = null;
var prevCheckTag = null;

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

function showInput(tag,mode)
{
    this.mode = mode;
    commentSubmitMode = mode;

    var inputArea = tag.parentNode.querySelector("#inputCommentPassword");
    var checkBtn = tag.parentNode.querySelector("#checkBtn");
    if(inputArea.style.display === "none")
    {
        if(prevInputTag != null)
        {
            prevInputTag.style.display = "none";
            prevCheckTag.style.display = "none";
        }
        prevInputTag = inputArea;
        prevCheckTag = checkBtn;
        inputArea.style.display = "block";
        checkBtn.style.display = "block";
    }
    else
    {
        inputArea.style.display = "none";
        checkBtn.style.display = "none";
    }

}


function checkAndSubmit(childTag,commentIdx)
{
    var formTag = childTag.parentNode;
    var inputArea = formTag.parentNode.querySelector("#inputCommentPassword");

    var commentDto =
    {
        commentIdx : commentIdx,
        commentPassword : inputArea.value
    };

    $.ajax({
        url : "/API/checkCommentPasswordAPI",
        type : "POST",
        data : commentDto,
        async: false

    }).done(function (result){
        if(result == false)
        {
            alert("비밀번호가 일치하지 않습니다");
        }
        else
        {
            if(commentSubmitMode == "deleteComment")
            {
                var url = formTag.action;
                if(url.charAt(url.length-1) == '/')
                {
                    url += commentSubmitMode;
                }
                else
                {
                    url += "/" + commentSubmitMode;
                }

                formTag.action = url;
                formTag.submit();
            }
            else
            {

            }
        }
    });


}
