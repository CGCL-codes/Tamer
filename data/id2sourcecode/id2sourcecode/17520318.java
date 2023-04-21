        public void visit(Object element, int row, int column) {
            AntlrRailRoadNode node = (AntlrRailRoadNode) element;
            column--;
            if (column >= 0) {
                AntlrRailRoadNode prev = (AntlrRailRoadNode) fMatrix.get(row, column);
                while (prev == null && row >= 0) {
                    row--;
                    prev = (AntlrRailRoadNode) fMatrix.get(row, column);
                }
                if (prev != null) {
                    Polyline polyline = new Polyline();
                    polyline.setLineWidth(2);
                    if (prev.row == node.row) {
                        double y = prev.location.y + (prev.location.height / 2);
                        double x1 = prev.location.x + prev.location.width;
                        double x2 = node.location.x;
                        polyline.addPoint(new Point(x1, y));
                        polyline.addPoint(new Point(x2, y));
                    } else {
                        double y1 = prev.location.y + (prev.location.height / 2);
                        double y2 = node.location.y + (node.location.height / 2);
                        double prevEnd = prev.location.x + prev.location.width;
                        double nextStart = node.location.x;
                        double nextEnd = nextStart + node.location.width;
                        double x1 = prevEnd + (nextStart - prevEnd) / 2;
                        x1 += x1 * 7.5 / 100;
                        double x2 = node.location.x;
                        polyline.addPoint(new Point(x1, y1));
                        polyline.addPoint(new Point(x1, y2));
                        polyline.addPoint(new Point(x2, y2));
                    }
                    root.add(polyline);
                }
            }
            root.add(node, node.location);
        }
