    public static void main(String[] args) throws Exception {
        ImageIcon ii = new ImageIcon("/home/aploese/workspace/AmikoViewer/AmikoViewer.gif");
        Image img = ii.getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT);
        while (img.getHeight(null) == -1) {
            Thread.sleep(50);
        }
        ResIcon ri = new ResIcon(img);
        ByteBuffer bb = ri.getData();
        FileOutputStream fos = new FileOutputStream("/home/aploese/test_20070720.ico");
        ByteArrayInputStream bas = new ByteArrayInputStream(bb.array());
        byte[] buffer = new byte[8192];
        int read;
        while ((read = bas.read(buffer)) > -1) {
            fos.write(buffer, 0, read);
        }
        fos.close();
    }
