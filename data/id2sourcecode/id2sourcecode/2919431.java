    public void store(Reader reader) throws IOException {
        FileWriter writer = new FileWriter(this);
        char[] buf = new char[1024];
        int a = 0;
        while ((a = reader.read(buf)) != -1) writer.write(buf, 0, a);
        reader.close();
        writer.close();
        return;
    }
