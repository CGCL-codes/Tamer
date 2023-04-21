        public void run() {
            try {
                PrintStream out = System.out;
                if (out != null) out.println("---  Start : " + file.getName() + "  ---");
                AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
                if (out != null) out.println("Audio Type : " + aff.getType());
                AudioInputStream in = AudioSystem.getAudioInputStream(file);
                AudioInputStream din = null;
                if (in != null) {
                    AudioFormat baseFormat = in.getFormat();
                    if (out != null) out.println("Source Format : " + baseFormat.toString());
                    AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                    if (out != null) out.println("Target Format : " + decodedFormat.toString());
                    Map proper = aff.properties();
                    out.println("Meta Data: " + proper.toString());
                    din = AudioSystem.getAudioInputStream(decodedFormat, in);
                    rawplay(decodedFormat, din);
                    in.close();
                    if (out != null) out.println("---  Stop : " + file.getName() + "  ---");
                    jButtonPlay.setEnabled(true);
                    jButtonPause.setEnabled(false);
                    jButtonStop.setEnabled(false);
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
