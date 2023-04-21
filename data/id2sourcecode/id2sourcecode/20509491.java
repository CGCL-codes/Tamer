    private void saveFile(String content, String path) throws IOException {
        File f = new File(path);
        if (f.exists()) f.delete();
        f.createNewFile();
        FileWriter out = new FileWriter(f);
        StringReader in = new StringReader(content);
        int b;
        while ((b = in.read()) != -1) out.write(b);
        out.flush();
        out.close();
        in.close();
    }
