// jQuery code (optional, for some interactive features if needed)
$(document).ready(function () {
    let call_btn = $('#start-call');
    call_btn.click(function (event) {
        console.log('클릭함수 진입');
        event.preventDefault(); // 폼 제출 방지
        let difficulty_val = $('input[name="difficulty"]:checked').val();
        let ingredient_val = $('#ingredients').val();
        $("#loading").fadeIn(200);
        // AJAX 요청
        $.ajax({
            type: 'POST',
            url: '/ai', // 서버로 경로 지정 (닉네임 중복 체크)
            headers: { 'Content-Type': 'application/json' }, // json 데이터로 지정
            data: JSON.stringify({ ingredient: ingredient_val,
                difficulty: difficulty_val }), // JSON 형식으로 데이터 전송
            success: function (result) {
                if (result) {
                    let markGptResponse = marked(result.gptResponse);
                    $("#loading").fadeOut(200, function () {
                        $('#result-container').fadeIn(500);
                        $("#result-content").html(markGptResponse);
                    })
                } else {
                    alert('값받기 실패');
                }
            },
            error: function () {
                alert('서버 오류가 발생했습니다.');
            },
        });
    });
});
