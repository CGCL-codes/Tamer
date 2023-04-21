    public void tcpDataReceived(NioServer.Event evt) {
        ByteBuffer inBuff = evt.getInputBuffer();
        ByteBuffer outBuff = evt.getOutputBuffer();
        cbuff.clear();
        CoderResult cr = decoder.reset().decode(inBuff, cbuff, true);
        if (cr == CoderResult.OVERFLOW) {
            outBuff.clear();
            RESP_400_BAD_REQUEST.rewind();
            encoder.reset().encode(RESP_400_BAD_REQUEST, outBuff, true);
            outBuff.flip();
            evt.closeAfterWriting();
        } else if (cr == CoderResult.UNDERFLOW) {
            cbuff.flip();
            System.out.println(cbuff);
            eoh_matcher.reset(cbuff);
            if (eoh_matcher.matches()) {
                get_matcher.reset(cbuff);
                if (get_matcher.matches()) {
                    String filename = get_matcher.group(1);
                    File file = new File(this.root, filename);
                    System.out.println("Requested filename " + filename + "(" + file + ")");
                    if (file.isFile()) {
                        try {
                            FileInputStream fis = new FileInputStream(file);
                            FileChannel fc = fis.getChannel();
                            evt.getKey().attach(fc);
                            CharBuffer mimeLine = contentTypeLine(filename);
                            CharBuffer sizeLine = contentLengthLine(fc.size());
                            System.out.println("OK: " + file);
                            outBuff.clear();
                            encoder.reset();
                            RESP_200_OK.rewind();
                            BLANK_LINE.rewind();
                            encoder.encode(RESP_200_OK, outBuff, false);
                            encoder.encode(mimeLine, outBuff, false);
                            encoder.encode(sizeLine, outBuff, false);
                            encoder.encode(BLANK_LINE, outBuff, true);
                            outBuff.flip();
                        } catch (IOException ex) {
                            Logger.getLogger(HttpNioExample.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println("500: " + ex.getMessage());
                            outBuff.clear();
                            encoder.reset();
                            RESP_500_INTERNAL_ERROR.rewind();
                            BLANK_LINE.rewind();
                            encoder.encode(RESP_500_INTERNAL_ERROR, outBuff, false);
                            encoder.encode(BLANK_LINE, outBuff, true);
                            outBuff.flip();
                            evt.closeAfterWriting();
                        }
                    } else {
                        System.err.println("Not a file: " + file);
                        outBuff.clear();
                        encoder.reset();
                        RESP_404_NOT_FOUND.rewind();
                        BLANK_LINE.rewind();
                        encoder.encode(RESP_404_NOT_FOUND, outBuff, false);
                        encoder.encode(BLANK_LINE, outBuff, true);
                        outBuff.flip();
                        evt.closeAfterWriting();
                    }
                } else {
                    System.out.println("400: Headers done but no valid GET");
                    outBuff.clear();
                    encoder.reset();
                    RESP_400_BAD_REQUEST.rewind();
                    BLANK_LINE.rewind();
                    encoder.encode(RESP_400_BAD_REQUEST, outBuff, false);
                    encoder.encode(BLANK_LINE, outBuff, true);
                    outBuff.flip();
                    evt.closeAfterWriting();
                }
            } else {
                System.out.println("Headers not done: " + cbuff);
                inBuff.flip();
            }
        }
    }
