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

feedback = {};
feedback.feedbackModal = $('#feedbackModal');
feedback.feedbackForm = $('#js-feedback-form');
feedback.feedbackSuccessDiv = $('#js-feedback-success');
feedback.feedbackLoadingDiv = $('#js-feedback-loading');
feedback.imageData = null;
feedback.submitButton = $('#js-feedback-submit-btn');
feedback.cancelButton = $('#js-feedback-cancel-btn');
feedback.closeButton = $('#js-feedback-close-btn');
feedback.showScreenshot = false;

/**
 * Set up buttons
 */
feedback.init = function() {
    $('.feedbackButton').click(function(event) {
        event.preventDefault();
        feedback.showDialog();
    });

    feedback.cancelButton.click(function(event) {
        event.preventDefault();
        feedback.hideDialog();
    });

    feedback.submitButton.click(function(event) {
        event.preventDefault();
        if (feedback.sendFeedback()) {
            feedback.hideDialog();
        }
    });

    feedback.closeButton.click(function(event) {
        event.preventDefault();
        feedback.hideDialog();
        feedback.feedbackForm.show();
        feedback.feedbackSuccessDiv.hide();
    });
};

/**
 * Fade background, take screenshot with html2canvas, show dialog
 */
feedback.showDialog = function() {

    // hide recipients error if present
    $('#js-feedback-recipient-error').hide();

    // fade background
    $('.container').css({opacity:0.2});

    // clone page and take screenshot of cloned page
    $('#screenshot').html($('.container').clone());
    $('#screenshot').find('a.btn-navbar').remove();
    $('#screenshot').find('a.brand').remove();

    $('#screenshot').children('.container').css({opacity:1});
    html2canvas($('#screenshot'), {
        onrendered: function(canvas) {
            if (feedback.showScreenshot) {
                $('#dialogScreenshot').html("<img width=300 src='" + canvas.toDataURL("image/png") + "'/>");
            }
            feedback.imageData = canvas.toDataURL("image/png");
            $('#screenshot').html("");
        }
    });

    $.getJSON('/web/feedback/getFeedbackRecipients', function(data) {
        var recipients = $('#js-feedback-recipient');
        recipients.empty();
        recipients.append("<option value='-1' selected='selected'>-- Select a recipient --</option>");
        $.each(data, function(id, name) {
            recipients.append("<option value='" + id + "'>" + name + "</option>");
        });
    }).fail(function(error) {

    });

    // show dialog
    feedback.feedbackModal.show();
}

/**
 * Hide dialog, clear the form of user entered data
 */
feedback.hideDialog = function() {
    // hide dialog
    $('.container').css({opacity:1});
    feedback.feedbackModal.hide();

    // clear form
    $('#js-feedback-subject').val("");
    $('#js-feedback-message').val("");
    $("#js-feedback-recipient").val($("#js-feedback-recipient option:first").val());
    feedback.clearErrorMessages();
}

/**
 * clear error messages, hide alerts
 */
feedback.clearErrorMessages = function() {
    $("#js-feedback-recipient-error").text("");
    $("#js-feedback-recipient-error").hide();
    $('#js-feedback-subject-error').text("");
    $('#js-feedback-subject-error').hide();
    $('#js-feedback-message-error').text("");
    $('#js-feedback-message-error').hide();
    $('#js-feedback-error-found').text("");
    $('#js-feedback-error-found').hide();
}

/**
 * Get user data from form, check for errors, send to server with html2canvas screenshot
 */
feedback.sendFeedback = function() {

    feedback.clearErrorMessages();

    var feedbackData = {};
    feedbackData.imageData = feedback.imageData;
    feedbackData.recipient = $('#js-feedback-recipient :selected').val();
    feedbackData.subject = $('#js-feedback-subject').val();
    feedbackData.message = $('#js-feedback-message').val();

    var errors = false;

    if (feedbackData.recipient == "-1") {
        $('#js-feedback-recipient-error').text("Please choose a recipient");
        $('#js-feedback-recipient-error').show();
        errors = true;
    }

    if (!feedbackData.subject.length > 0) {
        $('#js-feedback-subject-error').text("Please enter a subject");
        $('#js-feedback-subject-error').show();
        errors = true;
    }

    if (!feedbackData.message.length > 0) {
        $('#js-feedback-message-error').text("Please enter a message");
        $('#js-feedback-message-error').show();
        errors = true;
    }

    if (errors) {
        return false;
    } else {

        feedback.feedbackForm.hide();
        feedback.feedbackLoadingDiv.show();

        $.ajax({
            type: "POST",
            url: "/web/feedback/submitFeedback",
            data: feedbackData,
            success: function(data) {
                feedback.feedbackLoadingDiv.hide();
                feedback.feedbackSuccessDiv.show();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                $('#js-feedback-error-found').text("Sorry, there was an error sending your feedback");
                $('#js-feedback-error-found').show();
                feedback.feedbackLoadingDiv.hide();
                feedback.feedbackForm.show();
            },
            dataType: 'json'
        });
    }
}

// add in a dom ready to fire init
$(function() {
    feedback.init();
});
