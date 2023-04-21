    public int binary_query(long query[], Set<Integer[]> origins) {
        Node current = this;
        for (int i = 0; i < query.length; i++) {
            long v = query[i];
            boolean matched = false;
            int a = 0;
            int b = current.getChildrenNum() - 1;
            int s = (a + b) / 2;
            while (a <= b) {
                Node nj = current.getChild(s);
                Interval Sj = nj.getInterval(0);
                long leftj = Sj.getLeft();
                long rightj = Sj.getRight();
                if (leftj <= v && v <= rightj) {
                    current = nj;
                    matched = true;
                    break;
                }
                if (v < leftj) {
                    b = s - 1;
                    s = (a + b) / 2;
                } else {
                    a = s + 1;
                    s = (a + b) / 2;
                }
            }
            if (!matched) {
                return -1;
            }
        }
        if (current.isLeaf()) {
            origins.addAll(current.getOrigins());
            return current.getLabel();
        } else {
            System.out.println("Error in Node.java binary_query. Did not reach leaf.");
            return -1;
        }
    }
