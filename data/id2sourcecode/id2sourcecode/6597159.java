    public static final File unGZip(final GZIPInputStream in, final File outFile) {
        BufferedOutputStream out = null;
        try {
            final byte data[] = new byte[10240];
            out = new BufferedOutputStream(new FileOutputStream(outFile));
            int count;
            while ((count = in.read(data)) != -1) out.write(data, 0, count);
            out.flush();
            return outFile;
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeIO(in);
        }
    }
