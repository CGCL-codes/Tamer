            @Override
            public void tcpDataReceived(NioServer.Event evt) {
                ByteBuffer inBuff = evt.getInputBuffer();
                ByteBuffer outBuff = evt.getOutputBuffer();
                request.clear();
                CoderResult cr = decoder.reset().decode(inBuff, request, true);
                request.flip();
                String s = request.toString();
                if (cr == CoderResult.OVERFLOW) {
                    outBuff.clear();
                    encoder.reset().encode((CharBuffer) nackTooLong.rewind(), outBuff, true);
                    outBuff.flip();
                    inBuff.clear().flip();
                    return;
                }
                if (s.contains("\r") || s.contains("\n")) {
                    FileInputStream fis = null;
                    try {
                        s = s.trim();
                        fis = new FileInputStream(s);
                        FileChannel fc = fis.getChannel();
                        evt.getKey().attach(fc);
                        evt.setNotifyOnTcpWritable(true);
                        outBuff.clear();
                        ack.rewind();
                        encoder.reset().encode(ack, outBuff, true);
                        outBuff.flip();
                    } catch (IOException ex) {
                        Logger.getLogger(this.getClass().getName()).warning(ex.getMessage());
                        outBuff.clear();
                        encoder.reset().encode((CharBuffer) nackNotFound.rewind(), outBuff, true);
                        outBuff.flip();
                        inBuff.clear().flip();
                    }
                } else {
                    inBuff.flip();
                }
            }
