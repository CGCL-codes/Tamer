        public void eventValue(final ChannelRecord record, final Channel channel) {
            double value = Double.NaN;
            synchronized (CHANNEL_LOCK) {
                if (_channelSource != null && _channelSource.getChannel() == channel) {
                    value = record.doubleValue();
                }
            }
            if (!Double.isNaN(value) && !Double.isInfinite(value)) {
                final List<Double> buffer = new ArrayList<Double>();
                UnivariateStatistics stats;
                int[] counts;
                double[] range;
                synchronized (HISTOGRAM_LOCK) {
                    VALUE_BUFFER.add(value);
                    VALUE_STATS.addSample(value);
                    while (VALUE_BUFFER.size() > _bufferSize) {
                        final double oldValue = VALUE_BUFFER.removeFirst();
                        VALUE_STATS.removeSample(oldValue);
                    }
                    final List<Double> orderedList = new ArrayList<Double>(VALUE_BUFFER);
                    Collections.sort(orderedList);
                    final int valueCount = orderedList.size();
                    if (valueCount > 1) {
                        VALUE_RANGE[0] = orderedList.get(0);
                        VALUE_RANGE[1] = orderedList.get(valueCount - 1);
                    } else if (valueCount == 1) {
                        VALUE_RANGE[0] = orderedList.get(0) - 1.0;
                        VALUE_RANGE[1] = VALUE_RANGE[1] + 1.0;
                    }
                    buffer.addAll(VALUE_BUFFER);
                    stats = new UnivariateStatistics(VALUE_STATS);
                    range = getHistogramRange();
                    counts = populateHistogram(range);
                }
                EVENT_PROXY.histogramUpdated(ChannelHistogram.this, range, counts, buffer, stats);
            }
        }
