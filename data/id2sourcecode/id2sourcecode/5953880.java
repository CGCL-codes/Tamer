    public void readjust() {
        List<Node> l = new java.util.ArrayList<Node>(owner.getIncidentVertices());
        GuessPNode node1 = (GuessPNode) ((Node) l.get(0)).getRep();
        GuessPNode node2 = node1;
        if (l.size() > 1) {
            node2 = (GuessPNode) ((Node) l.get(1)).getRep();
        }
        if (node1 != node2) {
            if (VisFactory.getFactory().getDirected()) {
                Point2D pa = new Point2D.Double(0, 0);
                Point2D pb = new Point2D.Double(0, 0);
                findEndPoints(node1, node2, pa, pb);
                double x1 = pa.getX();
                double y1 = pa.getY();
                double x2 = pb.getX();
                double y2 = pb.getY();
                if (VisFactory.getFactory().getDirected()) {
                    if (Guess.getZooming() == Guess.ZOOMING_ZOOM) {
                        double cx = (x1 + x2) / 2;
                        double cy = (y1 + y2) / 2;
                        double thetaRadians = Math.atan2((y1 - y2), (x1 - x2));
                        double buffer = Math.max(2, Arrow.getArrowLength(x1, y1, x2, y2, getLineWidth()) / 2);
                        double radius = (Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) / 2) - buffer;
                        double tx1 = radius * Math.cos(thetaRadians) + cx;
                        double ty1 = radius * Math.sin(thetaRadians) + cy;
                        thetaRadians += Math.PI;
                        double tx2 = radius * Math.cos(thetaRadians) + cx;
                        double ty2 = radius * Math.sin(thetaRadians) + cy;
                        setShape(new Line2D.Double(tx1, ty1, tx2, ty2));
                    } else {
                        setShape(new Line2D.Double(x1, y1, x2, y2));
                    }
                } else {
                    setShape(new Line2D.Double(x1, y1, x2, y2));
                }
                if (owner instanceof DirectedEdge) {
                    if (((Node) (((DirectedEdge) owner).getSource())).getRep() == node1) {
                        arrow_style = ARROW_END;
                        p1 = new Point2D.Double(x1, y1);
                        p2 = new Point2D.Double(x2, y2);
                    } else {
                        arrow_style = ARROW_START;
                        p1 = new Point2D.Double(x1, y1);
                        p2 = new Point2D.Double(x2, y2);
                    }
                } else if (owner instanceof UndirectedEdge) {
                    arrow_style = ARROW_BOTH;
                    p1 = new Point2D.Double(x1, y1);
                    p2 = new Point2D.Double(x2, y2);
                }
                if (labelVisible) {
                    labelX = (x1 + x2) / 2 + 2;
                    if (x1 != x2) {
                        double slope = (y1 - y2) / (x1 - x2);
                        if ((slope >= -.5) && (slope <= 0)) {
                            labelY = (y1 + y2) / 2 + 2;
                        } else if ((slope > 0) && (slope <= .5)) {
                            labelY = (y1 + y2) / 2 - 2;
                        } else {
                            labelY = (y1 + y2) / 2;
                        }
                    } else {
                        labelY = (y1 + y2) / 2;
                    }
                }
            } else {
                double lx1 = node1.getX() + node1.getWidth() / 2;
                double ly1 = node1.getY() + node1.getHeight() / 2;
                double lx2 = node2.getX() + node2.getWidth() / 2;
                double ly2 = node2.getY() + node2.getHeight() / 2;
                setShape(new Line2D.Double(lx1, ly1, lx2, ly2));
                if (labelVisible) {
                    labelX = (lx1 + lx2) / 2 + 2;
                    if (lx1 != lx2) {
                        double slope = (ly1 - ly2) / (lx1 - lx2);
                        if ((slope >= -.5) && (slope <= 0)) {
                            labelY = (ly1 + ly2) / 2 + 2;
                        } else if ((slope > 0) && (slope <= .5)) {
                            labelY = (ly1 + ly2) / 2 - 2;
                        } else {
                            labelY = (ly1 + ly2) / 2;
                        }
                    } else {
                        labelY = (ly1 + ly2) / 2;
                    }
                }
                arrow_style = ARROW_NONE;
                p1 = null;
                p2 = null;
            }
        } else {
            double x1 = node1.getX() + node1.getWidth() / 2;
            double y1 = node1.getY() + node1.getHeight() / 2;
            double size = Math.max(node1.getHeight(), node1.getWidth());
            setShape(new Ellipse2D.Double(x1 - size, y1 - size, size, size));
            if (labelVisible) {
                labelX = x1 - size / 2;
                labelY = y1 - size / 2 - 1;
            }
            arrow_style = ARROW_NONE;
            p1 = null;
            p2 = null;
        }
    }
