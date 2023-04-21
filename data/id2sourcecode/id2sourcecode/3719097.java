    @Override
    public void write(DataOutput out) throws IOException {
        read.write(out);
        qualities.write(out);
        chrom.write(out);
        site.write(out);
        score.write(out);
        strand.write(out);
        unique.write(out);
    }
