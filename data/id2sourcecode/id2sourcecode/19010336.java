    private void showTransferJsp(int[] docIds, HttpServletRequest request, User user) throws Exception {
        String transferType = request.getParameter("transferType");
        if (transferType == null || transferType.trim().equalsIgnoreCase("copy")) {
            transferType = OPERATE_COPY_TRANSFER_PUBLISH;
        } else {
            transferType = OPERATE_MOVE_TRANSFER_PUBLISH;
        }
        request.setAttribute("operate", transferType);
        String channelPath = String.valueOf(request.getParameter("selfChannelPath"));
        request.setAttribute("selfChannelPath", request.getParameter("selfChannelPath"));
        request.setAttribute("docID", Function.arrayToStr(docIds));
        DocumentCBF[] docs = new DocumentCBF[docIds.length];
        for (int i = 0; i < docs.length; i++) {
            docs[i] = DocumentCBF.getInstance(docIds[i]);
            if (docs[i] == null) {
                throw new Exception("���ܵõ��ĵ����Ϊ" + docIds[i] + "��ʵ��!��������Ƿ���ȷ!");
            }
        }
        request.setAttribute("docs", docs);
        String[] published = Documents.getPublishedChannelPaths(docs);
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
        String tempPaths = "";
        String tempPath = "";
        String selfSitePath = channelPath.substring(0, 10);
        for (int i = 0; i < tns.length; i++) {
            tempPath = tns[i].getPath();
            if (tempPath != null && tempPath.length() == 10) {
                tempPaths = tempPaths + tempPath + ",";
            } else if (ConfigInfo.getInstance().getChannelAuthority().equals("2") && tempPath != null && !tempPath.startsWith(selfSitePath)) {
                tempPaths = tempPaths + tempPath + ",";
            }
        }
        String[] sites = Function.stringToArray(tempPaths);
        int newLongth = published.length + sites.length;
        String[] disabledPaths = new String[newLongth];
        for (int j = 0; j < disabledPaths.length; j++) {
            if (j < published.length) {
                disabledPaths[j] = published[j];
            } else {
                disabledPaths[j] = sites[j - published.length];
            }
        }
        String[] links = Function.newStringArray(tns.length, "");
        String[] targets = Function.newStringArray(tns.length, "");
        TreeNode root = TreeNode.getSiteChannelTreeRoot();
        String rootLink = "";
        String rootTarget = "";
        String siteChannelTreeCode = getTreeJsCode(TreeNode.SITE_CHANNEL_TREE, root, rootLink, rootTarget, tns, links, targets, new String[0], disabledPaths, user);
        request.setAttribute("siteChannelTreeCode", siteChannelTreeCode);
    }
