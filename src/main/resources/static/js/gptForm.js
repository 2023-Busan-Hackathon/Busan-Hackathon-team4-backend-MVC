// jQuery code (optional, for some interactive features if needed)
$(document).ready(function () {
    let call_btn = $('#start-call');
    call_btn.onclick = function (event) {
        event.preventDefault(); // 폼 제출 방지
        let difficulty_val = $('input[name="difficulty"]:checked').val();
        let ingredient_val = document.querySelector('#myText').value;

        // AJAX 요청
        $.ajax({
            type: 'POST',
            url: '/ai', // 서버로 경로 지정 (닉네임 중복 체크)
            headers: { 'Content-Type': 'application/json' }, // json 데이터로 지정
            data: JSON.stringify({ ingredient: ingredient_val,
                difficulty: difficulty_val }), // JSON 형식으로 데이터 전송
            success: function (result) {
                if (result) {
                    alert('값을 받아옴');
                    console.log(result);
                } else {
                    alert('값받기 실패');
                }
            },
            error: function () {
                alert('서버 오류가 발생했습니다.');
            },
        });
        var markdownResult = "# Recipe Title\n\n- Ingredient 1\n- Ingredient 2\n- Ingredient 3\n";
        var htmlResult = marked(markdownResult); // Convert markdown to HTML
        $("#loading").fadeOut(200, function () {
            $("#result-container").html(htmlResult).fadeIn(500);
        });
    };

    $("#recipe-form").submit(function (event) {
        event.preventDefault();
        $("#recipe-form").fadeOut(500, function () {
            $("#loading").fadeIn(300, function () {
                // Simulate server response time (you should replace this with actual API call)
                setTimeout(function () {
                    // Here, you can call your server API to get the markdown result
                    // For demonstration purposes, let's assume the result is in a variable called 'markdownResult'
                    var markdownResult = "# Recipe Title\n\n- Ingredient 1\n- Ingredient 2\n- Ingredient 3\n";
                    var htmlResult = marked(markdownResult); // Convert markdown to HTML
                    $("#loading").fadeOut(200, function () {
                        $("#result-container").html(htmlResult).fadeIn(500);
                    });
                }, 2000); // Simulate 2 seconds server response time
            });
        });

    });
    $("#replay-button").click(function () {
        $("#result-container").fadeOut(500, function () {
            $("#recipe-form").fadeIn(500);
        });
    });
});
