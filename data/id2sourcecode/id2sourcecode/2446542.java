    public static boolean deliveryEntity(Header param, InputStream is, OutputStream os, OutputStream oss, boolean keepalive) {
        boolean ret = keepalive;
        try {
            String te = param.getLastValue("Transfer-Encoding");
            byte[] buf = new byte[4096];
            if (te == null || te.toLowerCase().equals("identity")) {
                String sSize = param.getLastValue("Content-Length");
                if (sSize == null) {
                    if (ret) {
                        return true;
                    } else {
                        ret = false;
                        int size;
                        try {
                            while ((size = is.read(buf)) >= 0) {
                                os.write(buf, 0, size);
                                oss.write(buf, 0, size);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    int size = Integer.parseInt(sSize);
                    int read;
                    try {
                        while (size > 0) {
                            if (size > 4096) {
                                read = is.read(buf);
                            } else {
                                read = is.read(buf, 0, size);
                            }
                            if (read < 0) {
                                throw new IOException("Unexpected stream close.");
                            }
                            size -= read;
                            os.write(buf, 0, read);
                            oss.write(buf, 0, read);
                        }
                    } catch (IOException e) {
                        ret = false;
                    }
                }
            } else {
                System.err.println(Thread.currentThread().hashCode() + "Condiction 3");
                try {
                    while (true) {
                        String sLength = readLine(is);
                        if (sLength == null) {
                            return false;
                        }
                        os.write(sLength.getBytes(ISO8859_1));
                        os.write(CRLF);
                        if (sLength.length() == 0) {
                            return true;
                        }
                        int idx = sLength.indexOf(" ");
                        if (idx >= 0) {
                            sLength = sLength.substring(idx);
                        }
                        int length = Integer.parseInt(sLength, 16);
                        if (length == 0) {
                            break;
                        } else {
                            length += 2;
                            int read;
                            while (length > 0) {
                                if (length > 4096) {
                                    read = is.read(buf);
                                } else {
                                    read = is.read(buf, 0, length);
                                }
                                length -= read;
                                os.write(buf, 0, read);
                                oss.write(buf, 0, read);
                            }
                        }
                    }
                    String line;
                    while (true) {
                        line = HTTPUtils.readLine(is);
                        if (line == null) {
                            ret = false;
                            break;
                        }
                        os.write(line.getBytes(ISO8859_1));
                        os.write(HTTPUtils.CRLF);
                        if (line.length() == 0) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ret = false;
                }
            }
        } finally {
            try {
                os.flush();
            } catch (IOException ex) {
            }
        }
        return ret;
    }
