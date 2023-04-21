    public static void main(String[] args) throws Exception {
        AddWatermark render = new AddWatermark();
        FileInputStream fi = new FileInputStream(args[0]);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024 * 100];
        int size = 0;
        while ((size = fi.read(buffer)) != -1) {
            baos.write(buffer, 0, size);
        }
        FileInputStream imagefi = new FileInputStream(args[1]);
        ByteArrayOutputStream imagebaos = new ByteArrayOutputStream(1024);
        byte[] imagebuffer = new byte[1024 * 100];
        int imagesize = 0;
        while ((imagesize = imagefi.read(imagebuffer)) != -1) {
            imagebaos.write(imagebuffer, 0, imagesize);
        }
        FileOutputStream fo = new FileOutputStream(args[2]);
        fo.write(render.watermark(baos.toByteArray(), imagebaos.toByteArray(), "duplicate"));
        fo.close();
    }
