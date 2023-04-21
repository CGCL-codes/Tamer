    public static void main(String[] args) throws IOException {
        SyncState oy;
        StreamState os;
        Page og;
        Packet op;
        Info vi;
        Comment vc;
        DspState vd;
        Block vb;
        oy = new SyncState();
        os = new StreamState();
        og = new Page();
        op = new Packet();
        vi = new Info();
        vc = new Comment();
        vd = new DspState();
        vb = new Block();
        int[] convbuffer = new int[convsize];
        byte[] buffer;
        int bytes;
        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);
        buffer = new byte[4096];
        oy.init();
        while (true) {
            boolean eos = false;
            int i;
            bytes = inputStream.read(buffer);
            oy.write(buffer, bytes);
            if (oy.pageOut(og) != 1) {
                if (bytes < 4096) {
                    break;
                }
                System.err.print("Input does not appear to be an Ogg bitstream.\n");
                System.exit(1);
            }
            os.init(og.getSerialNo());
            vi.init();
            vc.init();
            if (os.pageIn(og) < 0) {
                System.err.print("Error reading first page of Ogg bitstream data.\n");
                System.exit(1);
            }
            if (os.packetOut(op) != 1) {
                System.err.print("Error reading initial header packet.\n");
                System.exit(1);
            }
            if (vi.headerIn(vc, op) < 0) {
                System.err.print("This Ogg bitstream does not contain Vorbis audio data.\n");
                System.exit(1);
            }
            i = 0;
            while (i < 2) {
                while (i < 2) {
                    int result = oy.pageOut(og);
                    if (result == 0) {
                        break;
                    }
                    if (result == 1) {
                        os.pageIn(og);
                        while (i < 2) {
                            result = os.packetOut(op);
                            if (result == 0) {
                                break;
                            }
                            if (result < 0) {
                                System.err.print("Corrupt secondary header.  Exiting.\n");
                                System.exit(1);
                            }
                            vi.headerIn(vc, op);
                            i++;
                        }
                    }
                }
                bytes = inputStream.read(buffer);
                if (bytes == 0 && i < 2) {
                    System.err.print("End of file before finding all Vorbis headers!\n");
                    System.exit(1);
                }
                oy.write(buffer, bytes);
            }
            {
                String[] astrComments = vc.getUserComments();
                for (i = 0; i < astrComments.length; i++) {
                    System.err.println(astrComments[i]);
                }
                System.err.print("\nBitstream is " + vi.getChannels() + " channel, " + vi.getRate() + " Hz\n");
                System.err.print("Encoded by: " + vc.getVendor() + "\n\n");
            }
            int nChannels = vi.getChannels();
            convsize = 4096 / nChannels;
            vd.init(vi);
            vb.init(vd);
            while (!eos) {
                while (!eos) {
                    int result = oy.pageOut(og);
                    if (result == 0) {
                        break;
                    }
                    if (result < 0) {
                        System.err.print("Corrupt or missing data in bitstream; continuing...\n");
                    } else {
                        os.pageIn(og);
                        while (true) {
                            result = os.packetOut(op);
                            if (result == 0) {
                                break;
                            }
                            if (result < 0) {
                            } else {
                                float[][] pcm = new float[nChannels][0];
                                int samples;
                                if (vb.synthesis(op) == 0) {
                                    vd.blockIn(vb);
                                }
                                while ((samples = vd.pcmOut(pcm)) > 0) {
                                    int j;
                                    boolean clipflag = false;
                                    int bout = (samples < convsize ? samples : convsize);
                                    for (i = 0; i < nChannels; i++) {
                                        int ptr = i;
                                        for (j = 0; j < bout; j++) {
                                            int val = Math.round(pcm[i][j] * 32767.0F);
                                            if (val > 32767) {
                                                val = 32767;
                                                clipflag = true;
                                            }
                                            if (val < -32768) {
                                                val = -32768;
                                                clipflag = true;
                                            }
                                            convbuffer[ptr] = val;
                                            ptr += nChannels;
                                        }
                                    }
                                    if (clipflag) {
                                        System.err.print("Clipping in frame " + vd.getSequence() + "\n");
                                    }
                                    byte[] abBuffer = new byte[2 * nChannels * bout];
                                    int byteOffset = 0;
                                    boolean bigEndian = false;
                                    for (int nSample = 0; nSample < nChannels * bout; nSample++) {
                                        int sample = convbuffer[nSample];
                                        if (bigEndian) {
                                            abBuffer[byteOffset++] = (byte) (sample >> 8);
                                            abBuffer[byteOffset++] = (byte) (sample & 0xFF);
                                        } else {
                                            abBuffer[byteOffset++] = (byte) (sample & 0xFF);
                                            abBuffer[byteOffset++] = (byte) (sample >> 8);
                                        }
                                    }
                                    outputStream.write(abBuffer);
                                    vd.read(bout);
                                }
                            }
                        }
                        if (og.isEos()) {
                            eos = true;
                        }
                    }
                }
                if (!eos) {
                    bytes = inputStream.read(buffer);
                    oy.write(buffer, bytes);
                    if (bytes == 0) {
                        eos = true;
                    }
                }
            }
            os.clear();
            vb.clear();
            vd.clear();
            vc.clear();
            vi.clear();
        }
        oy.clear();
        System.err.print("Done.\n");
        System.exit(0);
    }
