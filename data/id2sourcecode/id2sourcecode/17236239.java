    private double paintNode(Graphics2D g2, Tree tree, NodeRef node, double x0, double x1, boolean hilight) {
        double y;
        double ix0 = convertX(x0);
        double ix1 = convertX(x1);
        double iy;
        if (tree.getNodeAttribute(node, "selected") != null) {
            hilight = true;
        }
        if (tree.isExternal(node)) {
            if (rememberYPositions) {
                String taxonId = tree.getNodeTaxon(node).getId();
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
            String label = tree.getTaxonId(node.getNumber());
            double labelWidth = g2.getFontMetrics().stringWidth(label);
            double labelHeight = g2.getFontMetrics().getAscent();
            double labelOffset = labelHeight / 2;
            iy = convertY(y);
            if (label != null && label.length() > 0 && drawLabels) {
                g2.drawString(label, (float) (ix1 + 4), (float) (iy + labelOffset));
            }
            nodeRectVert[node.getNumber()] = new Rectangle.Double(ix1 + 4, iy, labelWidth, labelHeight);
            if (hilight) {
                g2.setPaint(hilightPaint);
                g2.setStroke(hilightStroke);
            } else {
                if (colorAttribute != null) {
                    Paint c = (Color) tree.getNodeAttribute(node, colorAttribute);
                    if (c == null) c = linePaint;
                    g2.setPaint(c);
                } else {
                    g2.setPaint(linePaint);
                }
                if (lineAttribute != null) {
                    Stroke stroke = (Stroke) tree.getNodeAttribute(node, lineAttribute);
                    if (stroke == null) stroke = lineStroke;
                    g2.setStroke(stroke);
                } else g2.setStroke(lineStroke);
            }
        } else {
            double y0, y1;
            NodeRef child = tree.getChild(node, 0);
            double length = tree.getNodeHeight(node) - tree.getNodeHeight(child);
            y0 = paintNode(g2, tree, child, x1, x1 - length, hilight);
            y1 = y0;
            for (int i = 1; i < tree.getChildCount(node); i++) {
                child = tree.getChild(node, i);
                length = tree.getNodeHeight(node) - tree.getNodeHeight(child);
                y1 = paintNode(g2, tree, child, x1, x1 - length, hilight);
            }
            double iy0 = convertY(y0);
            double iy1 = convertY(y1);
            if (hilight) {
                g2.setPaint(hilightPaint);
                g2.setStroke(hilightStroke);
            } else {
                if (colorAttribute != null) {
                    Paint c = (Color) tree.getNodeAttribute(node, colorAttribute);
                    if (c == null) c = linePaint;
                    g2.setPaint(c);
                } else {
                    g2.setPaint(linePaint);
                }
                if (lineAttribute != null) {
                    Stroke stroke = (Stroke) tree.getNodeAttribute(node, lineAttribute);
                    if (stroke == null) stroke = lineStroke;
                    g2.setStroke(stroke);
                } else g2.setStroke(lineStroke);
            }
            if (drawHorizontals) {
                Line2D line = new Line2D.Double(ix1, iy0, ix1, iy1);
                g2.draw(line);
            }
            nodeRectVert[node.getNumber()] = new Rectangle.Double(ix1 - 2, iy0 - 2, 5, (iy1 - iy0) + 4);
            y = (y1 + y0) / 2;
            iy = convertY(y);
        }
        if (drawVerticals) {
            Line2D line = new Line2D.Double(ix0, iy, ix1, iy);
            g2.draw(line);
        }
        nodeRectHoriz[node.getNumber()] = new Rectangle.Double(ix0 - 2, iy - 2, (ix1 - ix0) + 4, 5);
        if (shapeAttribute != null) {
            Shape shape = (Shape) tree.getNodeAttribute(node, shapeAttribute);
            if (shape != null) {
                Rectangle bounds = shape.getBounds();
                double tx = ix1 - bounds.getWidth() / 2.0;
                double ty = iy - bounds.getHeight() / 2.0;
                g2.translate(tx, ty);
                g2.fill(shape);
                g2.translate(-tx, -ty);
            }
        }
        if (labelAttribute != null) {
            Object label = tree.getNodeAttribute(node, labelAttribute);
            if (label != null) {
                Color c = g2.getColor();
                Font f = g2.getFont();
                Font fsmall = f.deriveFont(f.getSize() - 1.0f);
                g2.setFont(fsmall);
                String labelString = label.toString();
                int width = g2.getFontMetrics().stringWidth(labelString);
                g2.setColor(textColor);
                g2.drawString(labelString, (float) (ix1 - width - 1.0), (float) (iy - 2.0));
                g2.setColor(c);
                g2.setFont(f);
            }
        }
        return y;
    }
