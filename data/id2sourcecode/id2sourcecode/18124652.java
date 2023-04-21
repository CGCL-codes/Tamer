    private void writeFileToURL(OutputStream out, String filename, String fieldname, InputStream in, String mimetype) throws IOException {
        writeln(out, "Content-Disposition: form-data; name=\"" + encode(fieldname) + "\"; filename=\"" + encode(filename) + "\"");
        writeln(out, "Content-Type: " + mimetype);
        out.write(CRLF);
        byte[] buf = new byte[1024 * 100];
        int read;
        while ((read = in.read(buf)) > 0) {
            out.write(buf, 0, read);
        }
        out.write(CRLF);
        in.close();
    }
