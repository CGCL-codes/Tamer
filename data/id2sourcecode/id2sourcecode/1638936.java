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
