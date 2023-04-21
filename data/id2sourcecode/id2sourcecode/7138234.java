    public void setup(SeqWareSettings settings) throws FileNotFoundException, DatabaseException, Exception {
        super.setup(settings);
        variantWriter = new BufferedWriter(new FileWriter(new File("/tmp/variant.sql")));
        variantWriter.write("COPY variant (variant_id, type, contig, start, stop, referencebase, consensusbase, calledbase, consensuscallquality, readcount, keyvalues) FROM stdin;");
        variantWriter.newLine();
        tagWriter = new BufferedWriter(new FileWriter(new File("/tmp/tag.sql")));
        tagWriter.write("COPY tag (tag_id, key, value) FROM stdin;");
        tagWriter.newLine();
        variantTagWriter = new BufferedWriter(new FileWriter(new File("/tmp/variant_tag.sql")));
        variantTagWriter.write("COPY variant_tag (variant_tag_id, variant_id, tag_id) FROM stdin;");
        variantTagWriter.newLine();
    }
