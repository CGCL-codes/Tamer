    public String exportDataASCII() {
        double recs = 0;
        DataGroup group = null;
        DataChannel channel = null;
        StringBuffer datOut = new StringBuffer();
        java.text.DecimalFormat datFormat = new java.text.DecimalFormat(format);
        try {
            datOut.append("# ");
            for (int i = 0; i < getGroupsSize(); i++) {
                for (int j = 0; j < getChannelsSize(i); j++) {
                    channel = getChannel(i, j);
                    if (channel.getAttribute().isNormal()) {
                        recs = Math.max(recs, channel.size());
                        datOut.append(channel.getName() + delimiter);
                    }
                }
            }
            datOut.append('\n');
            for (int k = 0; k < recs; k++) {
                for (int i = 0; i < getGroupsSize(); i++) {
                    group = getGroup(i);
                    for (int j = 0; j < getChannelsSize(i); j++) {
                        channel = getChannel(i, j);
                        if (channel.getAttribute().isNormal()) {
                            if (k < channel.size()) {
                                datOut.append(datFormat.format(channel.getData(k)) + delimiter);
                            } else {
                                datOut.append(" " + delimiter);
                            }
                        }
                    }
                }
                datOut.append('\n');
            }
        } catch (Exception e) {
        }
        return datOut.toString();
    }
