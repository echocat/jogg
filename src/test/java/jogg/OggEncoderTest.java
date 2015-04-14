/**
 * **************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat JOpus, Copyright (c) 2014 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 * **************************************************************************************
 */

package jogg;

import org.echocat.jogg.OggPacket;
import org.echocat.jogg.OggSyncStateOutput;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by christian.rijke on 14.08.2014.
 */
public class OggEncoderTest {

    Logger LOG = LoggerFactory.getLogger(OggCodecTest.class);

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void encode() throws IOException {
        String source = "bach_48k.wav";
        LOG.info("encoding {}...", source);

        final int numberOfFrames = 2880;

        File tempFile = tempFolder.newFile(source.substring(0, source.length() - 4) + ".ogg");
        try (final DataInputStream in = new DataInputStream(getClass().getResourceAsStream(source));

             final OutputStream out = new FileOutputStream(tempFile);
             final OggSyncStateOutput sso = new OggSyncStateOutput(out)) {

            final byte[] buffer = new byte[numberOfFrames * 2];
            while (in.read(buffer) != -1) {
                OggPacket oggPacket = OggPacket.packetFor(buffer);
                sso.write(oggPacket);
            }
        }

        LOG.info("encoding finished");
        assertThat(tempFile.length(), is(1030616L));
    }
}
