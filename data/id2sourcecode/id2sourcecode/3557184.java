    public void transform(final File warc, final File dir, final String prefix, final String suffix, final boolean force) throws IOException, java.text.ParseException {
        FileUtils.isReadable(warc);
        FileUtils.isReadable(dir);
        WARCReader reader = WARCReaderFactory.get(warc);
        List<String> metadata = new ArrayList<String>();
        metadata.add("Made from " + reader.getReaderIdentifier() + " by " + this.getClass().getName() + "/" + getRevision());
        ARCWriter writer = new ARCWriter(new AtomicInteger(), Arrays.asList(new File[] { dir }), prefix, suffix, reader.isCompressed(), -1, metadata);
        transform(reader, writer);
    }
