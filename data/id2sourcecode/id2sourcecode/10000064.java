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
        boolean minDist = params[MIN_DISTANCE] > 0f && r > params[MIN_DISTANCE];
        if ((!n.hasChildren && n.value != item) || (!same && (x2 - x1) / r < params[BARNES_HUT_THETA])) {
            if (minDist) return;
            float v = params[GRAVITATIONAL_CONST] * item.mass * n.mass / (r * r * r);
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
            if (minDist) return;
            if (n.value != null && n.value != item) {
                float v = params[GRAVITATIONAL_CONST] * item.mass * n.value.mass / (r * r * r);
                item.force[0] += v * dx;
                item.force[1] += v * dy;
            }
        }
    }
