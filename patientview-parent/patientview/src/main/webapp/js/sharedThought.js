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
sharedThought.responderCommentBtn = $('#btnAddComment');
sharedThought.responderAddMessage = $('#messageAddOtherSharedThoughtResponder');
sharedThought.id = $('#sharedThoughtId').val();

/**
 * Set up buttons
 */
sharedThought.init = function() {
    sharedThought.responderAddBtn.click(function(event) {
        event.preventDefault();
        sharedThought.addComment();
    });

    sharedThought.responderCommentBtn.click(function(event) {
        event.preventDefault();
        sharedThought.addResponder();
    });

    $(document.body).on("click", sharedThought.responderRemoveBtn, function(event) {
        event.preventDefault();
        console.log($(sharedThought.responderRemoveBtn));
        sharedThought.removeResponder($(this));
    });

    sharedThought.responderAddMessage.empty();
    sharedThought.getOtherResponders();
};

/**
 * Get list of responders who are not already attached to this shared thought, add to select
 */
sharedThought.getOtherResponders = function() {
    sharedThought.responderSelect.empty();
    sharedThought.responderAddBtn.attr('disabled','disabled');
    sharedThought.responderSelect.attr('disabled','disabled');

    $.ajax({
        type: "POST",
        url: "/web/sharingThoughts/getOtherResponders",
        data: {sharedThoughtId : sharedThought.id},
        success: function(data) {
            $.each(data, function(id, name) {
                sharedThought.responderSelect.append("<option value='" + id + "'>" + name + "</option>");
            });
            sharedThought.responderAddBtn.removeAttr('disabled');
            sharedThought.responderSelect.removeAttr('disabled');
        },
        error: function() {
            sharedThought.responderAddMessage.text("No additional responders available");
        },
        dataType: 'json'
    });
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
 * Add selected staff member to list of responders, in db and on ui table
 */
sharedThought.addResponder = function() {
    sharedThought.responderCommentBtn.attr('disabled','disabled');

    var responderId = sharedThought.responderSelect.val();

    var responderData = {};
    responderData.sharedThoughtId = sharedThought.id;
    responderData.commentText = responderId;

    $.ajax({
        type: "POST",
        url: "/web/sharingThoughts/addComment",
        data: responderData,
        success: function() {
            location.reload();
        },
        error: function() {
            alert("There was an error adding a comment");
        },
        dataType: 'json'
    });

    sharedThought.responderCommentBtn.removeAttr('disabled');

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

// add in a dom ready to fire init
$(function() {
    sharedThought.init();
});
