    public static void writeStreamToFile(InputStream srcStream, File destFile) throws IOException {
        OutputStream output;
        InputStream input = null;
        output = null;
        try {
            input = new BufferedInputStream(srcStream);
            output = new BufferedOutputStream(new FileOutputStream(destFile));
            int ch;
            while ((ch = input.read()) != -1) output.write(ch);
        } catch (IOException e) {
            log.error("Error writing stream to file: " + destFile.getAbsolutePath());
            throw e;
        } finally {
            input.close();
        }
        output.close();
    }
