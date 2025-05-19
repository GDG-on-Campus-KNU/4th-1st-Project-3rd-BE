var currentPage = 0;
var modalCurrentPage = 0;
var pageSize = 10;
var targetUser = 0;

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

    $('#c-prevPage').click(function() {
        if (modalCurrentPage > 0) {
            getChats(modalCurrentPage - 1, targetUser);
        }
    });

    $('#c-nextPage').click(function() {
        getChats(modalCurrentPage + 1, targetUser);
    });
});

window.onclick = function (event) {
    if (event.target === document.getElementById("modal")) {
        document.getElementById("modal").style.display = "none";
    }
};

function openModal(id) {
    targetUser = id;
    getChats(modalCurrentPage, targetUser);
    document.getElementById("modal").style.display = "block";
}

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
                                <a href="#" onclick="openModal(${member.id})">로그 보기</a>
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

function getChats(page, id){
    $.ajax({
        url: '/admin/chat/' + id + '?page=' + page + '&size=' + pageSize,
        method: 'GET',
        success: function(data) {
            var contents = data.content;
            var tbody = $('#chat-tbody');
            tbody.empty(); // 초기화

            // 데이터에 따라 테이블 행을 생성
            contents.forEach(function(chat) {
                // 한 행의 내용
                var role = chat.isUserChat === true ? 'USER' : 'BOT';
                var row = `
                        <tr>
                            <td>${chat.time}</td>
                            <td>${chat.content}</td>
                            <td>${role}</td>
                        </tr>
                    `;
                tbody.append(row);

                currentPage = data.number;

                $('#c-prevPage').prop('disabled', data.first);
                $('#c-nextPage').prop('disabled', data.last);
            });
        },
        error: function(err) {
            alert("권한이 없습니다!");
            window.location.href = '/admin/loginPage';
        }
    });
}