    private double adjustTreeToConstraints(FlexibleTree tree, NodeRef node, Set<String> leaves, Map<Set<String>, double[]> cladeHeights) {
        if (!tree.isExternal(node)) {
            Set<String> l = new HashSet<String>();
            double maxChildHeight = 0.0;
            for (int i = 0; i < tree.getChildCount(node); i++) {
                NodeRef child = tree.getChild(node, i);
                double h = adjustTreeToConstraints(tree, child, l, cladeHeights);
                if (h > maxChildHeight) {
                    maxChildHeight = h;
                }
            }
            double height = tree.getNodeHeight(node);
            double lower = maxChildHeight;
            double upper = Double.POSITIVE_INFINITY;
            if (cladeHeights != null) {
                for (Set<String> clade : cladeHeights.keySet()) {
                    if (clade.equals(l)) {
                        double[] bounds = cladeHeights.get(clade);
                        lower = Math.max(bounds[0], maxChildHeight);
                        upper = bounds[1];
                    }
                }
            }
            if (lower > upper) {
                throw new IllegalArgumentException("incompatible constraints");
            }
            if (height < lower) {
                height = lower + 1E-6;
            } else if (height > upper) {
                height = (upper + lower) / 2;
            }
            tree.setNodeHeight(node, height);
            if (leaves != null) {
                leaves.addAll(l);
            }
        } else {
            leaves.add(tree.getNodeTaxon(node).getId());
        }
        return tree.getNodeHeight(node);
    }
