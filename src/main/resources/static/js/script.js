// jQuery code (optional, for some interactive features if needed)
$(document).ready(function () {
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
