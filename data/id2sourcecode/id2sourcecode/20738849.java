    public ReadSound(String filename) throws Exception {
        File soundFile = new File(filename);
        audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        audioFormat = audioInputStream.getFormat();
        lenghtFile = soundFile.length();
        channels = audioFormat.getChannels();
        this.quantPacket = (int) lenghtFile / (this.channels * PACKET_SIZE);
        this.channel = choseChannel(audioFormat.getChannels());
        this.rest = (int) (lenghtFile - (((int) (this.quantPacket)) * PACKET_SIZE * this.channels));
        bufferFile = new byte[(int) (lenghtFile / this.channels)];
        abData = new byte[channels * PACKET_SIZE];
    }
