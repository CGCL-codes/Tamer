    protected String slurp(final File lsb) {
        if (lsb != null && lsb.exists()) {
            FileInputStream stream = null;
            try {
                stream = new FileInputStream(lsb);
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                return Charset.defaultCharset().decode(bb).toString().replace("[\\r\\n]*$", "");
            } catch (final Exception e) {
                LogUtils.debugf(this, e, "Unable to read from file '%s'", lsb.getPath());
            } finally {
                IOUtils.closeQuietly(stream);
            }
        }
        return null;
    }
