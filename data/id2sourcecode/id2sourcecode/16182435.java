    void updateVersion(String f) throws IOException {
        final File file = new File(this.outputDirectory, f);
        final File temp = File.createTempFile("ver", ".tmp");
        FileUtils.copyFile(file, temp);
        final OutputStream os = new FileOutputStream(file);
        final Writer wr = new OutputStreamWriter(os, this.encoding);
        final PrintWriter pw = new PrintWriter(wr);
        final InputStream is = new FileInputStream(temp);
        final Reader isr = new InputStreamReader(is, this.encoding);
        final BufferedReader br = new BufferedReader(isr);
        String ln = null;
        while ((ln = br.readLine()) != null) {
            ln = ln.replace(this.replace, this.with);
            pw.println(ln);
        }
        br.close();
        pw.close();
    }
