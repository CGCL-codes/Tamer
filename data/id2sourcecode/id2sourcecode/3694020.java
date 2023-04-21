    public static String getDescrFromHD(File descrFile) throws IOException {
        try {
            String descr = "";
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(descrFile);
                FileChannel fcin = fin.getChannel();
                CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
                ByteBuffer buffer = ByteBuffer.allocateDirect(32 * 1024);
                while (true) {
                    buffer.clear();
                    int r = fcin.read(buffer);
                    if (r == -1) {
                        break;
                    }
                    buffer.flip();
                    descr += decoder.decode(buffer).toString();
                }
                return descr;
            } finally {
                try {
                    fin.close();
                } catch (Exception e) {
                }
            }
        } catch (FileNotFoundException e) {
            log.warn("Не удается найти файл описания: " + descrFile);
            return "";
        }
    }
