    public static void write(File file, InputStream input) throws FileNotFoundException, IOException {
        InputStream in = new BufferedInputStream(input);
        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        int ch;
        while ((ch = in.read()) != -1) out.write(ch);
        close(in);
        close(out);
    }
