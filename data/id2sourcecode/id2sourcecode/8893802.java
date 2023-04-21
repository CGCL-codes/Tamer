            public String readAsciiLine() throws IOException {
                if (lineSeparator == null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while (true) {
                        int b = read();
                        if (b == -1) {
                            return null;
                        }
                        if (b == '\n') {
                            lineSeparator = LineSeparator.LF;
                            return new String(baos.toByteArray(), "UTF-8");
                        }
                        if (b == '\r') {
                            int b2 = read();
                            if (b2 == '\n') {
                                lineSeparator = LineSeparator.CRLF;
                            } else {
                                lineSeparator = LineSeparator.CR;
                                if (b2 != -1) {
                                    lastByte = b2;
                                }
                            }
                            return new String(baos.toByteArray(), "UTF-8");
                        }
                        baos.write(b);
                    }
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (lastByte != -1) {
                    baos.write(lastByte);
                    lastByte = -1;
                }
                switch(lineSeparator) {
                    case CR:
                        for (int b; (b = read()) != '\r' && b != -1; baos.write(b)) {
                        }
                        break;
                    case LF:
                        for (int b; (b = read()) != '\n' && b != -1; baos.write(b)) {
                        }
                        break;
                    case CRLF:
                        for (int b; (b = read()) != '\r' && b != -1; baos.write(b)) {
                        }
                        read();
                        break;
                }
                return new String(baos.toByteArray(), "UTF-8");
            }
