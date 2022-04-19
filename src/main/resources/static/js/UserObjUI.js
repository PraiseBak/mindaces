let btnHTML = "<a class=\"form-check-input\">🖤</a>";
let checkHTML = "<input type=\"checkbox\" class=\"form-check-input objCheck\"></input>";
let prevHTML = "";

function toggleCSS(changeHTML)
{

    if (prevHTML != changeHTML)
    {
        $(".form-check-input").each(function (){
            $(this).replaceWith(changeHTML);
        });
    }

    $(".form-check-input").each(function (){
        if(this.style.display == "none")
        {
            $(this).css("display", "block");
        }else
        {
            $(this).css("display", "none");
        }
    });
    prevHTML = changeHTML;
}

function checkToggle()
{
    toggleCSS(checkHTML);
}

function buttonToggle()
{
    toggleCSS(btnHTML);
}

