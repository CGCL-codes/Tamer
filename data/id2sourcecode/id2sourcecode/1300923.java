    public static String readFileAll(InputStream res) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] utf8Header = new byte[3];
            byte[] buf = new byte[1024];
            int read = 0;
            int readLen = 0;
            while ((read = res.read(utf8Header, readLen, 3 - readLen)) != -1) {
                readLen += read;
                if (readLen == 3) {
                    if (utf8Header[0] == (byte) 0xef && utf8Header[1] == (byte) 0xbb && utf8Header[2] == (byte) 0xbf) {
                    } else {
                        baos.write(utf8Header);
                    }
                    break;
                }
            }
            while ((read = res.read(buf)) != -1) {
                baos.write(buf, 0, read);
            }
            res.close();
            return baos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Missing";
    }
