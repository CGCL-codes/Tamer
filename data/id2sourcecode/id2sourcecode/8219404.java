        public void run() {
            int selectReturn;
            int selectFailureOrZeroCount = 0;
            boolean pqReturn;
            Iterator keyIter;
            SelectionKey key;
            SocketChannel dst;
            DelayedDataInfo info;
            ByteBuffer delayedBuffer;
            int numberOfBytes;
            SocketChannel src;
            WHILETRUE: while (true) {
                pqReturn = processQueue();
                if (pqReturn) {
                    selectFailureOrZeroCount = 0;
                }
                if (selectFailureOrZeroCount >= 10) {
                    logger.warning("select appears to be failing repeatedly, pausing");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    selectFailureOrZeroCount = 0;
                }
                try {
                    selectReturn = delayedSelector.select();
                    if (selectReturn > 0) {
                        selectFailureOrZeroCount = 0;
                    } else {
                        selectFailureOrZeroCount++;
                    }
                } catch (IOException e) {
                    logger.warning("Error when selecting for ready channel: " + e.getMessage());
                    selectFailureOrZeroCount++;
                    continue WHILETRUE;
                }
                logger.finest("select reports " + selectReturn + " channels ready to write");
                keyIter = delayedSelector.selectedKeys().iterator();
                KEYITER: while (keyIter.hasNext()) {
                    key = (SelectionKey) keyIter.next();
                    keyIter.remove();
                    dst = (SocketChannel) key.channel();
                    synchronized (delayedInfo) {
                        info = (DelayedDataInfo) delayedInfo.get(dst);
                    }
                    delayedBuffer = info.getBuffer();
                    try {
                        numberOfBytes = dst.write(delayedBuffer);
                        logger.finest("Wrote " + numberOfBytes + " delayed bytes to " + dst + ", " + delayedBuffer.remaining() + " bytes remain delayed");
                        if (!delayedBuffer.hasRemaining()) {
                            try {
                                key.interestOps(key.interestOps() ^ SelectionKey.OP_WRITE);
                            } catch (CancelledKeyException e) {
                            }
                            src = info.getSource();
                            dumpDelayedState(info.getDest());
                            addToReactivateList(src);
                        }
                    } catch (IOException e) {
                        logger.warning("Error writing delayed data: " + e.getMessage());
                        closeConnection(dst, info.getSource(), info.isClientToServer());
                    }
                }
            }
        }
