    public Path aStarSearch3(DirtyNode source, DirtyNode target) throws PathNotFoundException {
        DirtyNode[] open = new DirtyNode[32];
        int n = 0;
        reset();
        float dx = target.x - source.x;
        float dy = target.y - source.y;
        float h = (float) Math.sqrt(dx * dx + dy * dy);
        source.total = 0.0f;
        source.score = minCost * h;
        open[n++] = source;
        while (n > 0) {
            DirtyNode current = open[--n];
            current.state ^= Node.OPEN;
            if (current.equals(target)) {
                return new DefaultPath(current);
            }
            DirtyNode[] child = current.children;
            for (int i = 0; i < child.length; i++) {
                if (child[i] == null || child[i].cost >= maxCost) {
                    continue;
                }
                float weight = (i % 2 == 0) ? 1 : MATH_SQRT_2;
                float total = current.total + weight * child[i].cost;
                if ((child[i].state & Node.OPEN) > 0 && child[i].total <= total) {
                    continue;
                }
                if ((child[i].state & Node.CLOSED) > 0 && child[i].total <= total) {
                    continue;
                }
                dx = child[i].x - target.x;
                dy = child[i].y - target.y;
                h = (float) Math.sqrt(dx * dx + dy * dy);
                float score = total + minCost * h;
                child[i].parent = current;
                child[i].total = total;
                child[i].score = score;
                if ((child[i].state & Node.CLOSED) > 0) {
                    child[i].state ^= Node.CLOSED;
                }
                if ((child[i].state & Node.OPEN) == 0) {
                    int l = 0;
                    int r = n - 1;
                    int m = 0;
                    int t = 0;
                    while (l <= r) {
                        m = (l + r) / 2;
                        t = child[i].score < open[m].score ? 1 : (child[i].score > open[m].score ? -1 : 0);
                        if (t < 0) {
                            r = m - 1;
                        } else if (t > 0) {
                            l = m + 1;
                        } else {
                            break;
                        }
                    }
                    m = m < 0 ? 0 : m;
                    if (n > open.length - 2) {
                        DirtyNode[] temp = open;
                        open = new DirtyNode[temp.length << 1];
                        System.arraycopy(temp, 0, open, 0, temp.length);
                    }
                    System.arraycopy(open, m, open, m + 1, n - m);
                    child[i].state ^= Node.OPEN;
                    open[m] = child[i];
                    n = n + 1;
                }
            }
            current.state ^= Node.CLOSED;
        }
        throw new PathNotFoundException("s=" + source + ", t=" + target);
    }
