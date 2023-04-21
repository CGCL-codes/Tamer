        public void run() {
            boolean nativeReassertNeeded = false;
            setCheckInterval(60);
            while (isRunning) {
                try {
                    Socket s = serverSocket.getChannel().socket().accept();
                    if (isRunning == true) handle(s);
                } catch (Exception e) {
                }
                if (listenForLostLock) {
                    try {
                        long start = System.currentTimeMillis();
                        synchronized (FileConcurrencyLock.this) {
                            if (isRunning == false) break;
                            assertLock(nativeReassertNeeded);
                        }
                        long end = System.currentTimeMillis();
                        logger.log(Level.FINEST, "assertValidity took {0} ms", new Long(end - start));
                        nativeReassertNeeded = false;
                        setCheckInterval(60);
                    } catch (LockUncertainException lue) {
                        nativeReassertNeeded = true;
                        setCheckInterval(20);
                    } catch (Exception e) {
                        logger.log(Level.FINER, "Exception when listening for lost lock", e);
                        try {
                            dispatchMessage(LockMessage.LOCK_LOST_MESSAGE);
                        } catch (Exception e1) {
                        }
                        setCheckInterval(0);
                    }
                }
            }
        }
