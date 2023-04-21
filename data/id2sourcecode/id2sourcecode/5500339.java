    public void setValue(double val) {
        Channel ch = mpv.getChannel();
        String chanName = mpv.getChannelName();
        if (ch != null) {
            try {
                ch.putVal(val);
                if (strobeChan != null) {
                    try {
                        strobeChan.putVal(strobeMask);
                    } catch (ConnectionException e) {
                        stopScanWithMessage("Cannot put value to the channel: " + strobeChan.channelName());
                        return;
                    } catch (PutException e) {
                        stopScanWithMessage("Cannot put value to the channel: " + strobeChan.channelName());
                        return;
                    }
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
