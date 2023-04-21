    public void dump(final boolean compress) throws IOException, java.text.ParseException {
        setDigest(false);
        boolean firstRecord = true;
        ARCWriter writer = null;
        for (Iterator<ArchiveRecord> ii = iterator(); ii.hasNext(); ) {
            ARCRecord r = (ARCRecord) ii.next();
            ARCRecordMetaData meta = r.getMetaData();
            if (firstRecord) {
                firstRecord = false;
                ByteArrayOutputStream baos = new ByteArrayOutputStream(r.available());
                while (r.available() > 0) {
                    baos.write(r.read());
                }
                List<String> listOfMetadata = new ArrayList<String>();
                listOfMetadata.add(baos.toString(WriterPoolMember.UTF8));
                writer = new ARCWriter(new AtomicInteger(), System.out, new File(meta.getArc()), compress, meta.getDate(), listOfMetadata);
                continue;
            }
            writer.write(meta.getUrl(), meta.getMimetype(), meta.getIp(), ArchiveUtils.parse14DigitDate(meta.getDate()).getTime(), (int) meta.getLength(), r);
        }
    }
