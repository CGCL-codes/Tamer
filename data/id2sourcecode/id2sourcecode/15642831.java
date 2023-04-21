    public synchronized void run() {
        if (lineDrain == null) System.gc();
        long bytesPerSecond = 44100 * 4;
        if (mp3Stream.mp3 == null) {
            if (mainFrame.library.attributes.libraryType == 'P' && state != 'S') {
                state = 'N';
                nextSong.increment = 1;
                javax.swing.SwingUtilities.invokeLater(nextSong);
            } else state = 'S';
            return;
        }
        Thread waitFor = null;
        if (!mainFrame.continuousMenuItem.isSelected() || useSameLine) {
            waitFor = lineDrain;
            if (waitFor != null) {
                synchronized (lineSync) {
                    waitFor.interrupt();
                }
                try {
                    waitFor.join();
                } catch (InterruptedException ex) {
                }
            }
        }
        announce = null;
        if (state == 'Q') {
            state = 'P';
            if (fullAnnouncement.length() > 0) {
                try {
                    announce = new Announce(fullAnnouncement.toString());
                    announce.start();
                    if (!mainFrame.continuousMenuItem.isSelected() && state == 'P') announce.join();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        fullAnnouncement = new StringBuffer();
        AudioFormat audioFormat = null;
        try {
            if (state != 'P') return;
            if (pluginAudioFileReader == null && !pluginTried) {
                try {
                    pluginTried = true;
                    Class codecClass = Class.forName("com.sun.media.codec.audio.mp3.JS_MP3FileReader");
                    Class[] nullClasses = new Class[0];
                    Constructor constructor = codecClass.getConstructor(nullClasses);
                    Object[] initargs = new Object[0];
                    pluginAudioFileReader = (AudioFileReader) constructor.newInstance(initargs);
                } catch (ClassNotFoundException cnf) {
                } catch (Exception ex) {
                    ex.printStackTrace();
                    pluginAudioFileReader = null;
                }
            }
            AudioInputStream audioInputStream;
            if (pluginAudioFileReader != null) audioInputStream = pluginAudioFileReader.getAudioInputStream(mp3Stream); else audioInputStream = AudioSystem.getAudioInputStream(mp3Stream);
            audioFormat = audioInputStream.getFormat();
            bytesPerSecond = (long) audioFormat.getSampleRate() * audioFormat.getChannels() * (nSampleSizeInBits / 8);
            playingTime = mp3Stream.mp3.getPlayingTime();
            playingTimeDisplay = formatTime(playingTime);
            startTime = playingTime * startPosition / 10000;
            nInternalBufferSize = (int) bytesPerSecond * bufferSecs;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, nInternalBufferSize);
            boolean bIsSupportedDirectly = AudioSystem.isLineSupported(info);
            if (!bIsSupportedDirectly) {
                AudioFormat sourceFormat = audioFormat;
                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), nSampleSizeInBits, sourceFormat.getChannels(), sourceFormat.getChannels() * (nSampleSizeInBits / 8), sourceFormat.getSampleRate(), false);
                audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
                audioFormat = audioInputStream.getFormat();
            }
            line = null;
            waitFor = null;
            synchronized (lineSync) {
                if (lineDrain != null) {
                    if (lineDrain.audioFormat.matches(audioFormat)) {
                        line = lineDrain.line;
                        lineDrain.line = null;
                    } else waitFor = lineDrain;
                }
            }
            if (waitFor != null) {
                synchronized (lineSync) {
                    waitFor.interrupt();
                }
                waitFor.join();
            }
            strMixerName = Jampal.initialProperties.getProperty("mixer-name");
            if (line == null) {
                line = getSourceDataLine(strMixerName, audioFormat, nInternalBufferSize);
            }
            if (!line.isRunning()) {
                line.start();
                FloatControl volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                String gainstr = Jampal.initialProperties.getProperty("mixer-gain-percent");
                float gain = 0;
                if (gainstr != null) {
                    try {
                        gain = Float.parseFloat(gainstr);
                        gain = (float) 10.0 * (float) Math.log10((double) gain * 0.01);
                    } catch (NumberFormatException ex) {
                        gain = 0;
                        System.err.println("Invalid gain value: " + gainstr + ", set to 0");
                    }
                    if (gain > volumeControl.getMaximum()) {
                        gain = volumeControl.getMaximum();
                        System.err.println("Gain value: " + gainstr + " too high, set to Maximum value: " + volumeControl.getMaximum());
                    }
                    if (gain < volumeControl.getMinimum()) {
                        gain = volumeControl.getMinimum();
                        System.err.println("Gain value: " + gainstr + " too low, set to Minimum value: " + volumeControl.getMinimum());
                    }
                }
                volumeControl.setValue(gain);
            }
            playedMicrosAtStart = line.getMicrosecondPosition();
            int nBytesRead = 0;
            prevSeconds = -1;
            mainFrame.timer.addActionListener(updateSlider);
            byte[] abData;
            abData = new byte[4608];
            byte[] abData2 = new byte[4608];
            int nBytesRead2;
            nBytesRead = audioInputStream.read(abData, 0, abData.length);
            nBytesRead = audioInputStream.read(abData, 0, abData.length);
            while (nBytesRead != -1 && state == 'P') {
                int nBytesWritten = 0;
                nBytesRead2 = audioInputStream.read(abData2, 0, abData2.length);
                if (nBytesRead2 == -1) nBytesRead = -1;
                if (nBytesRead >= 0) nBytesWritten = line.write(abData, 0, nBytesRead);
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead == -1) nBytesRead2 = -1;
                if (nBytesRead2 >= 0) nBytesWritten = line.write(abData2, 0, nBytesRead2);
            }
        } catch (LineUnavailableException li) {
            li.printStackTrace();
            state = 'S';
            ErrorMessage msg = new ErrorMessage();
            msg.errorMessage = li.toString();
            javax.swing.SwingUtilities.invokeLater(msg);
            stop();
            return;
        } catch (IOException io) {
            io.printStackTrace();
            state = 'N';
            ErrorMessage msg = new ErrorMessage();
            msg.errorMessage = io.toString();
            javax.swing.SwingUtilities.invokeLater(msg);
            if (++errorDialogCount >= 5) {
                stop();
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            if (state == 'P') {
                state = 'E';
                exceptionCount++;
                if (exceptionCount > 10) {
                    ErrorMessage msg = new ErrorMessage();
                    msg.errorMessage = ex.toString();
                    javax.swing.SwingUtilities.invokeLater(msg);
                    stop();
                    return;
                }
            }
        } finally {
            if (announce != null) announce.cancel = true;
            announce = null;
            if (line != null) {
                if (state == 'S' || state == 'A' || state == 'M') {
                    line.stop();
                    line.flush();
                }
                lineDrain = new LineDrain(line, audioFormat);
                lineDrain.start();
                line = null;
            }
            if (state == 'P' && mainFrame.library.attributes.libraryType != 'P') state = 'S';
            updateSlider.actionPerformed(null);
            if (state == 'S' || state == 'P' || state == 'N' || state == 'R') {
                updateSlider.position = 0;
                updateSlider.timeDisplay = "";
            }
            if (state == 'P' || state == 'N') {
                nextSong.increment = 1;
                javax.swing.SwingUtilities.invokeLater(nextSong);
            }
            if (state == 'R') {
                if (startTime + playedTime < 2) {
                    nextSong.increment = -1;
                    javax.swing.SwingUtilities.invokeLater(nextSong);
                } else {
                    nextSong.increment = -999;
                    updateSlider.position = 0;
                    javax.swing.SwingUtilities.invokeLater(nextSong);
                }
            }
            if (state == 'E' || state == 'M') {
                nextSong.increment = -999;
                javax.swing.SwingUtilities.invokeLater(nextSong);
            }
        }
    }
