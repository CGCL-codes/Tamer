    public void run() {
        while (!mStopped) {
            long lastActive = mDataChannel.getLastActiveTime();
            if (mCirUrl != null) {
                if (SystemClock.elapsedRealtime() - lastActive >= HTTP_CIR_PING_INTERVAL) {
                    HttpURLConnection urlConnection;
                    try {
                        urlConnection = (HttpURLConnection) mCirUrl.openConnection();
                        if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            mConnection.sendPollingRequest();
                        }
                    } catch (IOException e) {
                        mStopped = true;
                    }
                }
                try {
                    Thread.sleep(HTTP_CIR_PING_INTERVAL);
                } catch (InterruptedException e) {
                }
            } else {
                if (SystemClock.elapsedRealtime() - lastActive >= mServerPollMin && mDataChannel.isSendingQueueEmpty()) {
                    mConnection.sendPollingRequest();
                }
                try {
                    Thread.sleep(mServerPollMin);
                } catch (InterruptedException e) {
                }
            }
        }
    }
