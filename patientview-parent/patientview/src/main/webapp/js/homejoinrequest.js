/**
 * Created by Solid State Group on 04/03/14.
 */

joinrequest = {}


joinrequest.init = function()  {

    var joinRequestModel = $('#joinRequestModel');


    $(document).ready(function () {

        $.getJSON('/web/listSpecialties', function(data) {

            var specialties = $('.js-joinrequest-specialty');

            $.each(data, function(i, result) {
                specialties.append('<option value=' + result.id + '>' + result.name + '</option>');
            });

            specialties.change(function() {

                var units = $('.js-joinrequest-unit');
                var unitsUrl =  '/web/unitBySpecialty?specialtyId=' + specialties.val();

                units.empty();

                $.getJSON(unitsUrl, function(data) {
                    $.each(data, function(i, result) {
                        units.append('<option value=' + result.id + '>' + result.name + '</option>');
                    });
                });

            });

            var joinForm = $('.js-joinrequest-form');

            joinForm.submit(function(event) {
                event.preventDefault();
                joinrequest.sendJoinRequest(joinForm);
            });

        });


    });
}

joinrequest.sendJoinRequest = function(form) {


    var $form = $(form),
        firstName = $form.find('.js-joinrequest-firstname'),
        lastName = $form.find('.js-joinrequest-lastname'),
        dateOfBirth = $form.find('.js-joinrequest-dob'),
        nhsNumber = $form.find('.js-joinrequest-nhs-no'),
        unitCode = $form.find('.js-joinrequest-unit'),
        specialty = $form.find('.js-joinrequest-specialty'),
        email = $form.find('.js-joinrequest-email'),
        securityQuestion = $form.find('.js-joinrequest-security-question'),
        errorsEl = $form.find('.js-message-errors'),
        errors = [],
        messagesEl = $('.js-messages'),
        data ={}
        onError = function(errorSt) {
           errorsEl.html(errorSt).show();
        };

    errorsEl.html('').hide();

    if (!joinrequest.validateString(firstName.val())) {
        errors.push('Please enter a firstname');
    }

    if (!joinrequest.validateString(lastName.val())) {
        errors.push('Please enter a lastname');
    }

    if (!joinrequest.validateString(lastName.val())) {
        errors.push('Please enter an email');
    }


    data.firstName = firstName.val();
    data.lastName = lastName.val();
    data.dateOfBirth =  dateOfBirth.val();
    data.nhsNumber = nhsNumber.val();
    data.unitId = unitCode.val();
    data.email = email.val();
    data.securityQuestion = securityQuestion.val();

    if (errors.length > 0) {
        onError(errors.join('<br />'));
        return false;
    } else {
        $.ajax({
            type: "POST",
            url: $form.attr('action'),
            data: data,
            success: function(data) {
                alert('Your join request has been sent');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                onError(textStatus);
            },
            dataType: 'json'
        });
    }

}

joinrequest.validateString = function(s) {
    return s && s.length > 0;
};



$(function() {
    joinrequest.init();
});







