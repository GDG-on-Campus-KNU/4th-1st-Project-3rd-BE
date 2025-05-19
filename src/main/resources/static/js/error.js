var currentPage = 0;
var pageSize = 10;

$(document).ready(function() {
    getErrors(currentPage);

    $('#prevPage').click(function() {
        if (currentPage > 0) {
            getErrors(currentPage - 1);
        }
    });

    $('#nextPage').click(function() {
        getErrors(currentPage + 1);
    });
});

window.onclick = function (event) {
    if (event.target === document.getElementById("modal")) {
        document.getElementById("modal").style.display = "none";
    }
};

function openModal(id) {
    getErrorDetail(id);
    document.getElementById("modal").style.display = "block";
}

function getErrors(page){
    $.ajax({
        url: '/admin/error' + '?page=' + page + '&size=' + pageSize,
        method: 'GET',
        success: function(data) {
            var contents = data.content;
            var tbody = $('#error-tbody');
            tbody.empty(); // 초기화

            // 데이터에 따라 테이블 행을 생성
            contents.forEach(function(error) {
                // 한 행의 내용
                var row = `
                        <tr>
                            <td>${error.id}</td>
                            <td>${error.instance}</td>
                            <td>${error.errorCode}</td>
                            <td>${error.errorType}</td>
                            <td>${error.timeStamp}</td>
                            <td>
                                <a href="#" onclick="openModal(${error.id})">StackTrace</a>
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

function getErrorDetail(id){
    $.ajax({
        type: "GET",
        url: "/admin/error/" + id,
        success : function(res){
            var content = res
            document.getElementById("modal-content").innerHTML = `<p>${content}</p>`;
        },
        error : function(error){
            alert("권한이 없습니다");
        }
    })
}

function deleteAllErrorLogs() {
    $.ajax({
        type: "DELETE",
        url: "/admin/error",
        success : function(res){
            alert("에러 로그를 삭제했습니다");
            currentPage = 0
            getErrors(currentPage);
        },
        error : function(error){
            alert("권한이 없습니다");
        }
    })
}