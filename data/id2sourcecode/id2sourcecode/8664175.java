    DataLine initDataLine(AudioInputStream ais) {
        if (debugFlag) debugPrintln("JSClip: initDataLine(" + ais + ")");
        try {
            if (debugFlag) debugPrintln("JSClip: loadSample - try getting new line ");
            audioFormat = ais.getFormat();
            if ((audioFormat.getEncoding() == AudioFormat.Encoding.ULAW) || (audioFormat.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), audioFormat.getSampleSizeInBits() * 2, audioFormat.getChannels(), audioFormat.getFrameSize() * 2, audioFormat.getFrameRate(), true);
                ais = AudioSystem.getAudioInputStream(tmp, ais);
                audioFormat = tmp;
            }
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            line = (Clip) AudioSystem.getLine(info);
            if (debugFlag) debugPrintln("JSClip: open sound Clip");
            line.open(ais);
        } catch (Exception e) {
            if (debugFlag) {
                debugPrint("JSClip: Internal Error loadSample ");
                debugPrintln("get stream failed");
            }
            e.printStackTrace();
            return null;
        }
        return (DataLine) line;
    }
