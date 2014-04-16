/**
 * Created by james@solidstategroup.com on 21/03/2014.
 */
patient = {};

patient.init = function() {

    var specialtyId = $('.js-user-specialty-id').val();

    var units = $('.js-patient-units');
    var unitsUrl =  '/web/unitBySpecialty?specialtyId=' + specialtyId;

    units.empty();

    $.getJSON(unitsUrl, function(data) {
        $.each(data, function(i, result) {
            units.append('<option value=' + result.id + '>' + result.name + '</option>');
        });
    });


    patient.populateDropdown($('.js-ibd-manifestations'), '/web/lookup/component?name=js-ibd-manifestations');
    patient.populateDropdown($('.js-ibd-complications'), '/web/lookup/component?name=js-ibd-complications');
    patient.populateDropdown($('.js-ibd-smoking-history'), '/web/lookup/component?name=js-ibd-smoking-history');
    patient.populateDropdown($('.js-ibd-primary-diagnosis'), '/web/lookup/component?name=js-ibd-primary-diagnosis');
    patient.populateDropdown($('.js-ibd-disease-extent'), '/web/lookup/component?name=js-ibd-disease-extent');
    patient.populateDropdown($('.js-ibd-surgical-history'), '/web/lookup/component?name=js-ibd-surgical-history');

    var patientForm = $('.js-patient-form');

    patientForm.submit(function(event) {
        event.preventDefault();
        patient.add(patientForm);
    });

    var surgicalFrom = $('.js-ibd-surgical-history-input');

    surgicalFrom.submit(function(event) {
        event.preventDefault();
        patient.addSurgicalHistory(surgicalFrom);
    });

    $('.js-ibd-surgical-history-add').click(function() {
        surgicalFrom.submit();
    });


};

patient.populateDropdown = function(dropdown, url) {
    $.getJSON(url, function(data) {
        $.each(data, function(i, result) {
            dropdown.append('<option value=' + result.value + ' type="checkbox">' + result.text + '</option>');
        });
    });

}


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
        data = {}
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
                alert('Success');
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


patient.refreshSurgicalHistory = function(form, surgeries) {

    var surgeryDisplay = form.find('.js-ibd-surgical-history-display');
    surgeryDisplay.children("tbody").empty();

    surgeryDisplay.append('<tr><td> ' + $('.js-ibd-surgical-history-date').val + '</td><td>' + $('.js-ibd-surgical-history-date').val + '</td><td></td></tb></tr>');


}

patient.addSurgicalHistory = function(form) {

    var surgeryHistory = $('.js-ibd-surgical-history-text');
    var surgeryDate = $('.js-ibd-surgical-history-date');


    var surgeries = [];
    var surgery = {};
    surgery.surgeryHistory = surgeryHistory.val();
    surgery.surgeryDate = surgeryDate.val();

    surgeries.push(surgery);

    patient.refreshSurgicalHistory(form, surgeries);

}

$(function() {
    patient.init();
});