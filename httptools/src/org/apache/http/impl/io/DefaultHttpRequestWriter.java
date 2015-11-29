/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.impl.io;

import java.io.IOException;

import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineFormatter;
import org.apache.http.message.BasicLineFormatterHC4;

/**
 * HTTP request writer that serializes its output to an instance of {@link SessionOutputBuffer}.
 *
 * @since 4.3
 */
@NotThreadSafe
public class DefaultHttpRequestWriter extends AbstractMessageWriterHC4<HttpRequest> {

    /**
     * Creates an instance of DefaultHttpRequestWriter.
     *
     * @param buffer the session output buffer.
     * @param formatter the line formatter If <code>null</code>
     *   {@link BasicLineFormatterHC4#INSTANCE}
     *   will be used.
     */
    public DefaultHttpRequestWriter(
            final SessionOutputBuffer buffer,
            final LineFormatter formatter) {
        super(buffer, formatter);
    }

    public DefaultHttpRequestWriter(final SessionOutputBuffer buffer) {
        this(buffer, null);
    }

    @Override
    protected void writeHeadLine(final HttpMessage message) throws IOException {
        lineFormatter.formatRequestLine(this.lineBuf, ((HttpRequest) message).getRequestLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }

}