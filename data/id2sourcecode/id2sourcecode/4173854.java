    public void setValue(double val) {
        Channel ch = mpv.getChannel();
        String chanName = mpv.getChannelName();
        if (ch != null) {
            try {
                ch.putVal(val);
                if (needStrobeToSet && strobeChannel != null) {
                    if (!strobeChannel.isConnected()) {
                        strobeChannel.requestConnection();
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                    strobeChannel.putVal(strobeValue);
                }
            } catch (ConnectionException e) {
                stopScanWithMessage("Cannot put value to the channel: " + chanName);
                return;
            } catch (PutException e) {
                stopScanWithMessage("Cannot put value to the channel: " + chanName);
                return;
            }
        }
    }
