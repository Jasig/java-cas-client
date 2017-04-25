/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.client.http.servlet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.mock.web.MockHttpSession;

/**
 * Unit tests for {@link DelegatingClientSession}.
 *
 * @author Carl Harris
 */
public class DelegatingClientSessionTest {

    private MockHttpSession delegate = new MockHttpSession();

    private DelegatingClientSession session = new DelegatingClientSession(delegate);

    @Test(expected = RuntimeException.class)
    public void testDelegateRequired() throws Exception {
        new DelegatingClientSession(null);
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(session.getId(), delegate.getId());
    }

    @Test
    public void testInvalidate() throws Exception {
        session.invalidate();
        assertTrue(delegate.isInvalid());
    }

}
