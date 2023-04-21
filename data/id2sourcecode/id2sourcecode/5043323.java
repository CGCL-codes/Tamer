    public static void main(String[] args) throws Exception {
        Line.Info si = new Line.Info(SourceDataLine.class);
        Line.Info mi = new Line.Info(TargetDataLine.class);
        SourceDataLine sl = (SourceDataLine) AudioSystem.getLine(si);
        TargetDataLine tl = (TargetDataLine) AudioSystem.getLine(mi);
        AudioFormat lf = new AudioFormat(Encoding.PCM_SIGNED, 8000.0f, 16, 1, 2, 8000.0f, false);
        AudioFormat mf = new AudioFormat(Encoding.PCM_SIGNED, 44100.0f, 16, 1, 2, 44100.0f, false);
        AudioFormat hf = new AudioFormat(Encoding.PCM_SIGNED, 44100.0f, 16, 2, 4, 44100.0f, false);
        sl.open(hf);
        tl.open(hf);
        sl.start();
        tl.start();
        AudioInputStream hos = new AudioInputStream(tl);
        AudioInputStream mos = AudioSystem.getAudioInputStream(mf, hos);
        AudioInputStream los = AudioSystem.getAudioInputStream(lf, mos);
        int factor = 1;
        LibSpeex l = LibSpeex.THIS;
        SpeexBits eb = new SpeexBits();
        SpeexBits db = new SpeexBits();
        Pointer m = l.speex_lib_get_mode(LibSpeex.SPEEX_MODEID_NB);
        Pointer e = l.speex_encoder_init(m);
        Pointer d = l.speex_decoder_init(m);
        IntByReference fr = new IntByReference();
        l.speex_encoder_ctl(e, LibSpeex.SPEEX_GET_FRAME_SIZE, fr);
        l.speex_encoder_ctl(e, LibSpeex.SPEEX_SET_QUALITY, new IntByReference(10));
        byte[] ed = new byte[fr.getValue() * 2];
        byte[] dd = new byte[fr.getValue() * 2];
        l.speex_bits_init(eb);
        l.speex_bits_init(db);
        while (true) {
            byte[] buf = new byte[(int) los.getFormat().getSampleRate() * los.getFormat().getChannels() / factor];
            int n = los.read(buf);
            ByteArrayInputStream is = new ByteArrayInputStream(buf, 0, n);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int s = -1;
            while ((n = is.read(ed)) != -1) {
                l.speex_bits_reset(eb);
                l.speex_encode_int(e, ed, eb);
                n = l.speex_bits_write(eb, ed, ed.length);
                if (s == -1) {
                    s = n;
                }
                os.write(ed, 0, n);
            }
            is = new ByteArrayInputStream(os.toByteArray());
            os = new ByteArrayOutputStream();
            byte[] t = new byte[s];
            while ((n = is.read(t)) != -1) {
                l.speex_bits_reset(db);
                l.speex_bits_read_from(db, t, t.length);
                l.speex_decode_int(d, db, dd);
                os.write(dd);
            }
            AudioInputStream lis = new AudioInputStream(new ByteArrayInputStream(os.toByteArray()), lf, AudioSystem.NOT_SPECIFIED);
            AudioInputStream mis = AudioSystem.getAudioInputStream(mf, lis);
            AudioInputStream his = AudioSystem.getAudioInputStream(hf, mis);
            buf = new byte[(int) his.getFormat().getSampleRate() * his.getFormat().getChannels() / factor];
            n = his.read(buf);
            buf[buf.length - 1] = buf[buf.length - 5];
            buf[buf.length - 2] = buf[buf.length - 6];
            buf[buf.length - 3] = buf[buf.length - 7];
            buf[buf.length - 4] = buf[buf.length - 8];
            sl.write(buf, 0, n);
        }
    }
