    public int compare(java.lang.Object obj, java.lang.Object obj1) {
        int ret = 0;
        Element lNode = (Element) ((NodeProgItem) obj).getItem();
        Element rNode = (Element) ((NodeProgItem) obj1).getItem();
        if (lNode != null && rNode != null) {
            ret = ((String) lNode.getAttribute(ProgramData.START)).compareTo((String) rNode.getAttribute(ProgramData.START));
            if (ret == 0) {
                String lChannel = lNode.getAttribute(ProgramData.CHANNEL);
                String lChannelDesc = Programs.getChannelDesc(lChannel);
                int lEndNum = lChannelDesc.indexOf(' ');
                String lChannelNum = lChannelDesc.substring(0, lEndNum);
                String rChannel = rNode.getAttribute(ProgramData.CHANNEL);
                String rChannelDesc = Programs.getChannelDesc(rChannel);
                int rEndNum = rChannelDesc.indexOf(' ');
                String rChannelNum = rChannelDesc.substring(0, rChannelDesc.indexOf(' '));
                ret = Integer.valueOf(lChannelNum).compareTo(Integer.valueOf(rChannelNum));
                if (ret == 0) {
                    ret = lChannelDesc.substring(lEndNum).compareTo(rChannelDesc.substring(rEndNum));
                    if (ret == 0) {
                        ret = lNode.toString().compareTo(rNode.toString());
                    }
                }
            }
        }
        return ret;
    }
