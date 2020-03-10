function post() {
     var questionId = $("#question_id").val();
     var content = $("#comment_content").val();
     comment2target(questionId,1,content);

}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("content is empty.......");
        return;
    }
    $.ajax({
        type: "POST",
        url:"/comment",
        contentType:"application/json",
        data:JSON.stringify( {
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success: function (response) {
            if (response.code == 200){
                window.location.reload();
            }else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=9c222e32e05f516f0401&redirect_uri=http://localhost:8887/callback&state=1");
                        window.localStorage.setItem("closable","true");
                    }

                }else {
                    alert(response.message);
                }

            }
            console.log(response);
        },
        dataType: "json"
    })


}



function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId,2,content);
}

/**
 * 展开二级评论
 */
function collapseComments(e){

    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    // 获取二级评论展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse){
        //展开状态需要折叠
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        $.getJSON( "/comment/"+id, function( data ) {
           console.log(data);
            console.log(data);
            // var commentBody = $("comment-body-" + id);
            // var items = [];
            // $.each(data.data, function (comment) {
            //     var c = $("<div/>", {
            //         "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
            //         html: comment.content
            //     });
            //     items.push(c);
            // });
            //
            // commentBody.append($("<div/>", {
            //     "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 collapse sub-comments",
            //     "id": "comment-" + id,
            //     html: items.join("")
            // }));
            //折叠状态需要展开
            comments.addClass("in");
            e.setAttribute("data-collapse","in");
            e.classList.add("active");

        });

    }

}