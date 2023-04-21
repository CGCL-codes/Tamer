        public void run() {
            Msglet msglet = getMsglet();
            if (msglet == null) return;
            int n = 0;
            readLock.lock();
            try {
                n = channelState.doRead(this, msglet);
            } catch (ChannelClosedException e) {
                return;
            } finally {
                readLock.unlock();
            }
            if (n == 0) {
                lock.lock();
                try {
                    channelState.interestRead(Channel.this);
                } catch (ChannelClosedException e) {
                    return;
                } finally {
                    lock.unlock();
                }
            } else if (n > 0) {
                boolean ok = false;
                ChannelMsgListener channelMsgListener = getChannelMsgListener();
                lock.lock();
                try {
                    if (ok = channelState.doSplitMsg(this, msglet)) {
                        log();
                        channelMsgListener.onMessage(Channel.this, dataList);
                    }
                } catch (ChannelClosedException e) {
                    return;
                } catch (Exception e) {
                    ok = false;
                    log.error(e, "Error - ", Channel.this);
                } finally {
                    dataList.clear();
                    lock.unlock();
                }
                if (ok) {
                    try {
                        channelMsgListener.onMessageComplete(Channel.this);
                    } catch (Exception e) {
                        Channel.this.close();
                        log.error(e, "Error - ", Channel.this);
                    }
                } else Channel.this.close();
            } else Channel.this.close();
        }
