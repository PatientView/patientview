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

package org.patientview.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;

/**
 * Use reflection and Jsoup to clean and reset the entity's input data.
 */
@Component(value = "xssUtils")
public final class XssUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(XssUtils.class);

    public <T> void cleanObjectForXss(T object) {
        Method[] methods = object.getClass().getMethods();

        if (methods != null && methods.length > 0) {
            Map<String, Method> methodNameToMethod = new HashMap<String, Method>();

            // add all the methods to the map
            for (Method method : methods) {
                methodNameToMethod.put(method.getName(), method);
            }

            // find the string setters
            for (Method method : methods) {
                if (isStringSetter(method)) {
                    // check if there is a matching getter
                    String getterName = method.getName().replace("set", "get");
                    Method getter = methodNameToMethod.get(getterName);

                    if (getter != null && isStringGetter(getter)) {

                        try {
                            // get the data, clean it and invoke the setter with the new data
                            String dirtyString = (String) getter.invoke(object);
                            if (dirtyString != null) {
                                String cleanString = Jsoup.clean(dirtyString, "", Whitelist.none(),
                                        new Document.OutputSettings().prettyPrint(false));


                                // set the clean string
                                method.invoke(object, cleanString);
                            }
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage());
                            LOGGER.debug(e.getMessage(), e);
                        }
                    }
                }
            }
        }
    }

    private boolean isStringSetter(Method method) {
        return method.getName().startsWith("set") && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0].equals(String.class);
    }

    private boolean isStringGetter(Method method) {
        return method.getName().startsWith("get") && method.getParameterTypes().length == 0
                && method.getReturnType().equals(String.class);
    }

    public static String encodeForHTML(String strSrc, String[] strReplace) {
        strSrc = ESAPI.encoder().encodeForHTML(strSrc);
        for (String replace : strReplace) {
            strSrc = strSrc.replace(replace, "<br/>");
        }

        return strSrc;
    }

    /**
     * Safely converts string containing shortcode (currently limited set) to HTML compatible string
     * @param sourceString Input string containing shortcode
     * @return String suitable for use in HTML
     */
    public static String convertShortCodeToHTML(String sourceString) {
        // safely encode html
        sourceString = ESAPI.encoder().encodeForHTML(sourceString);

        // ordered set of replacements
        TreeMap<String, String> shortCodes = new TreeMap<String, String>();
        shortCodes.put("&", "@");
        shortCodes.put("@#xd;@#xa;", "<br/>");
        shortCodes.put("@#x5b;link@#x5d;", "<a href=\"");
        shortCodes.put("@#x5b;@#x2f;link@#x5d;", "\">");
        shortCodes.put("@#x5b;linktext@#x5d;", "");
        shortCodes.put("@#x5b;@#x2f;linktext@#x5d;", "</a>");
        shortCodes.put("@#x5b;bold@#x5d;", "<strong>");
        shortCodes.put("@#x5b;@#x2f;bold@#x5d;", "</strong>");
        shortCodes.put("@#x3a;@#x2f;@#x2f;", "://");

        // run through replacements in order
        for (Map.Entry<String, String> shortCode : shortCodes.entrySet()) {
            sourceString = sourceString.replace(shortCode.getKey(), shortCode.getValue());
        }

        return sourceString;
    }

}
