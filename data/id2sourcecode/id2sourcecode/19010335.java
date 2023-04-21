    private void showPublishJsp(int[] docIds, HttpServletRequest request, User user) throws Exception {
        request.setAttribute("operate", DocPublishAction.OPERATE_PUBLISH);
        request.setAttribute("docID", Function.arrayToStr(docIds));
        DocumentCBF[] docs = new DocumentCBF[docIds.length];
        for (int i = 0; i < docs.length; i++) {
            docs[i] = DocumentCBF.getInstance(docIds[i]);
            if (docs[i] == null) {
                throw new Exception("���ܵõ��ĵ����Ϊ" + docIds[i] + "��ʵ��!��������Ƿ���ȷ!");
            }
        }
        request.setAttribute("docs", docs);
        DocumentCBF doc = docs[0];
        String[] defaultSelections = null;
        if (doc.getChannelPath() != null && !doc.getChannelPath().trim().equals("")) {
            defaultSelections = new String[1];
            defaultSelections[0] = doc.getChannelPath();
            String[] a = new ChannelDAO().getRelatingChannel(doc.getChannelPath(), Channel.DATA_RELATIVE);
            if (a != null) {
                String[] b = new String[a.length + 1];
                for (int i = 0; i < a.length; i++) {
                    b[i] = a[i];
                }
                b[a.length] = doc.getChannelPath();
                defaultSelections = b;
            }
        } else {
            defaultSelections = SiteChannelDocTypeRelation.getSiteChannelPaths(doc.getDoctypePath(), true);
        }
        TreeNode[] tns = null;
        if (ConfigInfo.getInstance().getChannelAuthority().equals("1")) {
            if (user.getUserID() == 1 || user.getFlagSA().trim().equals("1")) {
                tns = TreeNode.getSiteChannelTree();
            } else {
                tns = TreeNode.getSiteChannelTree(user.getUserID(), "8");
            }
        } else {
            tns = TreeNode.getSiteChannelTree();
        }
        if (tns == null) {
            tns = new TreeNode[0];
        }
        String[] links = Function.newStringArray(tns.length, "");
        String[] targets = Function.newStringArray(tns.length, "");
        TreeNode root = TreeNode.getSiteChannelTreeRoot();
        String rootLink = "";
        String rootTarget = "";
        String tempPaths = "";
        int i = 0;
        String tempPath;
        String selfSitePath = doc.getChannelPath().substring(0, 10);
        for (; i < tns.length; i++) {
            tempPath = tns[i].getPath();
            if (tempPath != null && tempPath.length() == 10) {
                tempPaths = tempPaths + tempPath + ",";
            } else if (ConfigInfo.getInstance().getChannelAuthority().equals("2") && tempPath != null && !tempPath.startsWith(selfSitePath)) {
                tempPaths = tempPaths + tempPath + ",";
            }
        }
        String[] disabledPaths = Function.stringToArray(tempPaths);
        String siteChannelTreeCode = getTreeJsCode(TreeNode.SITE_CHANNEL_TREE, root, rootLink, rootTarget, tns, links, targets, defaultSelections, disabledPaths, user);
        request.setAttribute("siteChannelTreeCode", siteChannelTreeCode);
    }
