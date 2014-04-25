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
sharedThought.responderSelect = $('#selectOtherSharedThoughtResponders');
sharedThought.responderTr = $('#trOtherSharedThoughtResponders');
sharedThought.responderAddBtn = $('#btnAddOtherSharedThoughtResponder');
sharedThought.responderAddMessage = $('#messageAddOtherSharedThoughtResponder');
sharedThought.id = $('#sharedThoughtId').val();

/**
 * Set up buttons
 */
sharedThought.init = function() {
    sharedThought.responderAddBtn.click(function(event) {
        event.preventDefault();
        sharedThought.addResponder();
    });

    sharedThought.getOtherResponders();
};

/**
 * Get list of responders who are not already attached to this shared thought, add to select
 */
sharedThought.getOtherResponders = function() {
    sharedThought.responderAddMessage.empty();
    $.getJSON('/web/sharingThoughts/getResponders', function(data) {
        sharedThought.responderSelect.empty();
        $.each(data, function(id, name) {
            sharedThought.responderSelect.append("<option value='" + id + "'>" + name + "</option>");
        });
    }).fail(function(error) {
        sharedThought.responderAddMessage.text("There was an error getting additional responders");
        sharedThought.responderAddBtn.attr('disabled','disabled');
    });
}

/**
 * Add selected staff member to list of responders, in db and on ui table
 */
sharedThought.addResponder = function() {

    sharedThought.responderAddMessage.empty();
    sharedThought.responderAddBtn.attr('disabled','disabled');

    var responderData = {};
    responderData.sharedThoughtId = sharedThought.id;
    responderData.responderId = sharedThought.responderSelect.val();

    $.ajax({
        type: "POST",
        url: "/web/sharingThoughts/addResponder",
        data: responderData,
        success: function() {
            sharedThought.responderAddMessage.text("Added responder");
        },
        error: function() {
            sharedThought.responderAddMessage.text("There was an error adding a responder");
        },
        dataType: 'json'
    });

    sharedThought.responderAddBtn.removeAttr('disabled');
}

// add in a dom ready to fire init
$(function() {
    sharedThought.init();
});
