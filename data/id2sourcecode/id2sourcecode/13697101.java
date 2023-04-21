    public AudioPlayer(URL songFile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        final AudioInputStream encodedInput = AudioSystem.getAudioInputStream(songFile);
        if (encodedInput == null) throw new AssertionError("Error playing file");
        AudioFormat baseFormat = encodedInput.getFormat();
        AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
        soundOutput = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, targetFormat));
        soundOutput.open(targetFormat);
        final AudioInputStream decodedInput = AudioSystem.getAudioInputStream(targetFormat, encodedInput);
        frameSize = targetFormat.getFrameSize();
        frameRate = targetFormat.getFrameRate();
        bufferFile = File.createTempFile("audioPlayer", ".mp3");
        bufferFile.deleteOnExit();
        raf = new RandomAccessFile(bufferFile, "rw");
        currentFrame = 0;
        raf.seek(0);
        new Thread(new Runnable() {

            public void run() {
                try {
                    long currentLocation = 0;
                    int nBytesRead = 0;
                    while ((nBytesRead = decodedInput.read(largeBuffer, 0, largeBuffer.length)) != -1) {
                        synchronized (raf) {
                            long previousLocation = raf.getFilePointer();
                            raf.seek(currentLocation);
                            raf.write(largeBuffer, 0, nBytesRead);
                            raf.seek(previousLocation);
                        }
                        currentLocation += nBytesRead;
                        loadedFramesCount = currentLocation / frameSize;
                        synchronized (listeners) {
                            for (AudioPlayerListener listener : listeners) listener.frameLoaded(new AudioPlayerEvent(AudioPlayer.this, currentFrame, loadedFramesCount, loadCompleted));
                        }
                    }
                    loadCompleted = true;
                    synchronized (listeners) {
                        for (AudioPlayerListener listener : listeners) listener.frameLoaded(new AudioPlayerEvent(AudioPlayer.this, currentFrame, loadedFramesCount, loadCompleted));
                    }
                    decodedInput.close();
                    encodedInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Throwable t) {
                    ErrorDetailsDialog.open(t);
                }
            }
        }).start();
    }
