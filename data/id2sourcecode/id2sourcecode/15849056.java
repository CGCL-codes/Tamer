    public final Path aStarSearch2(Node source, Node target) throws PathNotFoundException {
        int n = 0;
        Node[] open = new Node[16];
        reset();
        source.setTotal(0.0f);
        source.setScore(minCost * heuristic(source, target));
        open[n++] = source;
        while (n > 0) {
            Node current = open[--n];
            current.setOpen(false);
            if (current.equals(target)) {
                return new DefaultPath(current);
            }
            Node[] child = current.getAllChildren();
            for (int i = 0; i < child.length; i++) {
                if (child[i] == null || child[i].getCost() >= maxCost) {
                    continue;
                }
                float total = current.getTotal() + cost(current, child[i]);
                if (child[i].isOpen() && child[i].getTotal() <= total) {
                    continue;
                }
                if (child[i].isClosed() && child[i].getTotal() <= total) {
                    continue;
                }
                float score = total + minCost * heuristic(child[i], target);
                child[i].setParent(current);
                child[i].setTotal(total);
                child[i].setScore(score);
                if (child[i].isClosed()) {
                    child[i].setClosed(false);
                }
                if (!child[i].isOpen()) {
                    int l = 0;
                    int r = n - 1;
                    int m = 0;
                    int t = 0;
                    while (l <= r) {
                        m = (l + r) / 2;
                        t = -child[i].compareTo(open[m]);
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
                        Node[] temp = open;
                        open = new Node[temp.length << 1];
                        System.arraycopy(temp, 0, open, 0, temp.length);
                    }
                    System.arraycopy(open, m, open, m + 1, n - m);
                    child[i].setOpen(true);
                    open[m] = child[i];
                    n = n + 1;
                }
            }
            current.setClosed(true);
        }
        throw new PathNotFoundException("s=" + source + ", f=" + target);
    }
