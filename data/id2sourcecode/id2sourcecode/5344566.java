        public void run() {
            while (!t.isInterrupted()) {
                synchronized (lock) {
                    final long current = getCurrentUsage();
                    if (max < current) max = current;
                    average = (average + current) / 2;
                }
                try {
                    sleep(interval);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
