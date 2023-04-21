        public SearchNode[] getSuccessors(double minDistance, int minSides, int maxSides, double minArea, double maxArea, Set<Double> validLengths, Set<Double> validAngles) {
            if (getNumSides() >= maxSides) return new SearchNode[0];
            ArrayList<SearchNode> succ = new ArrayList<SearchNode>(validLengths.size() * validAngles.size());
            for (Double length : validLengths) {
                for (Double angle : validAngles) {
                    double ang = slope + Math.toRadians(angle.doubleValue());
                    int newX = x + (int) (length.doubleValue() * Math.cos(ang) + 0.5);
                    int newY = y + (int) (length.doubleValue() * Math.sin(ang) + 0.5);
                    SearchNode newNode = new SearchNode(this, newX, newY, ang);
                    if (newX < 0 || newY < 0 || (parent != null && newX == parent.x && newY == parent.y && newNode.isComplete(minSides, minArea))) continue;
                    boolean intersects = false;
                    for (SearchNode node = parent; node != null && node.parent != null; node = node.parent) {
                        if (node == parent || (node.parent == root && newX == root.x && newY == root.y)) continue;
                        Line2D.Double l1 = new Line2D.Double(parent.x, parent.y, newX, newY);
                        Line2D.Double l2 = new Line2D.Double(node.parent.x, node.parent.y, node.x, node.y);
                        if (l1.intersectsLine(l2) || shortestDistance(l1, l2) < minDistance) {
                            intersects = true;
                            break;
                        }
                    }
                    if (intersects) continue;
                    if (newNode.getArea() <= maxArea) succ.add(newNode);
                }
            }
            if (rand == null) rand = new Random();
            SearchNode randomized[] = new SearchNode[succ.size()];
            int remaining[] = new int[succ.size()];
            for (int i = 0; i < remaining.length; i++) remaining[i] = i;
            for (int i = 0; i < randomized.length; i++) {
                int chosen = rand.nextInt(randomized.length - i);
                randomized[i] = succ.get(remaining[chosen]);
                for (int j = chosen; j < randomized.length - i - 1; j++) remaining[j] = remaining[j + 1];
            }
            return randomized;
        }
