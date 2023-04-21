    public Song(XMLCategory _cat, JPanel patternsPanel, URL _baseURL, boolean xmlDragging, boolean xmlThreeClicks, int maxSounds) {
        nt = new Vector<Thread>();
        cat = _cat;
        s = new Sheet[1];
        currentSheet = 0;
        numberOfSheets = 1;
        panel = patternsPanel;
        baseURL = _baseURL;
        Drum = new byte[16][];
        outputFormat = new AudioFormat[16];
        this.xmlDragging = xmlDragging;
        this.xmlThreeClicks = xmlThreeClicks;
        this.maxSounds = maxSounds;
        for (int x = 0; x < cat.getNumberOfDrums(); x++) {
            ByteArrayOutputStream buffer;
            buffer = new ByteArrayOutputStream();
            try {
                AudioInputStream din = null;
                AudioInputStream in = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(cat.drum[x].getFile())));
                AudioFormat baseFormat = in.getFormat();
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                din = AudioSystem.getAudioInputStream(decodedFormat, in);
                if (buffer != null) {
                    byte[] data = new byte[4096];
                    int nBytesRead;
                    while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
                        try {
                            buffer.write(data, 0, nBytesRead);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                    din.close();
                }
                outputFormat[x] = decodedFormat;
            } catch (Exception e) {
                System.out.println(e);
            }
            Drum[x] = buffer.toByteArray();
            buffer = null;
        }
        s[0] = new Sheet(cat, patternsPanel, _baseURL, outputFormat, xmlDragging, xmlThreeClicks);
        s[0].draw(patternsPanel);
    }
