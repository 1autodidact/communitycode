function post() {
     var questionId = $("#question_id").val();
     var content = $("#comment_content").val();
     if (!content) {
         alert("content is empty.......");
         return;
     }
     $.ajax({
         type: "POST",
         url:"/comment",
         contentType:"application/json",
         data:JSON.stringify( {
                 "parentId":questionId,
                 "content":content,
                 "type":1
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