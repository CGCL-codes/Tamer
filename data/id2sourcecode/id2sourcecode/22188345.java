    public InkTracePoint transform(InkTracePoint sourcePoint, InkTraceFormat sourceFormat, InkTraceFormat targetFormat) throws InkMLComplianceException {
        double[][] src = new double[1][sourceFormat.getChannelCount()];
        int counter = 0;
        for (InkChannel channel : sourceFormat.getChannels()) {
            src[0][counter++] = sourcePoint.get(channel.getName());
        }
        double[][] trgt = new double[1][targetFormat.getChannelCount()];
        transform(src, trgt, sourceFormat, targetFormat);
        return new ArrayTracePoint(trgt[0], targetFormat);
    }
