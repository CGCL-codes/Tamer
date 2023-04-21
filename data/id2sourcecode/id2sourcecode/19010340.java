    private String getTreeJsCode(int treeType, TreeNode root, String rootLink, String rootTarget, TreeNode[] allTreeNodes, String[] linkList, String[] targetList, String[] defaultSelectionPaths, String[] disabledPaths, User user) throws Exception {
        if (root == null || allTreeNodes == null) {
            return "";
        }
        TreeJsCode tree = new TreeJsCode();
        tree.setItemType(TreeJsCode.ITEM_TYPE_CHECKBOX);
        tree.setTreeBehavior(TreeJsCode.TREE_BEHAVIOR_CLASSIC);
        tree.setDisplayCheckedNodes(true);
        if (ConfigInfo.getInstance().getChannelAuthority().equals("2")) {
            tree.setDisplayCheckedNodes(false);
        }
        tree.setRecursionChecked(false);
        tree.setTreeType(treeType);
        if (ConfigInfo.getInstance().getChannelAuthority().equals("1")) {
            if (user.getUserID() == 1 || user.getFlagSA().equals("1")) {
                tree.setTreeNodeAuthority(null);
            } else {
                tree.setTreeNodeAuthority(PublishTreeAuthority.getInstance());
            }
        } else {
            tree.setTreeNodeAuthority(null);
        }
        String[] operates = new String[1];
        operates[0] = Const.OPERATE_ID_RELEASE;
        tree.setOperates(operates);
        tree.setUserId(user.getUserID());
        tree.setTreeNodeTitleDecorate(DefaultTreeNodeTitleDecorate.getInstance());
        tree.setRoot(root);
        tree.setRootHyperlink(rootLink);
        tree.setRootTarget(rootTarget);
        tree.setTreeNodeList(allTreeNodes);
        tree.setHyperlinkList(linkList);
        tree.setTargetList(targetList);
        tree.setDefualtSelectedTreeNodePathList(defaultSelectionPaths);
        tree.setDisabledTreeNodePathList(disabledPaths);
        tree.setParentChild(false);
        return tree.getCode();
    }
