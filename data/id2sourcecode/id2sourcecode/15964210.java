        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, TcpServer.class.getSimpleName() + " NIO writer " + ++this.id);
            thread.setDaemon(true);
            return thread;
        }
