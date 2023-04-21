    public static void writeFile(final File ao_file, final byte[] ah_data, final boolean ab_append) throws IOException, FileNotFoundException {
        if (ao_file.exists() && ao_file.isFile()) {
            final FileOutputStream lo_output = new FileOutputStream(ao_file, ab_append);
            lo_output.getChannel().write(ByteBuffer.wrap(ah_data));
            lo_output.getChannel().close();
        }
    }
