    private static void doCopyFile(File source, File target) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(target);
            in.getChannel().transferTo(0, Long.MAX_VALUE, out.getChannel());
        } finally {
            IoUtil.closeQuietly(in, out);
        }
    }
