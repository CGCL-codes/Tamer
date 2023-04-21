    public static void main(String[] args) {
        try {
            FileInputStream fin = new FileInputStream(args[0]);
            BufferedInputStream bin = new BufferedInputStream(fin);
            byte[] bytes = new byte[1024];
            int read;
            Writer writer = new PrintWriter(System.out);
            while ((read = bin.read(bytes)) > -1) hexDump(bytes, read, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
