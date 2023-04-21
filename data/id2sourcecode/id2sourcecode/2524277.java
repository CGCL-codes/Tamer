    public int compare(java.lang.Object obj, java.lang.Object obj1) {
        int ret = 0;
        SQLProgItem lProgItem = (SQLProgItem) obj;
        SQLProgItem rProgItem = (SQLProgItem) obj1;
        if (lProgItem != null && rProgItem != null) {
            ret = lProgItem.Start.compareTo(rProgItem.Start);
            if (ret == 0) {
                String lChannelDesc = Programs.getChannelDesc(lProgItem.Channel);
                String rChannelDesc = Programs.getChannelDesc(rProgItem.Channel);
                int lEndNum = lChannelDesc.indexOf(' ');
                int rEndNum = rChannelDesc.indexOf(' ');
                if (lEndNum > 0 && rEndNum > 0) {
                    String lChannelNum = lChannelDesc.substring(0, lEndNum);
                    String rChannelNum = rChannelDesc.substring(0, rEndNum);
                    try {
                        ret = Integer.valueOf(lChannelNum).compareTo(Integer.valueOf(rChannelNum));
                    } catch (NumberFormatException e) {
                        ret = lChannelNum.compareTo(rChannelNum);
                    }
                    if (ret == 0) {
                        ret = lChannelDesc.substring(lEndNum).compareTo(rChannelDesc.substring(rEndNum));
                    }
                } else ret = lChannelDesc.compareToIgnoreCase(rChannelDesc);
            }
        }
        return ret;
    }
