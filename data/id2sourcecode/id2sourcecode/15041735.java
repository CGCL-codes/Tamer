    public void test() throws Exception {
        String s = "file:///C:/Documents%20and%20Settings/lourival.junior/workspace/funttel/web/images/close.png";
        URL url = new URL(s);
        InputStream input = url.openStream();
        OutputStream output = new FileOutputStream("D:/close-new.png");
        Writer writer = new FileWriter("D:/close-new-2.png");
        byte[] b = new byte[input.available()];
        input.read(b);
        output.write(b);
        writer.write(new String(b));
        input.close();
        output.flush();
        output.close();
        writer.close();
    }
