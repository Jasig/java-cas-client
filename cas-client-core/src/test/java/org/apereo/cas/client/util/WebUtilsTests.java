/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apereo.cas.client.util;

import org.apereo.cas.client.Protocol;

import jakarta.servlet.http.HttpServletResponse;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Tests for the WebUtils.
 *
 * @author Jerome LELEU
 * @since 4.0.3
 */
public final class WebUtilsTests extends TestCase {

    public void testConstructServiceUrlWithTrailingSlash() {
        final var CONST_MY_URL = "https://www.myserver.com/hello/hithere/";
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.setScheme("https");
        request.setSecure(true);
        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null, "www.myserver.com",
                "service", "ticket", false);

        assertEquals(CONST_MY_URL, constructedUrl);
    }

    public void testConstructServiceUrlWithServerNameContainingPath() {
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.setScheme("https");
        request.setSecure(true);
        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null, "www.my.server.com/app",
                Protocol.CAS3.getServiceParameterName(), Protocol.CAS3.getArtifactParameterName(), false);

        assertEquals("https://www.my.server.com/app/hello/hithere/", constructedUrl);
    }

    public void testConstructServiceUrlWithServerNameContainingPathAndSchema() {
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.setScheme("https");
        request.setSecure(true);
        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null, "https://www.my.server.com/app",
                Protocol.CAS3.getServiceParameterName(), Protocol.CAS3.getArtifactParameterName(), false);

        assertEquals("https://www.my.server.com/app/hello/hithere/", constructedUrl);
    }

    public void testConstructServiceUrlWithParamsCas() {
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.setScheme("https");
        request.setSecure(true);
        request.setQueryString("service=this&ticket=that&custom=custom");

        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null, "www.myserver.com",
                Protocol.CAS3.getServiceParameterName(), Protocol.CAS3.getArtifactParameterName(), false);

        assertEquals("https://www.myserver.com/hello/hithere/?custom=custom", constructedUrl);
    }

    public void testConstructServiceUrlWithParamsCasAndServerNameWithSchema() {
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.setScheme("https");
        request.setSecure(true);
        request.setQueryString("service=this&ticket=that&custom=custom");

        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null, "https://www.myserver.com",
                Protocol.CAS3.getServiceParameterName(), Protocol.CAS3.getArtifactParameterName(), false);

        assertEquals("https://www.myserver.com/hello/hithere/?custom=custom", constructedUrl);
    }


    public void testConstructServiceUrlWithParamsSaml() {
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.setScheme("https");
        request.setSecure(true);
        request.setQueryString("TARGET=this&SAMLart=that&custom=custom");

        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null, "www.myserver.com",
                Protocol.SAML11.getServiceParameterName(), Protocol.SAML11.getArtifactParameterName(), false);

        assertEquals("https://www.myserver.com/hello/hithere/?custom=custom", constructedUrl);
    }

    public void testConstructServiceUrlWithEncodedParamsSaml() {
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.setScheme("https");
        request.setSecure(true);
        request.setQueryString("TARGET%3Dthis%26SAMLart%3Dthat%26custom%3Dcustom");

        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null, "www.myserver.com",
                Protocol.SAML11.getServiceParameterName(), Protocol.SAML11.getArtifactParameterName(), false);

        assertEquals("https://www.myserver.com/hello/hithere/?custom=custom", constructedUrl);
    }

    public void testConstructServiceUrlWithNoServiceParametersPassed() {
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.setScheme("https");
        request.setSecure(true);
        request.setQueryString("TARGET=Test1&service=Test2&custom=custom");

        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null, "www.myserver.com",
                Protocol.SAML11.getArtifactParameterName(), true);

        assertEquals("https://www.myserver.com/hello/hithere/?custom=custom", constructedUrl);
    }

    public void testConstructServiceUrlWithEncodedParams2Saml() {
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.setScheme("https");
        request.setSecure(true);
        request.setQueryString("TARGET%3Dthis%26SAMLart%3Dthat%26custom%3Dcustom%20value%20here%26another%3Dgood");

        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null, "www.myserver.com",
                Protocol.SAML11.getServiceParameterName(), Protocol.SAML11.getArtifactParameterName(), true);

        assertEquals("https://www.myserver.com/hello/hithere/?custom=custom+value+here&another=good", constructedUrl);
    }

    public void testConstructServiceUrlWithoutEncodedParamsSamlAndNoEncoding() {
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.setScheme("https");
        request.setSecure(true);
        request.setQueryString("TARGET=this&SAMLart=that&custom=custom value here&another=good");

        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null, "www.myserver.com",
                Protocol.SAML11.getServiceParameterName(), Protocol.SAML11.getArtifactParameterName(), false);

        assertEquals("https://www.myserver.com/hello/hithere/?custom=custom value here&another=good", constructedUrl);
    }

    public void testConstructServiceUrlWithEncodedParamsSamlAndNoEncoding() {
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.setScheme("https");
        request.setSecure(true);
        request.setQueryString("TARGET=this&SAMLart=that&custom=custom+value+here&another=good");

        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null, "www.myserver.com",
                Protocol.SAML11.getServiceParameterName(), Protocol.SAML11.getArtifactParameterName(), true);

        assertEquals("https://www.myserver.com/hello/hithere/?custom=custom+value+here&another=good", constructedUrl);
    }

    public void testConstructUrlNonStandardPortAndNoScheme() {
        constructUrlNonStandardPortAndNoPortInConfigTest("www.myserver.com");
    }

    public void testConstructUrlNonStandardPortAndScheme() {
        constructUrlNonStandardPortAndNoPortInConfigTest("https://www.myserver.com");
    }

    public void testConstructUrlWithMultipleHostsNoPortsOrProtocol() {
        final var CONST_MY_URL = "https://www.myserver.com/hello/hithere/";
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.addHeader("Host", "www.myserver.com");
        request.setScheme("https");
        request.setSecure(true);
        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null,
                "www.amazon.com www.bestbuy.com www.myserver.com", "service", "ticket", false);
        assertEquals(CONST_MY_URL, constructedUrl);
    }

    public void testConstructURlWithMultipleHostsAndPorts() {
        final var CONST_MY_URL = "https://www.myserver.com/hello/hithere/";
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.addHeader("Host", "www.myserver.com");
        request.setScheme("https");
        request.setSecure(true);
        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null,
                "http://www.amazon.com https://www.bestbuy.com https://www.myserver.com", "service", "ticket", false);
        assertEquals(CONST_MY_URL, constructedUrl);
    }

    public void testUrlEncodeWithQueryParameters() {
        final var request = new MockHttpServletRequest("GET", "/idp/authN/ExtCas");
        request.setQueryString("conversation=e1s1&ticket=ST-1234-123456789-a&entityId=https://test.edu/sp?alias=1234-1234-1234-1234&something=else");
        request.addHeader("Host", "www.myserver.com");
        request.setScheme("https");
        request.setSecure(true);
        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null,
                "https://my.server.com",
                "service", "ticket", false);
        assertEquals("https://my.server.com/idp/authN/ExtCas?conversation=e1s1&entityId=https://test.edu/sp?alias=1234-1234-1234-1234&something=else",
                constructedUrl);
    }

    private static void constructUrlNonStandardPortAndNoPortInConfigTest(final String serverNameList) {
        final var CONST_MY_URL = "https://www.myserver.com:555/hello/hithere/";
        final var request = new MockHttpServletRequest("GET", "/hello/hithere/");
        request.addHeader("Host", "www.myserver.com");
        request.setScheme("https");
        request.setSecure(true);
        request.setServerPort(555);
        final HttpServletResponse response = new MockHttpServletResponse();
        final var constructedUrl = WebUtils.constructServiceUrl(request, response, null,
                serverNameList, "service", "ticket", false);
        assertEquals(CONST_MY_URL, constructedUrl);
    }
}
