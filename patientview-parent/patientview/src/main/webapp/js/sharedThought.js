/*
 * PatientView
 *
 * Copyright (c) Worth Solutions Limited 2004-2013
 *
 * This file is part of PatientView.
 *
 * PatientView is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * PatientView is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with PatientView in a file
 * titled COPYING. If not, see <http://www.gnu.org/licenses/>.
 *
 * @package PatientView
 * @link http://www.patientview.org
 * @author PatientView <info@patientview.org>
 * @copyright Copyright (c) 2004-2013, Worth Solutions Limited
 * @license http://www.gnu.org/licenses/gpl-3.0.html The GNU General Public License V3.0
 */

sharedThought = {};
sharedThought.responderTable = $('#tableOtherSharedThoughtResponders');
sharedThought.responderSelect = $('#selectOtherSharedThoughtResponders');
sharedThought.responderTr = $('#trOtherSharedThoughtResponders');
sharedThought.responderAddBtn = $('#btnAddOtherSharedThoughtResponder');
sharedThought.responderRemoveBtn = '.removeUserSharedThought';
sharedThought.responderMessageBtn = $('#btnAddMessage');
sharedThought.responderMessageTextarea = $('#textareaMessage');
sharedThought.responderMessageTrNoComments = $('#trNoComments');
sharedThought.responderAddMessage = $('#messageAddOtherSharedThoughtResponder');
sharedThought.id = $('#sharedThoughtId').val();
sharedThought.isAnonymous = $('#sharedThoughtIsAnonymous').val();
sharedThought.userFullName = $('#userFullName').val();
sharedThought.closeBtn = $('#btnOpenCloseSharedThought');

// message to patient
sharedThought.sendMessageBtn = $('#btnSendMessage')
sharedThought.messageModal = $('#messageModal');
sharedThought.messageSubtitle = $('#messageSubtitle');
sharedThought.messageBackground = $('#messageBackground');
sharedThought.messageForm = $('#js-message-form');
sharedThought.messageSuccessDiv = $('#js-message-success');
sharedThought.messageLoadingDiv = $('#js-message-loading');
sharedThought.submitMessageBtn = $('#js-message-submit-btn');
sharedThought.cancelMessageBtn = $('#js-message-cancel-btn');
sharedThought.closeMessageBtn = $('#js-message-close-btn');

/**
 * Set up buttons
 */
sharedThought.init = function() {

    // conversation
    sharedThought.responderMessageBtn.click(function(event) {
        event.preventDefault();
        sharedThought.addMessage();
    });

    sharedThought.responderAddBtn.click(function(event) {
        event.preventDefault();
        sharedThought.addResponder();
    });

    $(document.body).on("click", sharedThought.responderRemoveBtn, function(event) {
        event.preventDefault();
        sharedThought.removeResponder($(this));
    });

    // set thought open/closed
    sharedThought.closeBtn.click(function(event) {
        event.preventDefault();
        sharedThought.openCloseSharedThought();
    });

    // sending message to patient
    sharedThought.sendMessageBtn.click(function(event) {
        event.preventDefault();
        sharedThought.showMessageToPatientDialog();
    });

    sharedThought.cancelMessageBtn.click(function(event) {
        event.preventDefault();
        sharedThought.hideMessageToPatientDialog();
    });

    sharedThought.submitMessageBtn.click(function(event) {
        event.preventDefault();
        if (sharedThought.sendMessageToPatient()) {
            sharedThought.hideMessageToPatientDialog();
        }
    });

    sharedThought.closeMessageBtn.click(function(event) {
        event.preventDefault();
        sharedThought.hideMessageToPatientDialog();
        sharedThought.messageForm.show();
        sharedThought.messageSuccessDiv.hide();
    });

    sharedThought.responderAddMessage.empty();
    sharedThought.responderMessageTextarea.empty();
};

/**
 * Show message to patient dialog
 */
sharedThought.showMessageToPatientDialog = function() {

    // set subtitle to different text if anonymous
    if (sharedThought.isAnonymous == "true") {
        sharedThought.messageSubtitle.text("The patient will be kept anonymous and receive your message as normal, you will not be able to see the patient's details when looking at the conversation.");
    } else {
        sharedThought.messageSubtitle.text("The patient will receive your message as a normal message.");
    }

    // fade background and show dialog
    sharedThought.messageBackground.css('height',$(document).height());
    sharedThought.messageBackground.css('width',$(window).width());
    sharedThought.messageBackground.show();
    sharedThought.messageModal.show();
}

/**
 * Get user data from message form, check for errors, send to server
 */
sharedThought.sendMessageToPatient = function() {

    sharedThought.clearErrorMessages();

    var messageData = {};
    messageData.sharedThoughtId = sharedThought.id;
    messageData.subject = $('#js-message-subject').val();
    messageData.message = $('#js-message-message').val();

    var errors = false;

    if (!messageData.subject.length > 0) {
        $('#js-message-subject-error').text("Please enter a subject");
        $('#js-message-subject-error').show();
        errors = true;
    }

    if (!messageData.message.length > 0) {
        $('#js-message-message-error').text("Please enter a message");
        $('#js-message-message-error').show();
        errors = true;
    }

    if (errors) {
        return false;
    } else {

        sharedThought.messageForm.hide();
        sharedThought.messageLoadingDiv.show();

        $.ajax({
            type: "POST",
            url: "/web/sharingThoughts/sendMessageToPatient",
            data: messageData,
            success: function(data) {
                sharedThought.messageLoadingDiv.hide();
                sharedThought.messageSuccessDiv.show();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                $('#js-message-error-found').text("Sorry, there was an error sending your message to the patient");
                $('#js-message-error-found').show();
                sharedThought.messageLoadingDiv.hide();
                sharedThought.messageForm.show();
            },
            dataType: 'json'
        });
    }
}

/**
 * Hide message dialog, clear the form of user entered data
 */
sharedThought.hideMessageToPatientDialog = function() {
    // hide dialog
    sharedThought.messageBackground.hide();
    sharedThought.messageModal.hide();

    // clear form
    $('#js-message-subject').val("");
    $('#js-message-message').val("");
    sharedThought.clearErrorMessages();
}

/**
 * clear error messages, hide alerts
 */
sharedThought.clearErrorMessages = function() {
    $('#js-message-subject-error').text("");
    $('#js-message-subject-error').hide();
    $('#js-message-message-error').text("");
    $('#js-message-message-error').hide();
    $('#js-message-error-found').text("");
    $('#js-message-error-found').hide();
}

/**
 * Add selected staff member to list of responders, in db and on ui table
 */
sharedThought.addResponder = function() {
    sharedThought.responderAddMessage.empty();
    sharedThought.responderAddBtn.attr('disabled','disabled');

    var responderName = sharedThought.responderSelect.find("option:selected").text();
    var responderId = sharedThought.responderSelect.val();

    var responderData = {};
    responderData.sharedThoughtId = sharedThought.id;
    responderData.responderId = responderId;

    $.ajax({
        type: "POST",
        url: "/web/sharingThoughts/addResponder",
        data: responderData,
        success: function() {
            sharedThought.getOtherResponders();
            sharedThought.responderAddMessage.text("Added responder");
            sharedThought.responderTr.before("<tr><td>" + responderName + "</td><td class='tdUserSharedThought'><a class='btn removeUserSharedThought' data-userId='" + responderId + "'>Remove</a></td></tr>");
        },
        error: function() {
            sharedThought.responderAddMessage.text("There was an error adding a responder");
        },
        dataType: 'json'
    });
}

/**
 * Add message to sharing thought conversation
 */
sharedThought.addMessage = function() {

    sharedThought.responderMessageBtn.attr('disabled','disabled');

    var responderData = {};
    responderData.sharedThoughtId = sharedThought.id;
    responderData.message = sharedThought.responderMessageTextarea.val();

    $.ajax({
        type: "POST",
        url: "/web/sharingThoughts/addMessage",
        data: responderData,
        success: function() {
            sharedThought.responderMessageTrNoComments.remove();
            sharedThought.responderMessageBtn.parent().parent().before("<tr><td colspan='2'><span class='sharingThoughtsMessageAuthor'>" + sharedThought.userFullName + ": </span><span class='sharingThoughtsMessageContent'>" + responderData.message + "</span><span class='pull-right sharingThoughtsMessageDate'>added just now</span></td></tr>")
            sharedThought.responderMessageTextarea.val("");
        },
        error: function() {
            alert("There was an error adding a message");
        },
        dataType: 'json'
    });

    sharedThought.responderMessageBtn.removeAttr('disabled');
}

/**
 * Remove selected staff member from list of responders, in db and on ui table
 */
sharedThought.removeResponder = function(removeButton) {
    sharedThought.responderAddMessage.empty();
    removeButton.attr('disabled','disabled');

    var responderData = {};
    responderData.sharedThoughtId = sharedThought.id;
    responderData.responderId = removeButton.attr("data-userId");

    $.ajax({
        type: "POST",
        url: "/web/sharingThoughts/removeResponder",
        data: responderData,
        success: function() {
            sharedThought.getOtherResponders();
            sharedThought.responderAddMessage.text("Removed responder");
            removeButton.parent().parent().remove();
        },
        error: function() {
            sharedThought.responderAddMessage.text("There was an error removing a responder");
        },
        dataType: 'json'
    });

    removeButton.removeAttr('disabled');
}

/**
 * close/open shared thought and return to list (avoids logic/redraw of message input)
 */
sharedThought.openCloseSharedThought = function() {

    var confirmMsg = "Are you sure you want to close this Shared Thought and stop staff adding to the conversation?";
    if (sharedThought.closeBtn.val().contains("Open")) {
        confirmMsg = "Are you sure you want to open this Shared Thought and allow staff to add to the conversation?"
    }

    if(confirm(confirmMsg)) {
        var data = {};
        data.sharedThoughtId = sharedThought.id;

        $.ajax({
            type: "POST",
            url: "/web/sharingThoughts/openCloseSharedThought",
            data: data,
            success: function() {
                window.location = "/renal/control/sharingThoughts.do";
            },
            error: function() {
                alert("There was an error changing the status of this Shared Thought");
            },
            dataType: 'json'
        });
    }
}

// add in a dom ready to fire init
$(function() {
    sharedThought.init();
});
