        @Override
        public void run() {
            Map<String, Link> links = router.getLinks();
            for (Link link : links.values()) {
                Message ping = new DefaultMessage();
                ping.setHeader(Message.TYPE, Message.TYPE_ANALYZE);
                ping.setHeader(Message.MESSAGE_NAME, MESSAGE_NAME_PING);
                ping.setHeader(Message.TIME_STAMP, String.valueOf(System.currentTimeMillis()));
                try {
                    if (link.getChannel() != null) {
                        link.processMessage(ping);
                    }
                } catch (ProcessorException e) {
                    logger.error("Exception in Latency Analyzer ignored.", e);
                }
            }
            try {
                timer.schedule(new LatencyAnalyzerTask(), measureInterval);
            } catch (IllegalStateException e) {
            }
        }
