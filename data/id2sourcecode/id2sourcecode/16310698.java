    public void tagDefineBitsJPEG2(int id, InputStream jpegImage) throws IOException {
        startTag(TAG_DEFINEBITSJPEG2, id, true);
        out.writeUI8(0xff);
        out.writeUI8(0xd9);
        out.writeUI8(0xff);
        out.writeUI8(0xd8);
        int read = 0;
        byte[] bytes = new byte[10000];
        while ((read = jpegImage.read(bytes)) >= 0) {
            out.write(bytes, 0, read);
        }
        jpegImage.close();
        completeTag();
    }
