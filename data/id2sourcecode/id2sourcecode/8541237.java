    private String loadFile(String fileURL, boolean incapsulate) throws IOException {
        String result = null;
        File file = new File(fileURL);
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
            if (result.startsWith("<?xml ")) {
                int endAt = result.indexOf("?>");
                result = result.substring(endAt + 2);
            }
            if (incapsulate) {
                result = result.replaceAll("\\{", "\\{\\{");
                result = result.replaceAll("\\}", "\\}\\}");
            }
            fc.close();
        }
        return result;
    }
