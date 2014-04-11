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
feedback.imageData = null;
feedback.submitButton = $("#js-feedback-submit-btn");
feedback.cancelButton = $("#js-feedback-cancel-btn");

/**
 * Set up buttons
 */
feedback.init = function() {
    $(".feedbackButton").click(function(e) {
        e.preventDefault();
        feedback.showDialog();
    });

    feedback.cancelButton.click(function(e) {
        e.preventDefault();
        feedback.hideDialog();
    });

    feedback.submitButton.click(function(event) {
        event.preventDefault();
        feedback.sendFeedback();
        feedback.hideDialog();
    });
};

/**
 * Fade background, take screenshot with html2canvas, show dialog
 */
feedback.showDialog = function() {
    // fade background
    $('.container').css({opacity:0.2});

    // clone page and take screenshot of cloned page
    $('#screenshot').html($(".container").clone());
    $('#screenshot').find("a.btn-navbar").remove();
    $('#screenshot').find("a.brand").remove();

    $('#screenshot').children(".container").css({opacity:1});
    html2canvas($('#screenshot'), {
        onrendered: function(canvas) {
            $('#dialogScreenshot').html("<img width=300 src='" + canvas.toDataURL("image/png") + "'/>");
            feedback.imageData = canvas.toDataURL("image/png");
            $('#screenshot').html("");
        }
    });

    // todo: get list of recipients
    /*
    $.getJSON('/web/listFeedbackRecipients', function(data) {
    var recipients = $('.js-feedback-recipients');

    $.each(data, function(i, result) {
    recipients.append('<option value=' + result.id + '>' + result.name + '</option>');
    });

    });
    */

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
}

/**
 * Get user data from form, send to server with html2canvas screenshot
 */
feedback.sendFeedback = function() {
    var feedbackData = {};
    feedbackData.imageData = feedback.imageData;
    feedbackData.subject = $('#js-feedback-subject').val();
    feedbackData.message = $('#js-feedback-message').val();
    feedbackData.recipient = $('#js-feedback-recipient :selected').val();
    // todo: send to server
}

// add in a dom ready to fire init
$(function() {
    feedback.init();
});
