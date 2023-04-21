    private static RandomPolygon newInstance(int x, int y, double minDistance, int minSides, int maxSides, double minArea, double maxArea) {
        if (rand == null) rand = new Random();
        LinkedList<RandomPolygon> stack = new LinkedList<RandomPolygon>();
        HashSet<RandomPolygon> alreadySearched = new HashSet<RandomPolygon>();
        stack.add(new RandomPolygon(new int[] { x + 0, x + 5, x + 10 }, new int[] { y + 0, y + 10, y + 0 }, 3));
        while (!stack.isEmpty()) {
            RandomPolygon node = stack.removeFirst();
            double area = node.calculateArea();
            if (node.npoints >= minSides && node.npoints <= maxSides && area >= minArea && area <= maxArea) {
                boolean pointsAreValid = true;
                for (int edge = 0; edge < node.npoints && pointsAreValid; edge++) {
                    int x1 = node.xpoints[edge];
                    int y1 = node.ypoints[edge];
                    int x2 = (edge == node.npoints - 1 ? node.xpoints[0] : node.xpoints[edge + 1]);
                    int y2 = (edge == node.npoints - 1 ? node.ypoints[0] : node.ypoints[edge + 1]);
                    for (int point = 0; point < node.npoints && pointsAreValid; point++) {
                        if (point == edge || (edge == node.npoints - 1 && point == 0) || (edge < node.npoints - 1 && point == edge + 1)) continue;
                        int px = node.xpoints[point];
                        int py = node.ypoints[point];
                        double distance = Line2D.ptSegDist((double) x1, (double) y1, (double) x2, (double) y2, (double) px, (double) py);
                        if (distance < minDistance) {
                            System.err.println("(" + px + ", " + py + ") is too close to (" + x1 + ", " + y1 + ")-(" + x2 + ", " + y2 + ")");
                            pointsAreValid = false;
                        }
                    }
                }
                if (pointsAreValid) return node;
            }
            ArrayList<RandomPolygon> successors = new ArrayList<RandomPolygon>();
            ArrayList<RandomPolygon> topSuccessors = new ArrayList<RandomPolygon>();
            double centroid[] = node.calculateCentroid();
            System.err.println("Centroid: (" + centroid[0] + ", " + centroid[1] + ")");
            for (int i = 1; i < node.npoints; i++) {
                double xdiff = (double) node.xpoints[i] - centroid[0];
                double ydiff = (double) node.ypoints[i] - centroid[1];
                double angle;
                if (xdiff == 0) angle = Math.PI / 2.0; else angle = Math.atan(ydiff / xdiff);
                int newX = node.xpoints[i] + (int) (2.0 * Math.cos(angle));
                int newY = node.ypoints[i] + (int) (2.0 * Math.sin(angle));
                boolean alreadyExists = false;
                for (int j = 0; j < node.npoints && !alreadyExists; j++) if (newX == node.xpoints[j] && newY == node.ypoints[j]) alreadyExists = true;
                if (alreadyExists) continue;
                System.err.println("Moving (" + node.xpoints[i] + ", " + node.ypoints[i] + ") --> (" + newX + ", " + newY + ")");
                int xpoints[] = new int[node.npoints];
                int ypoints[] = new int[node.npoints];
                for (int j = 0; j < node.npoints; j++) {
                    if (j == i) {
                        xpoints[j] = newX;
                        ypoints[j] = newY;
                    } else {
                        xpoints[j] = node.xpoints[j];
                        ypoints[j] = node.ypoints[j];
                    }
                }
                RandomPolygon newSuccessor = new RandomPolygon(xpoints, ypoints, node.npoints);
                if (calculateArea(newSuccessor) <= maxArea) {
                    boolean pointIsValid = true;
                    for (int edge = 0; edge < node.npoints && pointIsValid; edge++) {
                        int x1 = node.xpoints[edge];
                        int y1 = node.ypoints[edge];
                        int x2 = (edge == node.npoints - 1 ? node.xpoints[0] : node.xpoints[edge + 1]);
                        int y2 = (edge == node.npoints - 1 ? node.ypoints[0] : node.ypoints[edge + 1]);
                        if (i == edge || (edge == node.npoints - 1 && i == 0) || (edge < node.npoints - 1 && i == edge + 1)) continue;
                        int px = node.xpoints[i];
                        int py = node.ypoints[i];
                        double distance = Line2D.ptSegDist((double) x1, (double) y1, (double) x2, (double) y2, (double) px, (double) py);
                        if (distance < minDistance) {
                            System.err.println("(" + px + ", " + py + ") is too close to (" + x1 + ", " + y1 + ")-(" + x2 + ", " + y2 + ")");
                            pointIsValid = false;
                        }
                    }
                    if (!pointIsValid) topSuccessors.add(newSuccessor); else successors.add(newSuccessor);
                }
            }
            if (node.npoints < maxSides) {
                int numSuccessors = node.npoints / 2;
                double boundaryLength = node.calculateBoundaryLength();
                for (int i = 0; i < numSuccessors; i++) {
                    double distance = boundaryLength * rand.nextDouble();
                    int point[] = node.getPointOnPerimeter(distance);
                    int idx = point[2] + 1;
                    System.err.println("Adding a new point (index " + idx + ") at (" + point[0] + ", " + point[1] + ")");
                    int xpoints[] = new int[node.npoints + 1];
                    int ypoints[] = new int[node.npoints + 1];
                    for (int j = 0; j <= node.npoints; j++) {
                        if (j < idx) {
                            xpoints[j] = node.xpoints[j];
                            ypoints[j] = node.ypoints[j];
                        } else if (j == idx) {
                            xpoints[j] = point[0];
                            ypoints[j] = point[1];
                        } else {
                            xpoints[j] = node.xpoints[j - 1];
                            ypoints[j] = node.ypoints[j - 1];
                        }
                    }
                    successors.add(new RandomPolygon(xpoints, ypoints, node.npoints + 1));
                }
            }
            for (int successorList = 0; successorList < 2; successorList++) {
                ArrayList<RandomPolygon> succ;
                switch(successorList) {
                    case 0:
                        succ = successors;
                        break;
                    case 1:
                    default:
                        succ = topSuccessors;
                        break;
                }
                int remaining[] = new int[succ.size()];
                for (int i = 0; i < remaining.length; i++) remaining[i] = i;
                for (int i = 0; i < remaining.length; i++) {
                    int chosen = rand.nextInt(remaining.length - i);
                    RandomPolygon successor = succ.get(remaining[chosen]);
                    if (!alreadySearched.contains(successor)) {
                        alreadySearched.add(successor);
                        stack.addFirst(successor);
                    }
                    for (int j = chosen; j < remaining.length - i - 1; j++) remaining[j] = remaining[j + 1];
                }
            }
        }
        return null;
    }
