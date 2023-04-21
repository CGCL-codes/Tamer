    private double paintNode(Graphics2D g2, RootedTree tree, Node node, double x0, double x1, boolean hilight) {
        double y;
        double ix0 = convertX(x0);
        double ix1 = convertX(x1);
        double iy;
        if (tree.isExternal(node)) {
            if (rememberYPositions) {
                String taxonId = tree.getTaxon(node).getName();
                Double pos = yPositionMap.get(taxonId);
                if (pos != null) {
                    y = pos;
                } else {
                    y = currentY;
                    currentY += 1.0;
                    yPositionMap.put(taxonId, y);
                }
            } else {
                y = currentY;
                currentY += 1.0;
            }
            if (hilight) {
                g2.setPaint(hilightLabelPaint);
                g2.setFont(hilightLabelFont);
            } else {
                g2.setPaint(labelPaint);
                g2.setFont(labelFont);
            }
            String label = tree.getTaxon(node).getName();
            double labelWidth = g2.getFontMetrics().stringWidth(label);
            double labelHeight = g2.getFontMetrics().getAscent();
            double labelOffset = labelHeight / 2;
            iy = convertY(y);
            if (label != null && label.length() > 0 && drawLabels) {
                g2.drawString(label, (float) (ix1 + 4), (float) (iy + labelOffset));
            }
            nodeRectVert.put(node, new Rectangle.Double(ix1 + 4, iy, labelWidth, labelHeight));
            if (hilight) {
                g2.setPaint(hilightPaint);
                g2.setStroke(hilightStroke);
            } else {
                g2.setPaint(linePaint);
                g2.setStroke(lineStroke);
            }
        } else {
            double y0, y1;
            List<Node> children = tree.getChildren(node);
            Node child = children.get(0);
            double length = tree.getHeight(node) - tree.getHeight(child);
            y0 = paintNode(g2, tree, child, x1, x1 + length, hilight);
            y1 = y0;
            for (int i = 1; i < children.size(); i++) {
                child = children.get(i);
                length = tree.getHeight(node) - tree.getHeight(child);
                y1 = paintNode(g2, tree, child, x1, x1 + length, hilight);
            }
            double iy0 = convertY(y0);
            double iy1 = convertY(y1);
            if (hilight) {
                g2.setPaint(hilightPaint);
                g2.setStroke(hilightStroke);
            } else {
                g2.setPaint(linePaint);
                g2.setStroke(lineStroke);
            }
            if (drawHorizontals) {
                Line2D line = new Line2D.Double(ix1, iy0, ix1, iy1);
                g2.draw(line);
            }
            nodeRectVert.put(node, new Rectangle.Double(ix1 - 2, iy0 - 2, 5, (iy1 - iy0) + 4));
            y = (y1 + y0) / 2;
            iy = convertY(y);
        }
        if (drawVerticals) {
            Line2D line = new Line2D.Double(ix0, iy, ix1, iy);
            g2.draw(line);
        }
        nodeRectHoriz.put(node, new Rectangle.Double(ix0 - 2, iy - 2, (ix1 - ix0) + 4, 5));
        return y;
    }
