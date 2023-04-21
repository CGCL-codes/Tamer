    private void appendFile(OutputStream out, String source, byte[] buffer, BeneratorContext context) throws FileNotFoundException, IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(context.resolveRelativeUri(source));
            int partLength = 0;
            while ((partLength = in.read(buffer)) > 0) out.write(buffer, 0, partLength);
        } finally {
            IOUtil.close(in);
        }
    }
