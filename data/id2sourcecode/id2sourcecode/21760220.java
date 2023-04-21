        public void run() {
            byte tempBuffer[] = new byte[EXTERNAL_BUFFER_SIZE];
            try {
                sourceDataLine.start();
                int readBytes = 0;
                while ((readBytes != -1) && !stopPlayback) {
                    readBytes = audioInputStream.read(tempBuffer, 0, tempBuffer.length);
                    if (readBytes > 0) sourceDataLine.write(tempBuffer, 0, readBytes);
                }
                sourceDataLine.drain();
                sourceDataLine.close();
                stopSoundFile();
            } catch (Exception e) {
                stopSoundFile();
                JOptionPane.showMessageDialog(null, TLanguage.getString("TSoundChooser.OPEN_FILE_ERROR"), TLanguage.getString("ERROR") + "!", JOptionPane.ERROR_MESSAGE);
            }
        }
