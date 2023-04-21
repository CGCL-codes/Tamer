            @Override
            public void run() {
                InetSocketAddress addr = new InetSocketAddress(account.getHost(), account.getPort());
                try {
                    client.connect(addr).get();
                    log.fine("Connection complete.");
                } catch (Exception e) {
                    startLock.lock();
                    if (isStarting()) {
                        FutureManager<Object> fm = startfp.getManager();
                        startfp = null;
                        fm.cancelFuture(new BotConnectionFailedException(e));
                    } else {
                        beginConnect(RECONNECT_DELAY_MS);
                    }
                    startLock.unlock();
                    return;
                }
                try {
                    client.login(account.getUser(), pass, pvpgn).get();
                    log.fine("Login complete.");
                } catch (Exception e) {
                    startLock.lock();
                    if (isStarting()) {
                        FutureManager<Object> fm = startfp.getManager();
                        startfp = null;
                        fm.cancelFuture(new BotLoginFailedException(e));
                    } else {
                    }
                    startLock.unlock();
                    beginDisconnect(0);
                    return;
                }
                startLock.lock();
                if (isStarting()) {
                    FutureManager<Object> fm = startfp.getManager();
                    startfp = null;
                    started = true;
                    fm.completeFuture(null);
                } else {
                    try {
                        ed.dispatch(SingleChannelChatListener.class, "forcedPart");
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
                startLock.unlock();
            }
