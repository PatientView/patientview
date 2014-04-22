/**
 * Created by jameseaton@solidstategroup.com on 22/04/2014.
 */

ibd = {};

ibd.init = function() {

    ibd.populateDropdown($('.js-ibd-manifestations'), '/web/lookup/component?name=js-ibd-manifestations');
    ibd.populateDropdown($('.js-ibd-complications'), '/web/lookup/component?name=js-ibd-complications');
    ibd.populateDropdown($('.js-ibd-smoking-history'), '/web/lookup/component?name=js-ibd-smoking-history');
    ibd.populateDropdown($('.js-ibd-primary-diagnosis'), '/web/lookup/component?name=js-ibd-primary-diagnosis');
    ibd.populateDropdown($('.js-ibd-disease-extent'), '/web/lookup/component?name=js-ibd-disease-extent');
    ibd.populateDropdown($('.js-ibd-surgical-history'), '/web/lookup/component?name=js-ibd-surgical-history');


}

ibd.populateDropdown = function(dropdown, url) {
    $.getJSON(url, function(data) {
        $.each(data, function(i, result) {
            dropdown.append('<option value=' + result.value + ' type="checkbox">' + result.text + '</option>');
        });
    });

}

ibd.add = function(form) {

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


ibd.refreshSurgicalHistory = function(form, surgeries) {

    var surgeryDisplay = form.find('.js-ibd-surgical-history-display');
    surgeryDisplay.children("tbody").empty();

    surgeryDisplay.append('<tr><td> ' + $('.js-ibd-surgical-history-date').val + '</td><td>' + $('.js-ibd-surgical-history-date').val + '</td><td></td></tb></tr>');


}

ibd.addSurgicalHistory = function(form) {

    var surgeryHistory = $('.js-ibd-surgical-history-text');
    var surgeryDate = $('.js-ibd-surgical-history-date');


    var surgeries = [];
    var surgery = {};
    surgery.surgeryHistory = surgeryHistory.val();
    surgery.surgeryDate = surgeryDate.val();

    surgeries.push(surgery);

    ibd.refreshSurgicalHistory(form, surgeries);

}


$(function() {
    ibd.init();
});

