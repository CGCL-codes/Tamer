    public boolean checkEdge(int x, int y) {
        Graphics2D g2 = (Graphics2D) getGraphics();
        Font oldFont;
        FontMetrics fm;
        try {
            oldFont = g2.getFont();
            g2.setFont(new Font(oldFont.getFontName(), oldFont.getStyle(), oldFont.getSize() - 2));
            fm = g2.getFontMetrics();
            if (currGraph.isDirected() == false) {
                int i, j;
                int x1, y1, x2, y2;
                int cx, cy;
                int r;
                EdgeAttr ea;
                VertexAttr va1, va2;
                for (i = 0; i < currGraph.numVertices(); i++) {
                    for (j = 0; j < i; j++) {
                        if (currGraph.edgeExists(i, j)) {
                            ea = currGraph.getEdge(i, j);
                            va1 = currGraph.getVertex(i);
                            va2 = currGraph.getVertex(j);
                            x1 = va1.getXpos();
                            y1 = va1.getYpos();
                            x2 = va2.getXpos();
                            y2 = va2.getYpos();
                            cx = (x1 + x2) / 2;
                            cy = (y1 + y2) / 2;
                            if (getShowEdgeWeights()) r = Math.max(12, 2 + fm.stringWidth("" + ea.getWeight())); else r = 6;
                            if (x >= cx - r / 2 && x <= cx + r / 2 && y >= cy - r / 2 && y <= cy + r / 2) {
                                currEdgeFrom = i;
                                currEdgeTo = j;
                                return true;
                            }
                        }
                    }
                }
            } else {
                int i, j;
                int x1, y1, x2, y2;
                int w2, h2;
                int cx, cy;
                int r;
                double len, ang, ad;
                EdgeAttr ea;
                VertexAttr va1, va2;
                for (i = 0; i < currGraph.numVertices(); i++) {
                    for (j = 0; j < currGraph.numVertices(); j++) {
                        if (currGraph.edgeExists(i, j)) {
                            ea = currGraph.getEdge(i, j);
                            va1 = currGraph.getVertex(i);
                            va2 = currGraph.getVertex(j);
                            x1 = va1.getXpos();
                            y1 = va1.getYpos();
                            x2 = va2.getXpos();
                            y2 = va2.getYpos();
                            w2 = va2.getWidth();
                            h2 = va2.getHeight();
                            len = Math.hypot(x2 - x1, y2 - y1);
                            ang = Math.atan2(y2 - y1, x2 - x1);
                            ad = 0.5 * Math.sqrt(w2 * w2 + h2 * h2);
                            cx = (int) (x1 + (len - ad - 20) * Math.cos(ang));
                            cy = (int) (y1 + (len - ad - 20) * Math.sin(ang));
                            if (getShowEdgeWeights()) r = Math.max(12, 2 + fm.stringWidth("" + ea.getWeight())); else r = 6;
                            if (x >= cx - r / 2 && x <= cx + r / 2 && y >= cy - r / 2 && y <= cy + r / 2) {
                                currEdgeFrom = i;
                                currEdgeTo = j;
                                return true;
                            }
                        }
                    }
                }
            }
            g2.setFont(oldFont);
        } catch (IllegalVertexException e) {
            System.out.println("Check error in GPanel.drawGraph");
        }
        currEdgeFrom = -1;
        currEdgeTo = -1;
        return false;
    }
