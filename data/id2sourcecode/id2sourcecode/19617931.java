    public void assignAllChildren(Pepodom[] ps1, String treePath) throws Exception {
        Pepodom[] pepodoms = new Pepodom[ps1.length];
        for (int i = 0; i < ps1.length; i++) {
            pepodoms[i] = ps1[i];
        }
        TreeNode[] tns = null;
        int len = treePath.length();
        if (len > 10) {
            Channel channel = new Channel();
            channel.setPath(treePath);
            tns = channel.getList();
        } else {
            Site site = new Site();
            site.setPath(treePath);
            tns = site.getList();
        }
        try {
            if (tns != null) {
                for (int i = 0; i < tns.length; i++) {
                    String resID = Integer.toString(Const.CHANNEL_TYPE_RES + ((Channel) tns[i]).getChannelID());
                    String resName = tns[i].getTitle();
                    AuthorityManager am = new AuthorityManager();
                    boolean hasResource = am.hasSysResource(resID, Const.OPERATE_TYPE_ID);
                    if (!hasResource) {
                        int operateTypeID = Const.OPERATE_TYPE_ID;
                        int resTypeID = Const.RES_TYPE_ID;
                        String remark = "";
                        am.createExtResource(resID, resName, resTypeID, operateTypeID, remark);
                    }
                    Pepodom[] ps = Pepodom.getExtPepodom(resID);
                    for (int j = 0; j < pepodoms.length; j++) {
                        int m = 0;
                        for (m = 0; m < ps.length; m++) {
                            if (ps[m].getUserID() == pepodoms[j].getUserID()) {
                                pepodoms[j].setAuthorityID(ps[m].getAuthorityID());
                                pepodoms[j].setResID(resID);
                                break;
                            }
                        }
                        if (m == ps.length) {
                            pepodoms[j].setAuthorityID(Const.AUTHORITY_ID);
                            pepodoms[j].setResID(resID);
                        }
                    }
                    Pepodom.assignExtPepodom(pepodoms, tns[i].getPath(), true);
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
