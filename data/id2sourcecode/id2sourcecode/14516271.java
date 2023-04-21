    public static void writeInputStreamToOutputStream(InputStream in, OutputStream out, int totalSize, IProgressMonitor progressMonitor) throws IOException {
        try {
            BufferedInputStream bis = new BufferedInputStream(in);
            try {
                byte buf[] = new byte[1024 * 50];
                int totalRead = 0;
                int read = -1;
                while ((read = bis.read(buf, 0, buf.length)) != -1) {
                    out.write(buf, 0, read);
                    totalRead += read;
                    progressMonitor.progress(totalRead, totalSize);
                }
            } finally {
                bis.close();
            }
            out.flush();
        } finally {
            out.close();
        }
        progressMonitor.done();
    }
