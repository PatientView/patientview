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

ecr = {};
ecr.ecrOptInButton = $('#ecrOptInButton');
ecr.ecrOptInNowButton = $('#ecrOptInNowButton');
ecr.ecrOptInNotNowButton = $('#ecrOptInNotNowButton');
ecr.ecrOptOutButton = $('#ecrOptOutButton');
ecr.ecrOptOutNowButton = $('#ecrOptOutNowButton');
ecr.ecrOptOutNotNowButton = $('#ecrOptOutNotNowButton');
ecr.ecrOptOutPermanentlyButton = $('#ecrOptOutPermanentlyButton');
ecr.ecrOptedIn = $('#ecrOptedIn');
ecr.ecrOptedOut = $('#ecrOptedOut');
ecr.ecrOptedInOptedOut = $('#ecrOptedInOptedOut');

/**
 * Set up buttons
 */
ecr.init = function() {
    ecr.ecrOptInButton.click(function(event) {
        event.preventDefault();
        ecr.optIn();
    });
    ecr.ecrOptOutButton.click(function(event) {
        event.preventDefault();
        ecr.optOut();
    });
    ecr.ecrOptOutPermanentlyButton.click(function(event) {
        event.preventDefault();
        ecr.optOutPermanently();
    });
    ecr.ecrOptInNowButton.click(function(event) {
        event.preventDefault();
        ecr.optInNow();
    });
    ecr.ecrOptInNotNowButton.click(function(event) {
        event.preventDefault();
        ecr.optInNotNow();
    });
    ecr.ecrOptOutNowButton.click(function(event) {
        event.preventDefault();
        ecr.optOutNow();
    });
    ecr.ecrOptOutNotNowButton.click(function(event) {
        event.preventDefault();
        ecr.optOutNotNow();
    });
};

ecr.optIn = function() {
    $.ajax({
        type: "POST",
        url: "/web/ecr/changeOptInOut",
        data: {"optIn":"true","optOutPermanently":"false"},
        success: function() {
            location.reload();
        },
        error: function() {
            alert("There was an error setting your preferences");
        },
        dataType: 'json'
    });
};

ecr.optOut = function() {
    $.ajax({
        type: "POST",
        url: "/web/ecr/changeOptInOut",
        data: {"optIn":"false","optOutPermanently":"false"},
        success: function() {
            location.reload();
        },
        error: function() {
            alert("There was an error setting your preferences");
        },
        dataType: 'json'
    });
};

ecr.optInNow = function() {
    $.ajax({
        type: "POST",
        url: "/web/ecr/changeOptInOut",
        data: {"optIn":"true","optOutPermanently":"false","optInNotNow":"false"},
        success: function() {
            location.reload();
        },
        error: function() {
            alert("There was an error setting your preferences");
        },
        dataType: 'json'
    });
};

ecr.optInNotNow = function() {
    $.ajax({
        type: "POST",
        url: "/web/ecr/changeOptInOut",
        data: {"optIn":"false","optOutPermanently":"false","optInNotNow":"true"},
        success: function() {
            location.reload();
        },
        error: function() {
            alert("There was an error setting your preferences");
        },
        dataType: 'json'
    });
};

ecr.optOutNotNow = function() {
    $.ajax({
        type: "POST",
        url: "/web/ecr/changeOptInOut",
        data: {"optIn":"true","optOutPermanently":"false","optOutNotNow":"true"},
        success: function() {
            location.reload();
        },
        error: function() {
            alert("There was an error setting your preferences");
        },
        dataType: 'json'
    });
};

ecr.optOutNow = function() {
    $.ajax({
        type: "POST",
        url: "/web/ecr/changeOptInOut",
        data: {"optIn":"false","optOutPermanently":"false","optOutNotNow":"false"},
        success: function() {
            location.reload();
        },
        error: function() {
            alert("There was an error setting your preferences");
        },
        dataType: 'json'
    });
};

ecr.optOutPermanently = function() {
    $.ajax({
        type: "POST",
        url: "/web/ecr/changeOptInOut",
        data: {"optIn":"false","optOutPermanently":"true"},
        success: function() {
            location.reload();
        },
        error: function() {
            alert("There was an error setting your preferences");
        },
        dataType: 'json'
    });
};

// add in a dom ready to fire init
$(function() {
    ecr.init();
});
