    private void playing() {
        if (currentAudioFormat == null) {
            loading();
            if (currentAudioFormat == null) {
                return;
            }
        }
        assert (currentAudioFormat != null);
        currentAudioFormat.startSourceDataLine();
        avail = currentAudioFormat.getSourceDataLine().available();
        if (avail > 0) {
            try {
                readBytes = currentAudioFormat.getAudioInputStream().read(buffer, 0, Math.min(avail, buffer.length));
            } catch (ArrayIndexOutOfBoundsException e) {
                playerState = STOPPED;
                notifyEvent(EOM, -1);
                loadSong = true;
            } catch (IOException e) {
                playerState = STOPPED;
            }
            if (readBytes > 0) {
                currentAudioFormat.getSourceDataLine().write(buffer, 0, readBytes);
                notifyProgress(currentAudioFormat.getEncodedStreamPosition());
            } else if (readBytes == -1) {
                notifyEvent(EOM, -1);
                loadSong = true;
                try {
                    synchronized (threadLock) {
                        threadLock.wait(SLEEP_PLAYING);
                    }
                } catch (InterruptedException e) {
                }
            }
        } else {
            try {
                synchronized (threadLock) {
                    threadLock.wait(SLEEP_PLAYING);
                }
            } catch (InterruptedException e) {
            }
        }
    }
