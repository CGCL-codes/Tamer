    private int lame_decode_initfile(final RandomAccessFile fd, final MP3Data mp3data, final Enc enc) {
        byte buf[] = new byte[100];
        short pcm_l[] = new short[1152], pcm_r[] = new short[1152];
        boolean freeformat = false;
        if (hip != null) {
            mpg.hip_decode_exit(hip);
        }
        hip = mpg.hip_decode_init();
        int len = 4;
        try {
            fd.readFully(buf, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        if (buf[0] == 'I' && buf[1] == 'D' && buf[2] == '3') {
            if (parse.silent < 10) {
                System.out.println("ID3v2 found. " + "Be aware that the ID3 tag is currently lost when transcoding.");
            }
            len = 6;
            try {
                fd.readFully(buf, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
            buf[2] &= 127;
            buf[3] &= 127;
            buf[4] &= 127;
            buf[5] &= 127;
            len = (((((buf[2] << 7) + buf[3]) << 7) + buf[4]) << 7) + buf[5];
            try {
                fd.skipBytes(len);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
            len = 4;
            try {
                fd.readFully(buf, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
        if (check_aid(buf)) {
            try {
                fd.readFully(buf, 0, 2);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
            int aid_header = (buf[0] & 0xff) + 256 * (buf[1] & 0xff);
            if (parse.silent < 10) {
                System.out.printf("Album ID found.  length=%d \n", aid_header);
            }
            try {
                fd.skipBytes(aid_header - 6);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
            try {
                fd.readFully(buf, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
        len = 4;
        while (!is_syncword_mp123(buf)) {
            int i;
            for (i = 0; i < len - 1; i++) buf[i] = buf[i + 1];
            try {
                fd.readFully(buf, len - 1, 1);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
        }
        if ((buf[2] & 0xf0) == 0) {
            if (parse.silent < 10) {
                System.out.println("Input file is freeformat.");
            }
            freeformat = true;
        }
        int ret = mpg.hip_decode1_headersB(hip, buf, len, pcm_l, pcm_r, mp3data, enc);
        if (-1 == ret) return -1;
        while (!mp3data.header_parsed) {
            try {
                fd.readFully(buf);
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            }
            ret = mpg.hip_decode1_headersB(hip, buf, buf.length, pcm_l, pcm_r, mp3data, enc);
            if (-1 == ret) return -1;
        }
        if (mp3data.bitrate == 0 && !freeformat) {
            if (parse.silent < 10) {
                System.err.println("fail to sync...");
            }
            return lame_decode_initfile(fd, mp3data, enc);
        }
        if (mp3data.totalframes > 0) {
        } else {
            mp3data.nsamp = -1;
        }
        return 0;
    }
