    public void exportToDir(final TVData data, final File dir, final TimeZone tz) throws IOException {
        final DivideIterator itdivide = new DivideIterator(tz, System.currentTimeMillis() - (MSEC_PER_DAY * 2));
        data.iterate(itdivide);
        for (final Map.Entry<String, TVData> entry : itdivide.filesData.entrySet()) {
            final String fileName = entry.getKey();
            final TVData dayData = (TVData) entry.getValue();
            exportOneDay(new File(dir, fileName), dayData, tz);
        }
        final DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(dir, FILE_LIST))));
        try {
            dout.writeShort(version);
            final String[] channelIDs = data.getChannelIDs();
            dout.writeShort(channelIDs.length);
            for (int i = 0; i < channelIDs.length; i++) {
                dout.writeUTF(channelIDs[i]);
                final TVChannel ch = data.get(channelIDs[i]);
                if ((ch != null) && (ch.getDisplayName() != null)) {
                    dout.writeUTF(ch.getDisplayName());
                } else {
                    dout.writeUTF(channelIDs[i]);
                }
            }
            dout.flush();
            dout.close();
        } finally {
            dout.close();
        }
    }
