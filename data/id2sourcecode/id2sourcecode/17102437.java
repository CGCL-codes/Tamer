    public void run() {
        try {
            if (os != null) {
                assert is != null;
                assert br == null;
                assert textArea == null;
                byte[] buffer = new byte[256];
                for (; ; ) {
                    int numread = is.read(buffer, 0, buffer.length);
                    if (numread < 0) {
                        break;
                    }
                    os.write(buffer, 0, numread);
                }
            } else {
                assert br != null;
                assert textArea != null;
                char[] buffer = new char[256];
                for (; ; ) {
                    int numread = br.read(buffer, 0, buffer.length);
                    if (numread < 0) {
                        break;
                    }
                    String s = new String(buffer, 0, numread);
                    writeString(s);
                }
            }
        } catch (IOException e) {
        }
    }
