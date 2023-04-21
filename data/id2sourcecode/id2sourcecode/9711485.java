    private void writeTraceFile(final File destination_file, final String trace_file_name, final String trace_file_path) throws IOException {
        URL url = null;
        BufferedInputStream is = null;
        FileOutputStream fo = null;
        BufferedOutputStream os = null;
        int b = 0;
        url = new URL("http://" + trace_file_path + "/" + trace_file_name);
        is = new BufferedInputStream(url.openStream());
        fo = new FileOutputStream(destination_file);
        os = new BufferedOutputStream(fo);
        while ((b = is.read()) != -1) {
            os.write(b);
        }
        os.flush();
        is.close();
        os.close();
    }
