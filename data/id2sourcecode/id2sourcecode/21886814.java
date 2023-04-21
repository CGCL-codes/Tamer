    private void startConnectSession() {
        if (Constants.USE_TCP_DEBUG) {
            mConnectThread = new SocketConnectThread("localhost", Constants.TCP_DEBUG_PORT, 0);
            mConnectThread.start();
        } else {
            int channel = BluetoothOppPreference.getInstance(mContext).getChannel(mBatch.mDestination, OPUSH_UUID16);
            if (channel != -1) {
                if (D) Log.d(TAG, "Get OPUSH channel " + channel + " from cache for " + mBatch.mDestination);
                mTimestamp = System.currentTimeMillis();
                mSessionHandler.obtainMessage(SDP_RESULT, channel, -1, mBatch.mDestination).sendToTarget();
            } else {
                doOpushSdp();
            }
        }
    }
