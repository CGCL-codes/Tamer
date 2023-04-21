    public TimeSeriesTile(final Coverage coverage, Envelope envelope, double[] step, final int varyingDimension) throws IOException, TransformException {
        if (envelope == null) {
            envelope = coverage.getEnvelope();
        }
        envelope = this.envelope = new GeneralEnvelope(envelope);
        this.coverage = coverage;
        this.step = step = step.clone();
        this.varyingDimension = varyingDimension;
        final int dimension = coverage.getCoordinateReferenceSystem().getCoordinateSystem().getDimension();
        if (envelope.getDimension() != dimension) {
            throw new MismatchedDimensionException(Errors.format(ErrorKeys.MISMATCHED_DIMENSION_$3, "envelope", envelope.getDimension(), dimension));
        }
        if (step.length != dimension) {
            throw new MismatchedDimensionException(Errors.format(ErrorKeys.MISMATCHED_DIMENSION_$3, "step", step.length, dimension));
        }
        if (varyingDimension < 0 || varyingDimension >= dimension) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "varyingDimension", varyingDimension));
        }
        int nSeries = 1;
        size = new int[dimension];
        for (int i = 0; i < dimension; i++) {
            final double s = step[i];
            final int n = size[i] = (int) Math.floor(envelope.getLength(i) / s + EPS);
            final double min = envelope.getMinimum(i);
            this.envelope.setRange(i, min, min + n * s);
            if (i != varyingDimension) {
                nSeries *= n;
            }
        }
        layer = new TimeSeries[nSeries];
        file = File.createTempFile("TimeSeries", ".raw");
        file.deleteOnExit();
        channel = new RandomAccessFile(file, "rw").getChannel();
        final GeneralGridRange gridRange = new GeneralGridRange(new int[size.length], size);
        final GeneralGridGeometry gridGeometry = new GeneralGridGeometry(gridRange, envelope);
        gridToCRS = gridGeometry.getGridToCRS();
        final int[] index = new int[dimension];
        int count = 0;
        loop: while (true) {
            final GeneralDirectPosition position = new GeneralDirectPosition(dimension);
            for (int i = 0; i < dimension; i++) {
                position.ordinates[i] = index[i];
            }
            final DirectPosition pos = gridToCRS.transform(position, position);
            layer[count] = new TimeSeries(this, pos, count);
            count++;
            for (int d = dimension; --d >= 0; ) {
                if (d != varyingDimension) {
                    if (++index[d] < size[d]) {
                        continue loop;
                    }
                    index[d] = 0;
                }
            }
            break;
        }
        assert count == nSeries : count;
    }
