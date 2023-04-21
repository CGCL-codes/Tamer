    private void print(Data[] d) {
        StringBuffer channelBuffer = new StringBuffer();
        for (int i = 0, l = d.length; i < l; i++) {
            ChannelInfo cInfo = d[i].getChannelInfo();
            int dataType = cInfo.getDataType();
            String valueString = "";
            switch(dataType) {
                case ChannelInfo.TYPE_DOUBLE:
                    {
                        double[] values = d[i].getDoubleValues();
                        int l2 = values.length;
                        valueString = l2 > 0 ? resolved(cInfo, values[l2 - 1]) : null;
                        break;
                    }
                case ChannelInfo.TYPE_INT:
                    {
                        int[] values = d[i].getIntValues();
                        int l2 = values.length;
                        valueString = l2 > 0 ? resolved(cInfo, values[l2 - 1]) : null;
                        break;
                    }
                case ChannelInfo.TYPE_OBJECT:
                    {
                        Object[] values = d[i].getObjectValues();
                        int l2 = values.length;
                        valueString = l2 > 0 ? values[l2 - 1].toString() : null;
                        break;
                    }
            }
            String channelString = cInfo.getName() + ": " + valueString + "\n";
            channelBuffer.append(channelString);
        }
        System.out.println(channelBuffer);
    }
