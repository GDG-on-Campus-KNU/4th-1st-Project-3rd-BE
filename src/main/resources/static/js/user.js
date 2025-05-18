var currentPage = 0;
var pageSize = 10;

$(document).ready(function() {
    getMembers(currentPage);

    $('#prevPage').click(function() {
        if (currentPage > 0) {
            getMembers(currentPage - 1);
        }
    });

    $('#nextPage').click(function() {
        getMembers(currentPage + 1);
    });
});

function getMembers(page){
    $.ajax({
        url: '/admin/user' + '?page=' + page + '&size=' + pageSize,
        method: 'GET',
        success: function(data) {
            var contents = data.content;
            var tbody = $('#member-tbody');
            tbody.empty(); // 초기화

            // 데이터에 따라 테이블 행을 생성
            contents.forEach(function(member) {
                // 한 행의 내용
                var row = `
                        <tr>
                            <td>${member.id}</td>
                            <td>${member.email}</td>
                            <td>${member.mbti}</td>
                            <td>${member.role}</td>
                            <td>
                                <a href="#" onclick="adminMemberFunction(${member.id})">권한 설정</a>
                            </td>
                            <td>
                                <a href="#" onclick="deleteMemberFunction(${member.id})">삭제</a>
                            </td>
                        </tr>
                    `;
                tbody.append(row);

                currentPage = data.number;

                $('#prevPage').prop('disabled', data.first);
                $('#nextPage').prop('disabled', data.last);
            });
        },
        error: function(err) {
            alert("권한이 없습니다!");
            window.location.href = '/admin/loginPage';
        }
    });
}

function deleteMemberFunction(id){
    $.ajax({
        type: "DELETE",
        url: "/admin/user/" + id,
        success : function(res){
            alert("유저 ID : " + id + "가 삭제되었습니다.");
            getMembers(currentPage);
        },
        error : function(error){
            alert("권한이 없습니다");
            window.location.href = '/admin/loginPage';
        }
    })
}

function adminMemberFunction(id){
    $.ajax({
        type: "PATCH",
        url: "/admin/user" + id,
        success : function(res){
            alert("유저 ID : " + id + "의 권한을 수정했습니다.");
            getMembers(currentPage);
        },
        error : function(error){
            alert("권한이 없습니다");
            window.location.href = '/admin/loginPage';
        }
    })
}