    public void playStream() {
        AudioInputStream in = null;
        System.out.println("Got audio stream");
        ByteArrayInputStream bais = null;
        long fileLength = 0;
        playing = true;
        try {
            fileLength = pis.available();
            in = AudioSystem.getAudioInputStream(pis);
        } catch (Exception ex1) {
            MusicBoxView.showErrorDialog(ex1);
            System.err.println("First AudioSystem.getAudioInputStream method didn't work");
            Bitstream m = new Bitstream(pis);
            long start = m.header_pos();
            fileLength = fileLength - start;
            try {
                m.close();
            } catch (Exception ex) {
                MusicBoxView.showErrorDialog(ex);
            }
            try {
                pis.skip(start);
            } catch (IOException ex) {
                System.out.println("IOException: " + ex.toString());
            }
            try {
                in = AudioSystem.getAudioInputStream(pis);
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
                System.out.println("decodedFormat.toString(): " + decodedFormat.toString());
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
    }
