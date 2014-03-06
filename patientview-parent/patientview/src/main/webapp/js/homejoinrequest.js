/**
 * Created by Solid State Group on 04/03/14.
 */

joinrequest = {}


joinrequest.init = function()  {

    var joinRequestModel = $('#joinRequestModel');


    $(document).ready(function () {

        $("#specialty").click("change", function() {
            alert( "The specialty changed." );
        });

        $("#unitcode").click("change",function() {
            alert( "The unitcode changed." );
        });



    });
}


joinrequest.getJoinRequest = function(form) {

    var $form = $(form),
        submitBtn = $form.find('.js-message-submit-btn'),
        originalBtnValue = submitBtn.val(),
        firstName = $form.find('.js-joinrequest-firstname'),
        lastName = $form.find('.js-joinrequest-lastname'),
        dateOfBirth = $form.find('.js-joinrequest-dob'),
        nhsNumber = $form.find('.js-joinrequest-nhs-no'),
        unitCode = $form.find('.js-joinrequest-unitcode'),
        specialty = $form.find('.js-joinrequest-specialty'),
        email = $form.find('.js-joinrequest-email'),
        securityQuestion = $form.find('.js-joinrequest-security-question'),
        errors = [],
        messagesEl = $('.js-messages'),
        data ={}
        onError = function (errorSt) {
            errorsEl.html(errorSt).show();
            submitBtn.val(orin)
        };

    errorsEl.html('').hide();

    data.firstName = firstName.val();
    data.lastName = lastName.val();
    data.dateOfBirth =  dateOfBirth.val();
    data.nhsNumber = nhsNumber.val();
    data.unitCode = unitCode.val();
    data.email = email.val();
    data.securityQuestion = securityQuestion.val();

    $.ajax({
        type: "POST",
        url: $form.attr('action'),
        data: data,
        success: function(data) {
            if (data.errors.length > 0) {
                onError(data.errors.join('<br />'));
            } else {
                submitBtn.val(originalBtnValue);
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            onError(textStatus);
        },
        dataType: 'json'
    });

}

// add in a dom ready to fire utils.init
$(function() {
    joinrequest.init();
});







