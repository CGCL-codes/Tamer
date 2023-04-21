    protected void transform(final ARCReader reader, final File warc) throws IOException {
        ExperimentalWARCWriter writer = null;
        reader.setDigest(false);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(warc));
            final Iterator<ArchiveRecord> i = reader.iterator();
            ARCRecord firstRecord = (ARCRecord) i.next();
            ByteArrayOutputStream baos = new ByteArrayOutputStream((int) firstRecord.getHeader().getLength());
            firstRecord.dump(baos);
            ANVLRecord ar = new ANVLRecord(1);
            ar.addLabelValue("Filedesc", baos.toString());
            List<String> metadata = new ArrayList<String>(1);
            metadata.add(ar.toString());
            writer = new ExperimentalWARCWriter(null, bos, warc, reader.isCompressed(), null, metadata);
            writer.writeWarcinfoRecord(warc.getName(), "Made from " + reader.getReaderIdentifier() + " by " + this.getClass().getName() + "/" + getRevision());
            for (; i.hasNext(); ) {
                write(writer, (ARCRecord) i.next());
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                Logger l = Logger.getLogger(writer.getClass().getName());
                Level oldLevel = l.getLevel();
                l.setLevel(Level.WARNING);
                try {
                    writer.close();
                } finally {
                    l.setLevel(oldLevel);
                }
            }
        }
    }
