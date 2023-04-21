    @Override
    protected int run(CmdLineParser parser) {
        final List<String> args = parser.getRemainingArgs();
        if (args.isEmpty()) {
            System.err.println("view :: PATH not given.");
            return 3;
        }
        final String path = args.get(0);
        final List<String> regions = args.subList(1, args.size());
        final boolean headerOnly = parser.getBoolean(headerOnlyOpt);
        final SAMFileReader reader;
        try {
            final Path p = new Path(path);
            reader = new SAMFileReader(WrapSeekable.openPath(getConf(), p), WrapSeekable.openPath(getConf(), p.suffix(".bai")), false);
        } catch (Exception e) {
            System.err.printf("view :: Could not open '%s': %s\n", path, e.getMessage());
            return 4;
        }
        reader.setValidationStringency(ValidationStringency.SILENT);
        final SAMTextWriter writer = new SAMTextWriter(System.out);
        final SAMFileHeader header;
        try {
            header = reader.getFileHeader();
        } catch (SAMFormatException e) {
            System.err.printf("view :: Could not parse '%s': %s\n", path, e.getMessage());
            return 4;
        }
        if (regions.isEmpty() || headerOnly) {
            writer.setSortOrder(header.getSortOrder(), true);
            writer.setHeader(header);
            if (!headerOnly) if (!writeIterator(writer, reader.iterator(), path)) return 4;
            writer.close();
            return 0;
        }
        if (!reader.hasIndex()) {
            System.err.println("view :: Cannot output regions from BAM file lacking an index");
            return 4;
        }
        reader.enableIndexCaching(true);
        boolean errors = false;
        for (final String region : regions) {
            final StringTokenizer st = new StringTokenizer(region, ":-");
            final String refStr = st.nextToken();
            final int beg, end;
            if (st.hasMoreTokens()) {
                beg = parseCoordinate(st.nextToken());
                end = st.hasMoreTokens() ? parseCoordinate(st.nextToken()) : -1;
                if (beg < 0 || end < 0) {
                    errors = true;
                    continue;
                }
                if (end < beg) {
                    System.err.printf("view :: Invalid range, cannot end before start: '%d-%d'\n", beg, end);
                    errors = true;
                    continue;
                }
            } else beg = end = 0;
            SAMSequenceRecord ref = header.getSequence(refStr);
            if (ref == null) try {
                ref = header.getSequence(Integer.parseInt(refStr));
            } catch (NumberFormatException e) {
            }
            if (ref == null) {
                System.err.printf("view :: Not a valid sequence name or index: '%s'\n", refStr);
                errors = true;
                continue;
            }
            final SAMRecordIterator it = reader.queryOverlapping(ref.getSequenceName(), beg, end);
            if (!writeIterator(writer, it, path)) return 4;
        }
        writer.close();
        return errors ? 5 : 0;
    }
