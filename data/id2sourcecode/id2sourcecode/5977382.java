        public void run() {
            Context context = Context.getInstance();
            Workshop workshop = context.getWorkshop();
            ServerChannelFactory serverChannelFactory = context.getChannelManager().getServerChannelFactory();
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    if (selector.select() <= 0) {
                        if (Thread.currentThread().isInterrupted()) break;
                        selLock.lock();
                        selLock.unlock();
                        continue;
                    }
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        Runnable onAccept = null;
                        try {
                            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                            ServerProps serverProps = ((Server) key.attachment()).getServerProps();
                            onAccept = serverChannelFactory.createServerChannel().onAccept(socketChannel, serverProps);
                            workshop.run(onAccept);
                        } catch (RejectedExecutionException e) {
                            onAccept.run();
                        } catch (Exception e) {
                            log.error(e, "Failed to accept");
                        }
                    }
                }
            } catch (IOException e) {
                log.fatal(e, "Server Manager error(need restart)");
                return;
            }
            stopAllServers();
        }
