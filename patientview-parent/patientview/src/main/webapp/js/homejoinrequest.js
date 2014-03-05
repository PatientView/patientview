/**
 * Created by Solid State Group on 04/03/14.
 */

function sendSpecialty(username, email, url, btn){
    $.ajax({
        type: 'POST',
        url: url,
        data: {
            username: username,
            email: email
        },
        dataType: "json",
        success: function( result ) {
            btn.parentNode.innerHTML=result.message;
        }

    });

}