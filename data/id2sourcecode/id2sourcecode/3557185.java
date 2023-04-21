    protected void transform(final WARCReader reader, final ARCWriter writer) throws IOException, java.text.ParseException {
        reader.setDigest(false);
        Logger l = Logger.getLogger(writer.getClass().getName());
        Level oldLevel = l.getLevel();
        try {
            l.setLevel(Level.WARNING);
            for (final Iterator i = reader.iterator(); i.hasNext(); ) {
                WARCRecord r = (WARCRecord) i.next();
                if (!isARCType(r.getHeader().getMimetype())) {
                    continue;
                }
                if (r.getHeader().getContentBegin() <= 0) {
                    continue;
                }
                String ip = (String) r.getHeader().getHeaderValue((WARCConstants.HEADER_KEY_IP));
                long length = r.getHeader().getLength();
                int offset = r.getHeader().getContentBegin();
                String mimetype = r.getHeader().getMimetype();
                String t = r.getHeader().getDate().replaceAll("[-T:Z]", "");
                long time = ArchiveUtils.getSecondsSinceEpoch(t).getTime();
                writer.write(r.getHeader().getUrl(), mimetype, ip, time, (int) (length - offset), r);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                try {
                    writer.close();
                } finally {
                    l.setLevel(oldLevel);
                }
            }
        }
    }
