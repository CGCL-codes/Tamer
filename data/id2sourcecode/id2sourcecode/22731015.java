        @Override
        public void run() {
            setName("NCA - write thread");
            while (running) {
                try {
                    byte[] data;
                    data = mWriteQueueT.take();
                    write(data);
                } catch (InterruptedException e) {
                    Log.e(TAG, "WriteThread write error ", e);
                }
            }
        }
