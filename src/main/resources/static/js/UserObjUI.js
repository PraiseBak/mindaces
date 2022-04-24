let checkBoxClass = ".form-check-input";

function toggleCSS(mode)
{
    let classInst = mode;
    let anotherInst = (mode === "objDelete") ? "objRepresent" : "objDelete";

    let btnDom = $("#checkObjBtn");
    if (btnDom.css("display") == "none")
    {
        btnDom.css("display","");
    }
    else
    {
        btnDom.css("display","none");
    }

    $(checkBoxClass + "." + classInst).each(function (){
        if(this.style.display == "none")
        {
            $(this).css("display", "block");
        }else
        {
            $(this).css("display", "none");
        }
    });

    $(checkBoxClass + "." + anotherInst).each(function (){
        $(this).css("display", "none");
    });

}


