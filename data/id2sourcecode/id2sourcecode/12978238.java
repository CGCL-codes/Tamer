    private void playStream(byte[] buf) {
        System.out.println("buf.length: " + buf.length);
        AudioInputStream in = null;
        System.out.println("Got audio stream");
        ByteArrayInputStream bais = null;
        long fileLength = buf.length;
        playing = true;
        try {
            bais = new ByteArrayInputStream(buf);
            in = AudioSystem.getAudioInputStream(bais);
        } catch (Exception ex1) {
            MusicBoxView.showErrorDialog(ex1);
            Bitstream m = new Bitstream(bais);
            long start = m.header_pos();
            fileLength = fileLength - start;
            try {
                m.close();
            } catch (Exception ex) {
                MusicBoxView.showErrorDialog(ex);
            }
            bais = new ByteArrayInputStream(buf);
            bais.skip(start);
            try {
                in = AudioSystem.getAudioInputStream(bais);
            } catch (UnsupportedAudioFileException ex) {
                System.out.println("UnsupportedAudioFileException");
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.append("IOException");
                ex.printStackTrace();
            }
        }
        AudioInputStream din = null;
        if (in != null) {
            try {
                AudioFormat baseFormat = in.getFormat();
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                din = AudioSystem.getAudioInputStream(decodedFormat, in);
                System.out.println("rawplay called");
                mbv.back.rawplay(decodedFormat, din);
                playing = false;
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (LineUnavailableException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Done playing audio stream");
    }
