    protected void drawContent(GC gc, Rectangle r) {
        Region region = new Region();
        gc.getClipping(region);
        Rectangle clipping = gc.getClipping();
        Rectangle visibleArea = m_outerComposite.getClientArea();
        visibleArea.y = -m_innerComposite.getLocation().y;
        Rectangle drawingArea = clipping.intersection(visibleArea);
        int startY = drawingArea.y;
        int endY = drawingArea.y + drawingArea.height;
        int lo = 0;
        int hi = m_elements.size() - 1;
        while (lo < hi - 1) {
            int mid = (lo + hi) / 2;
            if (mid == lo) {
                break;
            }
            Element element = (Element) m_elements.get(mid);
            if (element.m_vcp.getBounds().y < startY) {
                lo = mid;
            } else {
                hi = mid;
            }
        }
        while (lo < m_elements.size()) {
            Element element = (Element) m_elements.get(lo);
            if (element.m_vcp.getBounds().y > endY) {
                break;
            }
            Rectangle r2 = element.m_vcp.getBounds();
            if (region.intersects(r2)) {
                element.m_vcp.draw(gc, r2);
            }
            lo++;
        }
        if (m_insertRect != null) {
            boolean xor = gc.getXORMode();
            int lineStyle = gc.getLineStyle();
            int lineWidth = gc.getLineWidth();
            Color color = gc.getForeground();
            gc.setXORMode(true);
            gc.setLineStyle(SWT.LINE_DOT);
            gc.setLineWidth(1);
            gc.setForeground(SlideUtilities.getAmbientColor(m_context));
            gc.drawRectangle(m_insertRect.x, m_insertRect.y, m_insertRect.width - 1, m_insertRect.height - 1);
            gc.setXORMode(xor);
            gc.setLineStyle(lineStyle);
            gc.setLineWidth(lineWidth);
            gc.setForeground(color);
        }
        region.dispose();
    }
