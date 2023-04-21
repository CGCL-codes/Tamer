    public Channel saveData(ChannelpropForm cForm, String saveStatus, String cPath, String parentPath) throws UnsupportedEncodingException, Exception {
        Channel channel = new Channel();
        if (saveStatus.trim().equalsIgnoreCase("add")) {
            ChannelPath channelPath = new ChannelPath();
            channelPath.setCPath(parentPath);
            String treeId = channelPath.getDefaultValue();
            channel.setPath(parentPath + treeId);
            int level = parentPath.length() / 5;
            int channelId = (int) IdGenerator.getInstance().getId(IdGenerator.GEN_ID_IP_CHANNEL);
            String parentId = parentPath.substring((level - 1) * 5, parentPath.length());
            String sitePath = parentPath.substring(0, 10);
            int siteID = ((Site) TreeNode.getInstance(sitePath)).getSiteID();
            channel.setSiteId(siteID);
            channel.setId(treeId);
            channel.setLevel(level);
            channel.setChannelID(channelId);
            channel.setParentId(parentId);
            channel.setChannelType("0");
            channel.setRefPath(cForm.getRefreshFlag());
        } else {
            channel = (Channel) TreeNode.getInstance(cPath);
        }
        channel.setName(Function.convertCharset(cForm.getChannelname(), Constant.PARAM_FORMBEAN));
        channel.setTitle(Function.convertCharset(cForm.getChannelname(), Constant.PARAM_FORMBEAN));
        channel.setDescription(Function.convertCharset(cForm.getDescription(), Constant.PARAM_FORMBEAN));
        channel.setOrderNo(Integer.parseInt(cForm.getNum()));
        channel.setUseStatus(cForm.getUsestatus());
        channel.setRefresh(cForm.getRefreshFlag());
        channel.setPageNum(Integer.parseInt(cForm.getPageNum().equals("") ? "0" : cForm.getPageNum()));
        channel.setTemplateId(cForm.getTemplate_id());
        channel.setSelfDefineList(titleSelfDefine(cForm.getOrder_no(), cForm.getField(), cForm.getField_name()));
        if (saveStatus.trim().equalsIgnoreCase("add")) {
            channel.setAsciiName(cForm.getChannelpath() + "_" + channel.getChannelID());
            channel.add(this.createFolder);
        } else {
            channel.setAsciiName(cForm.getChannelpath());
            channel.setExtendParent(cForm.getExtendparent());
            channel.update();
        }
        channel = (Channel) TreeNode.getInstance(channel.getPath());
        return channel;
    }
