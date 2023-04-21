    public String getChannelTreeJsCode() {
        String siteChannelTree = null;
        try {
            TreeNode[] siteChannelNodeList = TreeNode.getSiteChannelTree();
            if (siteChannelNodeList == null) {
                siteChannelNodeList = new TreeNode[0];
            }
            TreeNode root = TreeNode.getSiteChannelTreeRoot();
            String rootLink = "";
            String rootTarget = "";
            siteChannelTree = this.getChannelTreeJsCode(TreeJsCode.SITE_CHANNEL_TREE, root, siteChannelNodeList);
        } catch (Exception ex) {
        }
        return siteChannelTree;
    }
