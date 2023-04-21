    @Override
    public int write(AudioInputStream origStream, Type fileType, final OutputStream realOutput) throws IOException {
        final int writeCount[] = { 0 };
        OutputStream out = new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                realOutput.write(b);
                writeCount[0]++;
            }
        };
        if (!isFileTypeSupported(fileType)) throw new IllegalArgumentException("File type is not supported");
        AudioFormat audioFormat = origStream.getFormat();
        AudioFormat supportedFormat = new AudioFormat(audioFormat.getSampleRate(), 16, 2, true, false);
        AudioInputStream stream;
        if (audioFormat.matches(supportedFormat)) stream = origStream; else stream = AudioSystem.getAudioInputStream(supportedFormat, origStream);
        vorbisenc encoder;
        ogg_stream_state os;
        ogg_page og;
        ogg_packet op;
        vorbis_info vi;
        vorbis_comment vc;
        vorbis_dsp_state vd;
        vorbis_block vb;
        int READ = 1024;
        byte[] readbuffer = new byte[READ * 4 + 44];
        int page_count = 0;
        int block_count = 0;
        boolean eos = false;
        vi = new vorbis_info();
        encoder = new vorbisenc();
        Float qualityProperty = (Float) audioFormat.getProperty("quality");
        if (qualityProperty == null) qualityProperty = .3f;
        if (!encoder.vorbis_encode_init_vbr(vi, audioFormat.getChannels(), (int) audioFormat.getSampleRate(), qualityProperty)) {
            throw new IOException("Failed to Initialize vorbisenc");
        }
        vc = new vorbis_comment();
        vc.vorbis_comment_add_tag("ENCODER", "Java Vorbis Encoder");
        vd = new vorbis_dsp_state();
        if (!vd.vorbis_analysis_init(vi)) {
            throw new IOException("Failed to Initialize vorbis_dsp_state");
        }
        vb = new vorbis_block(vd);
        java.util.Random generator = new java.util.Random();
        os = new ogg_stream_state(generator.nextInt(256));
        ogg_packet header = new ogg_packet();
        ogg_packet header_comm = new ogg_packet();
        ogg_packet header_code = new ogg_packet();
        vd.vorbis_analysis_headerout(vc, header, header_comm, header_code);
        os.ogg_stream_packetin(header);
        os.ogg_stream_packetin(header_comm);
        os.ogg_stream_packetin(header_code);
        og = new ogg_page();
        op = new ogg_packet();
        while (!eos) {
            if (!os.ogg_stream_flush(og)) break;
            out.write(og.header, 0, og.header_len);
            out.write(og.body, 0, og.body_len);
        }
        while (!eos) {
            int i;
            int bytes = stream.read(readbuffer, 0, READ * 4);
            int break_count = 0;
            if (bytes == 0) {
                vd.vorbis_analysis_wrote(0);
            } else {
                float[][] buffer = vd.vorbis_analysis_buffer(READ);
                for (i = 0; i < bytes / 4; i++) {
                    buffer[0][vd.pcm_current + i] = ((readbuffer[i * 4 + 1] << 8) | (0x00ff & (int) readbuffer[i * 4])) / 32768.f;
                    buffer[1][vd.pcm_current + i] = ((readbuffer[i * 4 + 3] << 8) | (0x00ff & (int) readbuffer[i * 4 + 2])) / 32768.f;
                }
                vd.vorbis_analysis_wrote(i);
            }
            while (vb.vorbis_analysis_blockout(vd)) {
                vb.vorbis_analysis(null);
                vb.vorbis_bitrate_addblock();
                while (vd.vorbis_bitrate_flushpacket(op)) {
                    os.ogg_stream_packetin(op);
                    while (!eos) {
                        if (!os.ogg_stream_pageout(og)) {
                            break_count++;
                            break;
                        }
                        out.write(og.header, 0, og.header_len);
                        out.write(og.body, 0, og.body_len);
                        if (og.ogg_page_eos() > 0) eos = true;
                    }
                }
            }
        }
        return writeCount[0];
    }
