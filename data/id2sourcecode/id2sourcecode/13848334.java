    private Channel copySingle(Channel orgChannel, String aimChanPath) throws Exception {
        Channel channel = new Channel();
        ChannelPath channelPath = new ChannelPath();
        channelPath.setCPath(aimChanPath);
        String treeId = channelPath.getDefaultValue();
        channel.setPath(aimChanPath + treeId);
        int level = aimChanPath.length() / 5;
        int channelId = (int) IdGenerator.getInstance().getId(IdGenerator.GEN_ID_IP_CHANNEL);
        String parentId = aimChanPath.substring((level - 1) * 5, aimChanPath.length());
        String sitePath = aimChanPath.substring(0, 10);
        int siteID = ((Site) TreeNode.getInstance(sitePath)).getSiteID();
        channel.setSiteId(siteID);
        channel.setId(treeId);
        channel.setLevel(level);
        channel.setChannelID(channelId);
        channel.setParentId(parentId);
        channel.setChannelType(orgChannel.getChannelType());
        channel.setName(orgChannel.getName() + channel.getChannelID());
        channel.setTitle(orgChannel.getName() + channel.getChannelID());
        channel.setDescription(orgChannel.getDesc());
        channel.setTemplateId(orgChannel.getTemplateId());
        channel.setOrderNo(orgChannel.getOrderNo());
        channel.setUseStatus(orgChannel.getUseStatus());
        channel.setRefPath(REFRESH_RIGHT_NOW);
        channel.setRefresh(REFRESH_RIGHT_NOW);
        channel.setPageNum(orgChannel.getPageNum());
        String orgName = orgChannel.getAsciiName();
        if (orgName.indexOf("_") > -1) orgName = orgName.substring(0, orgName.indexOf("_"));
        channel.setAsciiName(orgName + "_" + channel.getChannelID());
        channel.setSelfDefineList(new ChannelDAO().getSelfDefine(orgChannel.getPath()));
        channel.add(true);
        channel = (Channel) TreeNode.getInstance(channel.getPath());
        HashMap result = SiteChannelDocTypeRelation.getDocTypePathsAndShowTemplateIds(orgChannel.getPath(), false);
        if (null != result) {
            Iterator iterator = result.keySet().iterator();
            String docType, tempId;
            ArrayList docTypes = new ArrayList();
            ArrayList tempIds = new ArrayList();
            while (iterator.hasNext()) {
                docType = (String) iterator.next();
                tempId = (String) result.get(docType);
                if ("null".equals(tempId)) tempId = "";
                docTypes.add(docType);
                tempIds.add(tempId);
            }
            SiteChannelDocTypeRelation.addBySiteChannelPath(channel.getPath(), (String[]) docTypes.toArray(new String[] {}), (String[]) tempIds.toArray(new String[] {}));
        }
        ConfigInfo cfg = ConfigInfo.getInstance();
        String orgFileUrl = cfg.getInfoplatDataDir() + "pub" + File.separator + orgChannel.getSiteAsciiName() + File.separator + orgChannel.getAsciiName() + File.separator;
        String aimFileUrl = cfg.getInfoplatDataDir() + "pub" + File.separator + channel.getSiteAsciiName() + File.separator + channel.getAsciiName() + File.separator;
        FileOperation.copyDir(orgFileUrl, aimFileUrl);
        BufferedReader reader;
        String line, documents;
        StringBuffer document;
        File psFile = new File(aimFileUrl + "index_ps.jsp");
        if (psFile.exists()) {
            reader = new BufferedReader(new FileReader(psFile));
            line = null;
            document = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                document.append(line + "\n");
            }
            documents = StringUtil.replaceAll(document.toString(), orgChannel.getPath(), channel.getPath());
            Function.writeTextFile(documents, aimFileUrl + "index_ps.jsp", "GBK", true);
            reader.close();
        }
        File pdFile = new File(aimFileUrl + "index_pd.jsp");
        if (pdFile.exists()) {
            reader = new BufferedReader(new FileReader(pdFile));
            line = null;
            document = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                document.append(line + "\n");
            }
            documents = StringUtil.replaceAll(document.toString(), orgChannel.getPath(), channel.getPath());
            Function.writeTextFile(documents, aimFileUrl + "index_pd.jsp", "GBK", true);
            reader.close();
        }
        return channel;
    }
