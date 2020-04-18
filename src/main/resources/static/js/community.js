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
        var subCommentContainer = $("#comment-"+id);
        if (subCommentContainer.children().length != 1) {
            comments.addClass("in");
            e.setAttribute("data-collapse","in");
            e.classList.add("active");

        } else {
            debugger;
            $.getJSON("/comment/" + id, function (data) {
                console.log(data)
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }

    }


}
function selectTag(e) {
    var value = e.getAttribute("data-tag")
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1){
        if (previous) {
            $("#tag").val(previous + ',' + value);
        }else {
            $("#tag").val(value);
        }
    }

}
function showSelectTag() {
    $("#select-tag").show();


}
function deleteQuestion(e) {
    var questionId = e.getAttribute("data-id");
    console.log(questionId);
    var isDeleted = confirm("delete forever ?");
    if (isDeleted) {
        window.open("http://localhost:8887/"+questionId+"/delete");
    }


}
function thumbComments(e) {
    debugger;
    var thumbId = e.getAttribute("id");
    var url = e.getAttribute("data-id");
    var tags =$("#"+thumbId).children("#thumbChildElement");

    $.getJSON("/thumb/" + url ,function(data) {
        //span标签赋值用html,表单一般用val
        tags.html(data);
    });


}

function sendEmail() {
    var email= $('input[name="email"]').val();
    if (email == null || email.trim().length === 0) {
        alert("请输入邮箱！");
        return;
    }
    $.getJSON("/sendEmail/" + email,function (data) {
        if (data.code === 200) {
            invokeSetTime("#send-email-btn");
        } else {
            alert(data.message);
        }
    });
    
}

function invokeSetTime(obj) {
    let countdown = 60;
    setTime(obj);

    function setTime(obj) {
        if (countdown === 0) {
            $(obj).attr("disabled", false);
            $(obj).text("GetCode");
            countdown = 60;
            return;
        } else {
            $(obj).attr("disabled", true);
            $(obj).text("(" + countdown + ") s resend");
            countdown--;
        }
        setTimeout(function () {
            setTime(obj)
        }, 1000);
    }
}