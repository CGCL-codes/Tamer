    private void moveNode(SimpleTreeNode movingNode, SimpleTreeNode toNode, boolean moveInto) {
        SimpleTreeNode newParent;
        int index;
        SimpleTreeNode oldParent = treeModel.getParent(movingNode);
        treeModel.removeNode(movingNode);
        if (!moveInto) {
            newParent = treeModel.getParent(toNode);
            index = newParent.getChildren().indexOf(toNode) + 1;
        } else {
            newParent = toNode;
            index = 0;
        }
        treeModel.addNode(newParent, movingNode, index);
        int path[] = treeModel.getPath(treeModel.getRoot(), movingNode);
        Treeitem movingItem = tree.renderItemByPath(path);
        tree.setSelectedItem(movingItem);
        Events.sendEvent(tree, new Event(Events.ON_SELECT, tree));
        Trx trx = org.compierezk.util.Trx.get("ADTree");
        try {
            CTreeNode oldMParent = (CTreeNode) oldParent.getData();
            for (int i = 0; i < oldParent.getChildCount(); i++) {
                SimpleTreeNode nd = (SimpleTreeNode) oldParent.getChildAt(i);
                CTreeNode md = (CTreeNode) nd.getData();
                StringBuffer sql = new StringBuffer("UPDATE ");
                sql.append(mTree.getNodeTableName()).append(" SET Parent_ID=").append(oldMParent.getNode_ID()).append(", SeqNo=").append(i).append(", Updated=SysDate").append(" WHERE AD_Tree_ID=").append(mTree.getAD_Tree_ID()).append(" AND Node_ID=").append(md.getNode_ID());
                log.fine(sql.toString());
                org.compierezk.util.DB.executeUpdate(sql.toString(), trx);
            }
            if (oldParent != newParent) {
                CTreeNode newMParent = (CTreeNode) newParent.getData();
                for (int i = 0; i < newParent.getChildCount(); i++) {
                    SimpleTreeNode nd = (SimpleTreeNode) newParent.getChildAt(i);
                    CTreeNode md = (CTreeNode) nd.getData();
                    StringBuffer sql = new StringBuffer("UPDATE ");
                    sql.append(mTree.getNodeTableName()).append(" SET Parent_ID=").append(newMParent.getNode_ID()).append(", SeqNo=").append(i).append(", Updated=SysDate").append(" WHERE AD_Tree_ID=").append(mTree.getAD_Tree_ID()).append(" AND Node_ID=").append(md.getNode_ID());
                    log.fine(sql.toString());
                    org.compierezk.util.DB.executeUpdate(sql.toString(), trx);
                }
            }
            trx.commit();
        } catch (Exception e) {
            trx.rollback();
            FDialog.error(windowNo, tree, "TreeUpdateError", e.getLocalizedMessage());
        }
        trx.close();
        trx = null;
    }
