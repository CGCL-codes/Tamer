    @Override
    public String toXMLString(int base_space) {
        String str = new String();
        str += (MyUTIL.whiteSpaceStr(base_space) + "<CVCT>\n");
        str += (MyUTIL.whiteSpaceStr(base_space + 2) + "<table_id>" + TableID.CABLE_VIRTUAL_CHANNEL_TABLE.getValue() + "</table_id>\n");
        str += (MyUTIL.whiteSpaceStr(base_space + 2) + "<transport_stream_id>" + transport_stream_id + "</transport_stream_id>\n");
        str += (MyUTIL.whiteSpaceStr(base_space + 2) + "<version_number>" + version_number + "</version_number>\n");
        if (getNumChannels() > 0) {
            str += (MyUTIL.whiteSpaceStr(base_space + 2) + "<CVCTChannelLoop>\n");
            Iterator<CVCTChannel> it = getChannels();
            while (it.hasNext()) str += it.next().toXMLString(base_space + 4);
            str += (MyUTIL.whiteSpaceStr(base_space + 2) + "</CVCTChannelLoop>\n");
        }
        if (getDescriptorSize() > 0) {
            Iterator<Descriptor> it = getDescriptors();
            str += (MyUTIL.whiteSpaceStr(base_space + 2) + "<DescriptorLoop>\n");
            while (it.hasNext()) str += it.next().toXMLString(base_space + 4);
            str += (MyUTIL.whiteSpaceStr(base_space + 2) + "</DescriptorLoop>\n");
        }
        str += (MyUTIL.whiteSpaceStr(base_space) + "</CVCT>\n");
        return str;
    }
