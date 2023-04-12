
function onSignup(){

    let info = {
        memail : document.querySelector(".memail").value,
        mpassword : document.querySelector(".mpassword").value,
        mname : document.querySelector(".mname").value,
        mphone : document.querySelector(".mphone").value
    }

    $.ajax({
        url:"/member/info",
        method : "post",
        data : JSON.stringify(info),
        contentType : "application/json",
        success : (r)=>{
            console.log(r);
            if(r==true){alert("가입이 되셨습니다."); }
        }
    })
}

function onLogin(){

    let info = {
        memail : document.querySelector(".memail").value,
        mpassword : document.querySelector(".mpassword").value
    }

    $.ajax({
        url:"/member/login",
        method : "post",
        data : JSON.stringify(info),
        contentType : "application/json",
        success : (r)=>{
            console.log(r);
            if(r==true){alert("로그인이 되셨습니다."); }
        }
    })
}