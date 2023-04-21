    private void playAudio() {
        try {
            if (new File(textField.getText() + ".csv").exists()) {
                System.out.println("Skipping file because csv exists: " + textField.getText() + ".csv");
                System.exit(1);
            }
            audioInputStream = AudioSystem.getAudioInputStream(new File(textField.getText()));
            AudioFormat baseFormat = audioInputStream.getFormat();
            System.out.print(textField.getText() + ": ");
            System.out.println(baseFormat);
            if (textField.getText().endsWith(".mp3") || textField.getText().endsWith(".ogg") || textField.getText().endsWith(".flac")) {
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                AudioInputStream dis = AudioSystem.getAudioInputStream(decodedFormat, audioInputStream);
                audioInputStream = dis;
                audioFormat = dis.getFormat();
                System.out.println("Converted to: " + audioFormat);
            } else {
                audioFormat = baseFormat;
            }
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            new PlayThread().start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
