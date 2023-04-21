    private void initOgg(String file) {
        try {
            {
                PhysicalOggStream os = null;
                AudioFormat audioFormat = null;
                ByteArrayOutputStream baos = new ByteArrayOutputStream(10240);
                URL url = CIO.getResourceURL(file);
                if (url != null) {
                    os = new UncachedUrlStream(url);
                } else {
                    File of = new File(file);
                    if (of.exists()) {
                        os = new FileStream(new RandomAccessFile(of, "r"));
                    }
                }
                if (os == null) {
                    System.err.println("can not create sound : " + file);
                }
                for (Object los : os.getLogicalStreams()) {
                    LogicalOggStream loStream = (LogicalOggStream) los;
                    VorbisStream vStream = new VorbisStream(loStream);
                    IdentificationHeader vStreamHdr = vStream.getIdentificationHeader();
                    audioFormat = new AudioFormat((float) vStreamHdr.getSampleRate(), 16, vStreamHdr.getChannels(), true, true);
                    try {
                        byte t = 0;
                        byte[] data = new byte[2];
                        while (true) {
                            vStream.readPcm(data, 0, data.length);
                            t = data[0];
                            data[0] = data[1];
                            data[1] = t;
                            baos.write(data);
                        }
                    } catch (EndOfOggStreamException e) {
                    }
                    vStream.close();
                    loStream.close();
                }
                os.close();
                if (audioFormat.getChannels() == 1) format[0] = AL.AL_FORMAT_MONO16; else format[0] = AL.AL_FORMAT_STEREO16;
                data[0] = ByteBuffer.wrap(baos.toByteArray());
                size[0] = baos.size();
                freq[0] = (int) audioFormat.getFrameRate();
                System.out.println(audioFormat + " size = " + baos.size() + " : " + file);
            }
            {
                int[] buffer = new int[1];
                al.alGenBuffers(1, buffer, 0);
                if (al.alGetError() != AL.AL_NO_ERROR) {
                    System.err.println("Error generating OpenAL buffers : " + file);
                    return;
                }
                al.alBufferData(buffer[0], format[0], data[0], size[0], freq[0]);
                if (al.alGetError() != AL.AL_NO_ERROR) {
                    System.err.println("Error bind WAV file : " + file);
                    return;
                }
                this.buffer = buffer;
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
