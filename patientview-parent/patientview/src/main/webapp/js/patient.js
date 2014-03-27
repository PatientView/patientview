/**
 * Created by james@solidstategroup.com on 21/03/2014.
 */
patient = {};

patient.init = function() {

    var patientModal = $('#patientModal');

    var specialtyId = $('.js-user-specialty-id').val();

    var units = $('.js-patient-units');
    var unitsUrl =  '/web/unitBySpecialty?specialtyId=' + specialtyId;

    units.empty();

    $.getJSON(unitsUrl, function(data) {
        $.each(data, function(i, result) {
            units.append('<option value=' + result.id + '>' + result.name + '</option>');
        });
    });

    var patientForm = $('.js-patient-form');

    patientForm.submit(function(event) {
        event.preventDefault();
        patient.add(patientForm);
    });

};


patient.add = function(form) {


    var $form = $(form),
        username = $form.find('.js-patient-username'),
        firstName = $form.find('.js-patient-firstname'),
        lastName = $form.find('.js-patient-lastname'),
        nhsNo = $form.find('.js-patient-nhsno'),
        override = $form.find('.js-patient-override-nhsno'),
        email = $form.find('.js-patient-email'),
        unitCode = $form.find('.js-patient-units'),
        errorsEl = $form.find('.js-message-errors'),
        errors = [],
        messagesEl = $('.js-messages'),
        data ={}
    onError = function(errorSt) {
        errorsEl.html(errorSt).show();
    };

    errorsEl.html('').hide();

    data.username = username.val();
    data.firstName = firstName.val();
    data.lastName =  lastName.val();
    data.nhsNo = nhsNo.val();
    data.unitCode = unitCode.val();
    data.email = email.val();
    data.emailVerified = false;
    data.overrideDuplicateNhsNo = false;
    data.firstLogon = false;
    data.dummyPatient = false;
    data.accountLocked = false;
    data.accountHidden = false;

    if (errors.length > 0) {
        onError(errors.join('<br />'));
        return false;
    } else {
        $.ajax({
            type: "POST",
            url: $form.attr('action'),
            data: JSON.stringify(data),
            success: function(data) {
                alert('Sucess');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                onError(textStatus);
            },
            dataType: 'json',
            contentType: "application/json; charset=UTF-8"
        });
    }

}

patient.validateString = function(s) {
    return s && s.length > 0;
};


$(function() {
    patient.init();
});