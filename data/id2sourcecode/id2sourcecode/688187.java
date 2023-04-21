    private void drawGraph(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Paint oldPaint;
        Font oldFont;
        FontMetrics fm;
        int nv = currGraph.numVertices();
        gFrame.statusBar.setMessage(GStatusBar.STATUS_GRAPHTYPE, (currGraph.isDirected() ? "Directed" : "Undirected") + " V:" + currGraph.numVertices() + " E:" + currGraph.numEdges());
        try {
            oldPaint = g2.getPaint();
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
                            g2.setPaint(ea.getColor());
                            g2.draw(new Line2D.Double(x1, y1, x2, y2));
                            if (getShowEdgeWeights()) r = Math.max(12, 2 + fm.stringWidth("" + ea.getWeight())); else r = 6;
                            g2.setPaint(Color.WHITE);
                            g2.fill(new Ellipse2D.Double(cx - r / 2, cy - r / 2, r, r));
                            g2.setPaint(ea.getColor());
                            g2.draw(new Ellipse2D.Double(cx - r / 2, cy - r / 2, r, r));
                            if (getShowEdgeWeights()) g2.drawString("" + ea.getWeight(), (int) (cx - fm.stringWidth("" + ea.getWeight()) / 2), (int) (cy + fm.getAscent() / 2));
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
                            if ((i <= j) || (i > j && currGraph.edgeExists(j, i) == false)) {
                                g2.setPaint(ea.getColor());
                                g2.draw(new Line2D.Double(x1, y1, x2, y2));
                            }
                            if (getShowEdgeWeights()) r = Math.max(12, 2 + fm.stringWidth("" + ea.getWeight())); else r = 6;
                            g2.setPaint(Color.WHITE);
                            g2.fill(new Ellipse2D.Double(cx - r / 2, cy - r / 2, r, r));
                            g2.setPaint(ea.getColor());
                            g2.draw(new Ellipse2D.Double(cx - r / 2, cy - r / 2, r, r));
                            if (getShowEdgeWeights()) g2.drawString("" + ea.getWeight(), (int) (cx - fm.stringWidth("" + ea.getWeight()) / 2), (int) (cy + fm.getAscent() / 2));
                            double px = x1 + (len - ad) * Math.cos(ang);
                            double py = y1 + (len - ad) * Math.sin(ang);
                            double p1x = px - 10 * Math.cos(ang - (30 * Math.PI / 180));
                            double p1y = py - 10 * Math.sin(ang - (30 * Math.PI / 180));
                            double p2x = px - 10 * Math.cos(ang + (30 * Math.PI / 180));
                            double p2y = py - 10 * Math.sin(ang + (30 * Math.PI / 180));
                            g2.draw(new Line2D.Double(px, py, p1x, p1y));
                            g2.draw(new Line2D.Double(px, py, p2x, p2y));
                        }
                    }
                }
            }
            g2.setPaint(oldPaint);
            g2.setFont(oldFont);
            oldPaint = g2.getPaint();
            fm = g2.getFontMetrics();
            for (int i = 0; i < nv; i++) {
                VertexAttr va = currGraph.getVertex(i);
                int vx = va.getXpos();
                int vy = va.getYpos();
                int vw = va.getWidth();
                int vh = va.getHeight();
                if (va.getShape() == VertexAttr.SHAPE_CIRCLE) {
                    g2.setPaint(va.getFillColor());
                    g2.fill(new Ellipse2D.Double(vx - vw / 2, vy - vh / 2, vw, vh));
                    g2.setPaint(va.getColor());
                    g2.draw(new Ellipse2D.Double(vx - vw / 2, vy - vh / 2, vw, vh));
                } else {
                    g2.setPaint(va.getFillColor());
                    g2.fill(new Rectangle2D.Double(vx - vw / 2, vy - vh / 2, vw, vh));
                    g2.setPaint(va.getColor());
                    g2.draw(new Rectangle2D.Double(vx - vw / 2, vy - vh / 2, vw, vh));
                }
                if (getShowVertexNames()) g2.drawString(va.getName(), vx - fm.stringWidth(va.getName()) / 2, vy + fm.getAscent() / 2);
            }
            g2.setPaint(oldPaint);
            oldPaint = g2.getPaint();
            if (currVertex != -1) {
                VertexAttr va = currGraph.getVertex(currVertex);
                int vx = va.getXpos();
                int vy = va.getYpos();
                int vw = va.getWidth();
                int vh = va.getHeight();
                g2.setPaint(va.getColor());
                g2.draw(new Rectangle2D.Double(vx - vw / 2 - 4 - 2, vy - 2, 4, 4));
                g2.draw(new Rectangle2D.Double(vx + vw / 2 + 4 - 2, vy - 2, 4, 4));
                g2.draw(new Rectangle2D.Double(vx - 2, vy - vh / 2 - 4 - 2, 4, 4));
                g2.draw(new Rectangle2D.Double(vx - 2, vy + vh / 2 + 4 - 2, 4, 4));
            }
            g2.setPaint(oldPaint);
        } catch (IllegalVertexException e) {
            System.out.println("Check error in GPanel.drawGraph");
        }
    }
