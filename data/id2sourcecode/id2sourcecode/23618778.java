    private void forceHelper(ForceItem item, QuadTreeNode n, float x1, float y1, float x2, float y2) {
        float dx = n.com[0] - item.location[0];
        float dy = n.com[1] - item.location[1];
        float r = (float) Math.sqrt(dx * dx + dy * dy);
        boolean same = false;
        if (r == 0.0f) {
            dx = (rand.nextFloat() - 0.5f) / 50.0f;
            dy = (rand.nextFloat() - 0.5f) / 50.0f;
            r = (float) Math.sqrt(dx * dx + dy * dy);
            same = true;
        }
        Float mDist = (Float) MDForceLayoutConfig.params[MDForceLayoutConfig.MIN_DISTANCE];
        boolean minDist = mDist > 0f && r > mDist;
        if ((!n.hasChildren && n.value != item) || (!same && (x2 - x1) / r < (Float) MDForceLayoutConfig.params[MDForceLayoutConfig.BARNES_HUT_THETA])) {
            double rate = Math.random();
            if (minDist) {
                return;
            }
            float v = (Float) MDForceLayoutConfig.params[MDForceLayoutConfig.GRAVITATIONAL_CONST];
            v *= (item.mass * n.mass / (r * r * r));
            item.force[0] += v * dx;
            item.force[1] += v * dy;
        } else if (n.hasChildren) {
            float splitx = (x1 + x2) / 2;
            float splity = (y1 + y2) / 2;
            for (int i = 0; i < n.children.length; i++) {
                if (n.children[i] != null) {
                    forceHelper(item, n.children[i], (i == 1 || i == 3 ? splitx : x1), (i > 1 ? splity : y1), (i == 1 || i == 3 ? x2 : splitx), (i > 1 ? y2 : splity));
                }
            }
            if (minDist) {
                return;
            }
            if (n.value != null && n.value != item) {
                float v = (Float) MDForceLayoutConfig.params[MDForceLayoutConfig.GRAVITATIONAL_CONST];
                v *= (item.mass * n.value.mass / (r * r * r));
                if (item.force[0] * dx > 0 && item.force[1] * dy > 0 && Math.random() < (Float) MDForceLayoutConfig.params[MDForceLayoutConfig.PERTUBATE_RATE]) {
                    v = -3f * v;
                    n.value.force[0] -= v * dx;
                    n.value.force[1] -= v * dy;
                }
                item.force[0] += v * dx;
                item.force[1] += v * dy;
            }
        }
    }
