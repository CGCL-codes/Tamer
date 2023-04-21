        public void run() {
            logger.debug("start ReadXMLToOutputStream thread");
            Writer writeStream = null;
            try {
                writeStream = new OutputStreamDecoderWriter(transaction.getDataOutputStream());
            } catch (UnsupportedEncodingException e1) {
                logger.error("OutputStreamDecoderWriter require jvm platform to support US_ASCII charset ", e1);
                except = e1;
                return;
            }
            try {
                boolean bEndAttachData;
                int tokenType;
                logger.debug("Reading XML stream and printing to output stream");
                bEndAttachData = false;
                while (reader.hasNext() && !bEndAttachData) {
                    tokenType = reader.next();
                    if (tokenType == EventConstants.START_ELEMENT) {
                        logger.warn("Readed START_ELEMENT :" + "element text:" + reader.getElementText() + " localName:" + reader.getLocalName() + " Name:" + reader.getName());
                    } else if (tokenType == EventConstants.END_DOCUMENT) {
                        logger.debug("End document");
                        bEndAttachData = true;
                    } else if (tokenType == EventConstants.END_ELEMENT) {
                        logger.warn("Readed END_ELEMENT :" + " localName:" + reader.getLocalName() + " Name:" + reader.getName());
                        bEndAttachData = true;
                    } else if (tokenType == EventConstants.CHARACTERS || tokenType == EventConstants.CDATA) {
                        logger.debug("CHARACTERS events were found " + tokenType);
                        try {
                            int reads;
                            reads = reader.getText(writeStream, false);
                            writeStream.flush();
                            logger.debug("getText readed bytes " + reads);
                        } catch (IOException e) {
                            logger.error("Exception during Character getText ", e);
                            except = e;
                        }
                    } else {
                        logger.warn("another EventType " + tokenType);
                    }
                }
                logger.debug("No more characters events, end output");
                try {
                    logger.debug("Closing output writer");
                    writeStream.close();
                } catch (IOException e) {
                    logger.error("Error string stream writer ", e);
                    except = e;
                }
            } catch (XMLStreamException e) {
                logger.error("An XMLStreamException has ocurred", e);
                except = e;
            }
            logger.debug("ReadXMLToOutputStream done");
            try {
                finishRead();
            } catch (XMLStreamException e) {
                logger.error(e);
                e.printStackTrace();
                except = e;
            } catch (IOException e) {
                logger.error("closing socket ", e);
                except = e;
            }
        }
