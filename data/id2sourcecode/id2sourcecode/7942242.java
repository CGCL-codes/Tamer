    public static void main(String[] arg) throws Exception {
        System.out.println("Downloading " + arg[0] + " to file " + arg[1]);
        URL url = new URL(arg[0]);
        InputStream in = url.openStream();
        FileOutputStream out = new FileOutputStream(arg[1]);
        byte chunk[] = new byte[8 * 1024];
        int amountRead;
        while ((amountRead = in.read(chunk)) != -1) {
            out.write(chunk, 0, amountRead);
        }
        in.close();
        out.close();
    }
