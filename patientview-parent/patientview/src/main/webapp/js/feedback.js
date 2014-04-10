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
feedback.submitButton = $(".js-feedback-submit-btn");
feedback.cancelButton = $(".js-feedback-cancel-btn");

feedback.init = function() {

    feedback.feedbackModal.hide();

    $(".feedbackButton").click(function(e) {
        e.preventDefault();
        feedback.showDialog();
    });

    feedback.cancelButton.click(function(e) {
        e.preventDefault();
        feedback.hideDialog();
    });
};

feedback.showDialog = function() {

    $('.container').css({opacity:0.2});
    //feedback.feedbackModal.css({opacity:1});
    feedback.feedbackModal.dialog();

    html2canvas($('body'), {
        onrendered: function(canvas) {
            feedback.imageData = canvas.toDataURL("image/png");
        }
    });
    /*
     $.getJSON('/web/listFeedbackRecipients', function(data) {
     var recipients = $('.js-feedback-recipients');

     $.each(data, function(i, result) {
     recipients.append('<option value=' + result.id + '>' + result.name + '</option>');
     });
     */
    var feedbackForm = $('.js-feedback-form');

    feedback.submitButton.click(function(event) {
        event.preventDefault();
        feedback.sendFeedback(feedbackForm);
    });
    // });
}

feedback.hideDialog = function() {

    $('.container').css({opacity:1});
    feedback.feedbackModal.dialog('close');
}

feedback.sendFeedback = function(form) {
    var $form = $(form),
        subject = $form.find('.js-feedback-subject'),
        message = $form.find('.js-feedback-message'),
        recipient = $form.find('.js-feedback-recipient');

    console.log(feedback.imageData);
    $('.container').css({opacity:1});
    feedback.feedbackModal.dialog('close');
}

// add in a dom ready to fire init
$(function() {
    feedback.init();
});

