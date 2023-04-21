    final void update() {
        if (state == DEFAULT) return;
        long count = 0;
        for (DownloadWorker w : warray) {
            count += w.bytesRead;
        }
        bytesRead = count;
        byteSpeed = bytesRead - bytesReadLastSec;
        bytesReadLastSec = bytesRead;
        aveByteSpeed = (byteSpeed + byteSpeedLastSec) / 2;
        byteSpeedLastSec = byteSpeed;
        long bytesLeft = size - bytesRead;
        if (state == DOWNLOADING) {
            timeLeft = bytesLeft / max(1, byteSpeed);
            long currentTime = System.currentTimeMillis();
            elapsedTime = (currentTime - startTime) / 1000;
        }
        progress = (state == BUILDING) ? builder.getProgress() : (size < 1) ? 0 : (bytesRead * 100) / size;
    }
