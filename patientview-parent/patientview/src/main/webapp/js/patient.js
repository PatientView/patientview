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

};


patient.add = function(form) {


    var $form = $(form),
        username = $form.find('.js-patient-username'),
        firstname = $form.find('.js-patient-firstname'),
        lastname = $form.find('.js-patient-lastname'),
        nhsno = $form.find('.js-patient-nhsno'),
        override = $form.find('.js-patient-override-nhsno'),
        email = $form.find('.js-patient-email'),
        unitcode = $form.find('.js-patient-units'),
        errorsEl = $form.find('.js-message-errors'),
        errors = [],
        messagesEl = $('.js-messages'),
        data ={}
    onError = function(errorSt) {
        errorsEl.html(errorSt).show();
    };

    errorsEl.html('').hide();

    data.username = username.val();
    data.firstname = firstname.val();
    data.lastname =  lastname.val();
    data.nhsno = nhsno.val();
    data.unitcode = unitcode.val();
    data.email = email.val();

    if (errors.length > 0) {
        onError(errors.join('<br />'));
        return false;
    } else {
        $.ajax({
            type: "POST",
            url: $form.attr('action'),
            data: data,
            success: function(data) {
                alert('Sucess');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                onError(textStatus);
            },
            dataType: 'json'
        });
    }

}

patient.validateString = function(s) {
    return s && s.length > 0;
};


$(function() {
    patient.init();
});