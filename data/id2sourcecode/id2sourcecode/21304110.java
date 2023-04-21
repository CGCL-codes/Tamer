        public LogWriterThread(Writer writer, String fileName, final BlockingQueue<Event> eventQueue) {
            super();
            this.writer = writer;
            this.fileName = fileName;
            this.threadEventQueue = eventQueue;
        }
