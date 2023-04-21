        private synchronized void setPaused(boolean paused) {
            if (imageComponentComponent == null) return;
            this.paused = paused;
            if (paused) {
                pauseButton.setText("Resume");
            } else {
                pauseButton.setText("Pause");
                if (writeThread != null) {
                    synchronized (writeThread) {
                        writeThread.notify();
                    }
                }
            }
        }
