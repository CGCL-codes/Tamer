    @Override
    public Channel getChannel(Nucleotide nucleotide) {
        if (nucleotide == Nucleotide.Adenine) {
            return getAChannel();
        }
        if (nucleotide == Nucleotide.Cytosine) {
            return getCChannel();
        }
        if (nucleotide == Nucleotide.Guanine) {
            return getGChannel();
        }
        return getTChannel();
    }
