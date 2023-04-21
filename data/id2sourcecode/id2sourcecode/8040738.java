   * transfer lines
   */
    private void readwrite(Reader in, Writer out) throws IOException {
        BufferedReader bin = new BufferedReader(in);
        BufferedWriter bout = new BufferedWriter(out);
        while (true) {
            String line = bin.readLine();
            if (line == null) break;
            bout.write(line);
            bout.newLine();
        }
        bin.close();
