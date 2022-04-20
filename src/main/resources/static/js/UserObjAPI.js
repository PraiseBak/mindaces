
function test()
{
    return "test";
}


function getSelectedObjIdxList()
{
    let classInst = checkBoxClass + ".objDelete";
    let idxList = [];
    $(".userObjTable").find('input:checked').each(function (){
        let idStr = this.id + "";
        idxList.push(idStr.split("-")[1]);
    });

    return idxList;
}

function updateUserObjAjax(input, isDelete)
{
    let userObjDto =
    {
        objIdx : input
    };
    let apiURL = "/API/setRepresentObj";
    if(isDelete)
    {
        apiURL = "/API/delObjs";
    }
    $.ajax({
        type : "POST",
        url : apiURL,
        data : userObjDto,
        async : false
    }).done(function (result){
        if (result == false)
        {
            alert("삭제하는데 실패하였습니다");
        }
        else
        {
            location.reload();
        }
    });

}

//delete or set userobj represent by ajax
function updateUserObjInfo(input)
{
    let isDelete = input === "delete";
    if (isDelete)
    {
        let selectedItem = getSelectedObjIdxList();
        updateUserObjAjax(selectedItem,isDelete);
    }
    else
    {
        updateUserObjAjax(input,isDelete);
    }
}

