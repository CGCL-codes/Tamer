    public int remove_node(NeighborInfo ni) {
        int result = 0;
        int i;
        for (i = 0; i < leaf_pred_count; ++i) {
            if (leaf_preds[i].node_id == ni.node_id) break;
        }
        if (i != leaf_pred_count) {
            result |= REMOVED_PREDECESSOR;
            for (; i < leaf_pred_count - 1; ++i) leaf_preds[i] = leaf_preds[i + 1];
            --leaf_pred_count;
        }
        for (i = 0; i < leaf_succ_count; ++i) {
            if (leaf_succs[i].node_id == ni.node_id) break;
        }
        if (i != leaf_succ_count) {
            result |= REMOVED_SUCCESSOR;
            for (; i < leaf_succ_count - 1; ++i) leaf_succs[i] = leaf_succs[i + 1];
            --leaf_succ_count;
        }
        return result;
    }
