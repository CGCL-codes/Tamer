        public void run() {
            try {
                byte[] buffer = new byte[1024];
                if (!mSource.getLine().isOpen()) mSource.getLine().open();
                mSource.getLine().start();
                while (!mStop.get()) {
                    int read = mSource.getStream().read(buffer, 0, buffer.length);
                    mDest.write(buffer, 0, read);
                }
                mSource.getLine().stop();
                mSource.getLine().flush();
                mDest.flush();
                mDest.close();
            } catch (Exception e) {
                e.printStackTrace();
                mException = e;
            }
        }
