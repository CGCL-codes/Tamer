    public int write(AudioInputStream stream, Type fileType, OutputStream out) throws IOException {
        if (fileType != oggtype) throw new IllegalArgumentException("Invalid file type.");
        stream = convertStream(stream);
        if (stream == null) throw new IOException("Invalid stream format.");
        int READ = 1024;
        byte[] readbuffer = new byte[READ * 4 + 44];
        StreamState os = new StreamState();
        Page og = new Page();
        Packet op = new Packet();
        Info vi = new Info();
        Comment vc = new Comment();
        DspState vd = new DspState();
        Block vb = new Block(vd);
        boolean eos = false;
        int ret;
        int i, founddata;
        VorbisEnc ve = new VorbisEnc();
        vi.init();
        AudioFormat format = stream.getFormat();
        ret = ve.initVBR(vi, format.getChannels(), (int) format.getSampleRate(), 0.1f);
        if (ret != 0) System.exit(1);
        vc.init();
        vc.addTag("ENCODER", "encoder_example.c");
        vd.analysisInit(vi);
        vb.blockInit(vd);
        Random rand = new Random();
        os.init(rand.nextInt());
        {
            Packet header = new Packet();
            Packet header_comm = new Packet();
            Packet header_code = new Packet();
            vd.analysisHeaderOut(vc, header, header_comm, header_code);
            os.packetIn(header);
            os.packetIn(header_comm);
            os.packetIn(header_code);
            while (!eos) {
                boolean result = os.flush(og);
                if (!result) break;
                try {
                    out.write(og.header_base, og.header, og.header_len);
                    out.write(og.body_base, og.body, og.body_len);
                } catch (IOException ioe) {
                }
            }
        }
        int pos = 0;
        while (!eos) {
            int bytes = 0;
            int l;
            try {
                bytes = stream.read(readbuffer, 0, READ * 4);
            } catch (IOException ioe) {
            }
            if (bytes == 0) {
                vd.analysisWrote(0);
            } else {
                float[][] buffer = vd.analysisBuffer(READ);
                for (i = 0, l = vd.pcm_current; i < bytes / 4; i++, l++) {
                    buffer[0][l] = ((readbuffer[i * 4 + 1] << 8) | (0x00ff & (int) readbuffer[i * 4])) / 32768.f;
                    buffer[1][l] = ((readbuffer[i * 4 + 3] << 8) | (0x00ff & (int) readbuffer[i * 4 + 2])) / 32768.f;
                }
                vd.analysisWrote(i);
            }
            while (vb.analysisBlockOut()) {
                vb.analysis(null);
                vb.bitrateAddBlock();
                while (vd.bitrateFlushPacket(op)) {
                    os.packetIn(op);
                    while (!eos) {
                        boolean result = os.pageOut(og);
                        if (!result) break;
                        try {
                            out.write(og.header_base, og.header, og.header_len);
                            out.write(og.body_base, og.body, og.body_len);
                        } catch (IOException ioe) {
                        }
                        if (og.eos()) eos = true;
                    }
                }
            }
        }
        os.clear();
        vb.clear();
        vd.clear();
        vc.clear();
        vi.clear();
        System.out.println("Done.\n");
        return 1;
    }
