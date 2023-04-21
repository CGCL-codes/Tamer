    protected SoundData loadFromOgg(String file) {
        String path = getPath(file);
        String t_file = AppState.config.homeDir + "temp/" + file;
        SoundData s_data = new SoundData();
        try {
            FileReader.extractFile(path, t_file);
        } catch (ZipException e) {
            Console.print("Exception: " + e.getMessage());
            Console.print("Couldn't extract sound data!");
            return null;
        } catch (IOException e) {
            Console.print("Exception: " + e.getMessage());
            Console.print("Couldn't extract sound data!");
            return null;
        }
        PhysicalOggStream os = null;
        try {
            os = new FileStream(new RandomAccessFile(t_file, "r"));
        } catch (OggFormatException e) {
            Console.print("Exception: " + e.getMessage());
            Console.print("Couldn't extract sound data!");
            return null;
        } catch (FileNotFoundException e) {
            Console.print("Exception: " + e.getMessage());
            Console.print("Couldn't extract sound data!");
            return null;
        } catch (IOException e) {
            Console.print("Exception: " + e.getMessage());
            Console.print("Couldn't extract sound data!");
            return null;
        }
        LogicalOggStream los = (LogicalOggStream) os.getLogicalStreams().iterator().next();
        if (los.getFormat() != LogicalOggStream.FORMAT_VORBIS) {
            Console.print("File is not in proper ogg vorbis format.");
            return null;
        }
        VorbisStream vs = null;
        try {
            vs = new VorbisStream(los);
        } catch (VorbisFormatException e) {
            Console.print("Exception: " + e.getMessage());
            Console.print("Couldn't extract sound data!");
            return null;
        } catch (IOException e) {
            Console.print("Exception: " + e.getMessage());
            Console.print("Couldn't extract sound data!");
            return null;
        }
        int channels = vs.getIdentificationHeader().getChannels();
        int sample_rate = vs.getIdentificationHeader().getSampleRate();
        Vector<Byte> buffer = new Vector<Byte>();
        byte[] tempbuffer = new byte[65536];
        int len = 0;
        try {
            while (true) {
                int read = vs.readPcm(tempbuffer, 0, tempbuffer.length);
                len += read;
                for (int i = 0; i < read; i += 2) {
                    buffer.add(tempbuffer[i + 1]);
                    buffer.add(tempbuffer[i]);
                }
            }
        } catch (EndOfOggStreamException e) {
        } catch (IOException e) {
            Console.print("Exception: " + e.getMessage());
            Console.print("Couldn't extract sound data!");
            return null;
        } catch (OutOfMemoryError e) {
            Console.print("Exception: " + e.getMessage());
            Console.print("Couldn't extract sound data! File " + file + " is too large.");
            return null;
        }
        try {
            los.close();
            os.close();
            vs.close();
        } catch (IOException e) {
            Console.print("Exception: " + e.getMessage());
            Console.print("Couldn't extract sound data!");
            return null;
        }
        (new File(t_file)).delete();
        s_data.data = new byte[buffer.size()];
        for (int i = 0; i < s_data.data.length; i++) s_data.data[i] = buffer.get(i);
        s_data.size = len;
        s_data.frequency = sample_rate;
        if (channels == 1) s_data.format = SoundFormat.MONO_16; else if (channels == 2) s_data.format = SoundFormat.STEREO_16; else {
            Console.print("Couldn't extract sound data! Probably wrong sound format.");
            return null;
        }
        return s_data;
    }
