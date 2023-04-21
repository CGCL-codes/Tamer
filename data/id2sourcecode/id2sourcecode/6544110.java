    public void run() {
        try {
            while (true) {
                selectedOutputLine.flush();
                while (audioLoopRunning) {
                    Arrays.fill(readBuffer, (byte) 0);
                    int numBytesRead = 0;
                    if (audioDataProvider != null) {
                        numBytesRead = audioDataProvider.onDataRequested(readBuffer);
                    }
                    if (numBytesRead <= 0) {
                        stopAudioLoop = true;
                        level = 0.0;
                    } else {
                        selectedOutputLine.write(readBuffer, 0, numBytesRead);
                        computeLevel();
                    }
                    if (stopAudioLoop) {
                        audioLoopRunning = false;
                    }
                }
                synchronized (audioStopSync) {
                    audioStopSync.notifyAll();
                }
                synchronized (audioStartSync) {
                    audioStartSync.wait();
                    audioLoopRunning = true;
                    stopAudioLoop = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
