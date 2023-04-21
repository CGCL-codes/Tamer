    private String getChannelTreeJsCode(int treeType, TreeNode root, TreeNode[] allTreeNodes) throws Exception {
        if (root == null || allTreeNodes == null) {
            return "";
        }
        TreeJsCode tree = new TreeJsCode();
        tree.setItemType(TreeJsCode.ITEM_TYPE_RADIO);
        tree.setTreeBehavior(TreeJsCode.TREE_BEHAVIOR_CLASSIC);
        tree.setTreeType(treeType);
        tree.setTreeNodeTitleDecorate(DefaultTreeNodeTitleDecorate.getInstance());
        tree.setRoot(root);
        tree.setTreeNodeList(allTreeNodes);
        return tree.getCode();
    }
