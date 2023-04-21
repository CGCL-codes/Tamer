    private void postDataTimeSeries(ChannelMap channelMap, String channelName, int channelIndex) {
        TimeSeriesCollection dataCollection = (TimeSeriesCollection) this.dataCollection;
        FastTimeSeries timeSeriesData = (FastTimeSeries) dataCollection.getSeries(getChannelDisplay(channelName));
        if (timeSeriesData == null) {
            log.error("We don't have a data collection to post this data.");
            return;
        }
        try {
            double[] times = channelMap.GetTimes(channelIndex);
            int typeID = channelMap.GetType(channelIndex);
            FixedMillisecond time;
            chart.setNotify(false);
            timeSeriesData.startAdd(times.length);
            switch(typeID) {
                case ChannelMap.TYPE_FLOAT64:
                    double[] doubleData = channelMap.GetDataAsFloat64(channelIndex);
                    for (int i = 0; i < doubleData.length; i++) {
                        time = new FixedMillisecond((long) (times[i] * 1000));
                        timeSeriesData.add(time, doubleData[i]);
                    }
                    break;
                case ChannelMap.TYPE_FLOAT32:
                    float[] floatData = channelMap.GetDataAsFloat32(channelIndex);
                    for (int i = 0; i < floatData.length; i++) {
                        time = new FixedMillisecond((long) (times[i] * 1000));
                        timeSeriesData.add(time, floatData[i]);
                    }
                    break;
                case ChannelMap.TYPE_INT64:
                    long[] longData = channelMap.GetDataAsInt64(channelIndex);
                    for (int i = 0; i < longData.length; i++) {
                        time = new FixedMillisecond((long) (times[i] * 1000));
                        timeSeriesData.add(time, longData[i]);
                    }
                    break;
                case ChannelMap.TYPE_INT32:
                    int[] intData = channelMap.GetDataAsInt32(channelIndex);
                    for (int i = 0; i < intData.length; i++) {
                        time = new FixedMillisecond((long) (times[i] * 1000));
                        timeSeriesData.add(time, intData[i]);
                    }
                    break;
                case ChannelMap.TYPE_INT16:
                    short[] shortData = channelMap.GetDataAsInt16(channelIndex);
                    for (int i = 0; i < shortData.length; i++) {
                        time = new FixedMillisecond((long) (times[i] * 1000));
                        timeSeriesData.add(time, shortData[i]);
                    }
                    break;
                case ChannelMap.TYPE_INT8:
                    byte[] byteData = channelMap.GetDataAsInt8(channelIndex);
                    for (int i = 0; i < byteData.length; i++) {
                        time = new FixedMillisecond((long) (times[i] * 1000));
                        timeSeriesData.add(time, byteData[i]);
                    }
                    break;
                case ChannelMap.TYPE_STRING:
                case ChannelMap.TYPE_UNKNOWN:
                case ChannelMap.TYPE_BYTEARRAY:
                    log.error("Got byte array type for channel " + channelName + ". Don't know how to handle.");
                    break;
            }
            timeSeriesData.fireSeriesChanged();
            chart.setNotify(true);
            chart.fireChartChanged();
        } catch (Exception e) {
            log.error("Problem plotting data for channel " + channelName + ".");
            e.printStackTrace();
        }
    }
