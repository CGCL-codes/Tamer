    private void moveNode(MTreeNode movingNode, MTreeNode toNode) {
        log.info(movingNode.toString() + " to " + toNode.toString());
        if (movingNode == toNode) return;
        MTreeNode oldParent = (MTreeNode) movingNode.getParent();
        movingNode.removeFromParent();
        treeModel.nodeStructureChanged(oldParent);
        MTreeNode newParent;
        int index;
        if (!toNode.isSummary()) {
            newParent = (MTreeNode) toNode.getParent();
            index = newParent.getIndex(toNode) + 1;
        } else {
            newParent = toNode;
            index = 0;
        }
        newParent.insert(movingNode, index);
        treeModel.nodeStructureChanged(newParent);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Trx trx = Trx.get(Trx.createTrxName("VTreePanel"), true);
        try {
            int no = 0;
            for (int i = 0; i < oldParent.getChildCount(); i++) {
                MTreeNode nd = (MTreeNode) oldParent.getChildAt(i);
                StringBuffer sql = new StringBuffer("UPDATE ");
                sql.append(m_nodeTableName).append(" SET Parent_ID=").append(oldParent.getNode_ID()).append(", SeqNo=").append(i).append(", Updated=SysDate").append(" WHERE AD_Tree_ID=").append(m_AD_Tree_ID).append(" AND Node_ID=").append(nd.getNode_ID());
                log.fine(sql.toString());
                no = DB.executeUpdate(sql.toString(), trx.getTrxName());
            }
            if (oldParent != newParent) for (int i = 0; i < newParent.getChildCount(); i++) {
                MTreeNode nd = (MTreeNode) newParent.getChildAt(i);
                StringBuffer sql = new StringBuffer("UPDATE ");
                sql.append(m_nodeTableName).append(" SET Parent_ID=").append(newParent.getNode_ID()).append(", SeqNo=").append(i).append(", Updated=SysDate").append(" WHERE AD_Tree_ID=").append(m_AD_Tree_ID).append(" AND Node_ID=").append(nd.getNode_ID());
                log.fine(sql.toString());
                no = DB.executeUpdate(sql.toString(), trx.getTrxName());
            }
            trx.commit(true);
        } catch (Exception e) {
            trx.rollback();
            log.log(Level.SEVERE, "move", e);
            ADialog.error(m_WindowNo, this, "TreeUpdateError", e.getLocalizedMessage());
        }
        trx.close();
        trx = null;
        setCursor(Cursor.getDefaultCursor());
        log.config("complete");
    }
