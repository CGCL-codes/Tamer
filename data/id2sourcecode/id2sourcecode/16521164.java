    private void resolveFormat(ChannelRecord record) throws IOException, UnsupportedFormatException {
        final ChannelIF channel = record.getChannel();
        InputStream in = getInputStream(channel, "Detecting format");
        if (in != null) {
            try {
                channel.setFormat(FormatDetector.getFormat(in));
                record.setFormatResolved(true);
            } catch (EOFException e) {
            }
        }
    }
