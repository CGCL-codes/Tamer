    public AudioFileIn(String fileName) {
        this.fileName = fileName;
        try {
            this.file = new File(fileName);
            this.fileFormat = AudioSystem.getAudioFileFormat(this.file);
            this.format = this.fileFormat.getFormat();
            this.bigEndian = this.format.isBigEndian();
            channels = format.getChannels();
            sampleRate = (int) format.getSampleRate();
            this.duration = (long) this.fileFormat.getFrameLength() * this.channels;
            this.sampleSize = (format.getSampleSizeInBits()) / 8;
        } catch (UnsupportedAudioFileException uafe) {
            System.out.println("jMusic AudioFileIn warning: '" + fileName + "' may not be an audio file.");
            System.out.println("Reading it in as raw data...");
            this.audioFileSpecified = false;
            this.channels = 1;
            this.sampleSize = 1;
            this.sampleRate = 0;
        } catch (IOException ioe) {
            System.out.println("jMusic AudioFileIn error: Cannot read the specified file: " + fileName);
            System.out.println("Most likely the file does not exist at this location. Exiting...");
            System.exit(0);
        }
    }
