        public void playAudioFile(File soundFile) {
            AudioInputStream ain;
            AudioFileFormat aFormat;
            int bufferSize = 40960;
            try {
                aFormat = AudioSystem.getAudioFileFormat(soundFile);
                ain = AudioSystem.getAudioInputStream(soundFile);
                AudioFormat format = aFormat.getFormat();
                DataLine.Info targetInfo = new DataLine.Info(SourceDataLine.class, format, 40960);
                if (!AudioSystem.isLineSupported(targetInfo)) {
                    JOptionPane.showMessageDialog(null, BlueSystem.getString("soundfile.player.error.lineUnsupported"));
                    return;
                }
                SourceDataLine b = (SourceDataLine) AudioSystem.getLine(targetInfo);
                int read;
                byte[] buffer = new byte[bufferSize];
                b.open(format, bufferSize);
                b.start();
                slider.setMinimum(0);
                slider.setMaximum(aFormat.getByteLength());
                slider.setValue(0);
                while ((read = ain.read(buffer)) != -1) {
                    if (stopPlaying) {
                        break;
                    }
                    b.write(buffer, 0, read);
                    int bytesRead = slider.getValue() + read;
                    slider.setValue(bytesRead);
                    updateCurrentTime(bytesRead);
                }
                if (!stopPlaying) {
                    b.drain();
                }
                b.stop();
                b.close();
            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(null, iae.getLocalizedMessage());
            } catch (LineUnavailableException lue) {
                JOptionPane.showMessageDialog(null, lue.getLocalizedMessage());
            } catch (FileNotFoundException fe) {
                JOptionPane.showMessageDialog(null, BlueSystem.getString("message.file.notFound"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            slider.setValue(0);
        }
