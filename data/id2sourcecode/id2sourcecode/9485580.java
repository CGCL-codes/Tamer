    private String loadFile(String string) throws IOException {
        String result = null;
        File file = new File("test/src/org/exist/xslt/test/bench/v1_0/" + string);
        if (!file.canRead()) {
            throw new IOException("can load information.");
        } else {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            int sz = (int) fc.size();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, sz);
            Charset charset = Charset.forName("ISO-8859-15");
            CharsetDecoder decoder = charset.newDecoder();
            CharBuffer cb = decoder.decode(bb);
            result = cb.toString();
            if (result.startsWith("<?xml version=\"1.0\"?>")) result = result.substring("<?xml version=\"1.0\"?>".length());
            if (result.startsWith("<?xml version=\"1.0\" encoding=\"utf-8\"?>")) result = result.substring("<?xml version=\"1.0\" encoding=\"utf-8\"?>".length());
            fc.close();
        }
        return result;
    }
