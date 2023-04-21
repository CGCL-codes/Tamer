    @Override
    public void doPost(HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        try {
            int bodylen = req.getContentLength();
            if (bodylen > 0) {
                Buffer content = new Buffer(bodylen);
                int len = content.read(req.getInputStream());
                if (len > 0) {
                    EventHeaderTags tags = new EventHeaderTags();
                    Event event = GAEEventHelper.parseEvent(content, tags);
                    EventSendService sendService = new EventSendService() {

                        public int getMaxDataPackageSize() {
                            return -1;
                        }

                        public void send(Buffer buf) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("Send result back with body len:" + buf.readableBytes());
                            }
                            resp.setStatus(200);
                            resp.setContentType("application/octet-stream");
                            resp.setContentLength(buf.readableBytes());
                            try {
                                resp.getOutputStream().write(buf.getRawBuffer(), buf.getReadIndex(), buf.readableBytes());
                                resp.getOutputStream().flush();
                            } catch (IOException e) {
                                logger.error("Failed to send HTTP response.", e);
                            }
                        }
                    };
                    event.setAttachment(new Object[] { tags, sendService });
                    EventDispatcher.getSingletonInstance().dispatch(event);
                }
            }
        } catch (Throwable e) {
            logger.warn("Failed to process message", e);
        }
    }
