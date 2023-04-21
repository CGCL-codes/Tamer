    private static void writeContentDispositionFile(final DataOutputStream out, final String name, final InputStream gpxInputStream) throws IOException {
        out.writeBytes("--" + BOUNDARY + LINE_END);
        out.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"" + LINE_END);
        out.writeBytes("Content-Type: application/octet-stream" + LINE_END);
        out.writeBytes(LINE_END);
        final byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        int sumread = 0;
        final InputStream in = new BufferedInputStream(gpxInputStream);
        while ((read = in.read(buffer)) >= 0) {
            out.write(buffer, 0, read);
            out.flush();
            sumread += read;
        }
        in.close();
        out.writeBytes(LINE_END);
    }
