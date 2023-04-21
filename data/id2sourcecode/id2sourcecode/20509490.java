    private String loadFile(String path) throws IOException, FileNotFoundException {
        BufferedReader in = new BufferedReader(new FileReader(path));
        StringWriter out = new StringWriter();
        int b;
        while ((b = in.read()) != -1) out.write(b);
        out.flush();
        out.close();
        in.close();
        return out.toString();
    }
