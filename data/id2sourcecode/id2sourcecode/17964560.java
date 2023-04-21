    public static String readAFile(InputStream res) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            boolean isUTF8 = false;
            byte[] utf8Header = new byte[3];
            byte[] buf = new byte[1024];
            int read = 0;
            int readLen = 0;
            while ((read = res.read(utf8Header, readLen, 3 - readLen)) != -1) {
                readLen += read;
                if (readLen == 3) {
                    if (utf8Header[0] == (byte) 0xef && utf8Header[1] == (byte) 0xbb && utf8Header[2] == (byte) 0xbf) {
                        isUTF8 = true;
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
            return isUTF8 ? baos.toString() : baos.toString("utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
