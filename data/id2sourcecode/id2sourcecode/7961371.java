    public static void main(String[] args) {
        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        Mixer mixer = null;
        Line[] lines = null;
        for (int i = 0; i < mixers.length; i++) {
            System.out.println(mixers[i].toString());
            System.out.println(mixers[i].getDescription());
            mixer = AudioSystem.getMixer(mixers[i]);
            System.out.println(mixer.getClass().getName());
            linetest(mixer, 2);
            System.out.println("  Available source lines (input):");
            lines = mixer.getSourceLines();
            for (int j = 0; j < lines.length; j++) {
                linetest(lines[i], 4);
            }
            System.out.println("  Available target lines (output):");
            lines = mixer.getTargetLines();
            for (int j = 0; j < lines.length; j++) {
                linetest(lines[i], 4);
            }
        }
        try {
            URL baseurl = new File("C:/data/media/sound/own/").toURL();
            URL snd1 = new URL(baseurl, "burn.wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(snd1);
            AudioFormat format = stream.getFormat();
            if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                stream = AudioSystem.getAudioInputStream(format, stream);
            }
            SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(stream.getFormat());
            line.start();
            int numRead = 0;
            byte[] buf = new byte[line.getBufferSize()];
            while ((numRead = stream.read(buf, 0, buf.length)) >= 0) {
                int offset = 0;
                while (offset < numRead) {
                    offset += line.write(buf, offset, numRead - offset);
                }
            }
            line.drain();
            line.stop();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } catch (LineUnavailableException e) {
        } catch (UnsupportedAudioFileException e) {
        }
    }
