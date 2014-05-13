package org.patientview.patientview.controller;


import org.patientview.model.LookupValue;
import org.patientview.service.LookupManager;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.List;


/**
 * Created by james@solidstategroup.com on 14/04/2014.
 */
@Controller
@RequestMapping(value = Routes.LOOKUP_CONTROLLER)
public class LookupController {


    @Inject
    private LookupManager lookupManager;

    @RequestMapping(value = Routes.GET_LOOKUP, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<LookupValue> getByComponent(@RequestParam(value = "name") String name) throws Exception {
        return lookupManager.getByComponent(name);
    }


 }
