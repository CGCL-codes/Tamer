    private static void process(InputStream input, String name) throws IOException {
        InputStreamReader isr = new InputStreamReader(input);
        BufferedReader reader = new BufferedReader(isr);
        String line;
        OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(name)), "8859_1");
        while ((line = reader.readLine()) != null) out.write(line);
        reader.close();
        out.close();
    }
