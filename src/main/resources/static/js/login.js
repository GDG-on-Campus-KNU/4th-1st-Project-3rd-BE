$(document).ready(function() {
    // 입력 필드에서 Enter 키를 눌렀을 때 로그인 버튼 클릭
    $("#userId, #password").keydown(function(event) {
        if (event.key === "Enter") { // Enter 키가 눌렸을 때
            $("#login").click(); // 로그인 버튼 클릭
        }
    });

    // 로그인 버튼 클릭 시 동작
    $("#login").click(function(){
        var user = {
            email: $("#userId").val(),
            password : $("#password").val()
        };

        $.ajax({
            type : "POST",
            url : "/auth/login",
            contentType: "application/json",
            datatype : "json",
            data : JSON.stringify(user),
            success : function(res) {
                successLogin()
            },
            error : function(err){
                alert(err.responseJSON.detail);
            }
        });
    });
});

function successLogin(value) {
    window.location.href = "/admin";
}