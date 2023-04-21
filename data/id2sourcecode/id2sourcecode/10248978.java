    protected void calcDecorationPosition() {
        if (m_jDecorations != null && m_jDecorations.size() > 0) {
            double noteHeight = getMetrics().getNoteHeight();
            double x = getBase().getX() + getWidth() / 2;
            double bottom = getStaffLine().get1stLineY() + noteHeight;
            double top = getStaffLine().get5thLineY() - noteHeight;
            double middle = bottom + (top - bottom) / 2;
            double left = x - getWidth();
            double right = x + getWidth();
            m_decorationAnchors[JDecoration.ABOVE_NOTE] = m_decorationAnchors[JDecoration.ABOVE_STAFF] = m_decorationAnchors[JDecoration.ABOVE_STAFF_AFTER_NOTE] = m_decorationAnchors[JDecoration.STEM_END] = m_decorationAnchors[JDecoration.STEM_END_OUT_OF_STAFF] = m_decorationAnchors[JDecoration.VERTICAL_NEAR_STEM] = m_decorationAnchors[JDecoration.VERTICAL_NEAR_STEM_OUT_STAFF] = new Point2D.Double(x, top);
            m_decorationAnchors[JDecoration.UNDER_NOTE] = m_decorationAnchors[JDecoration.UNDER_STAFF] = m_decorationAnchors[JDecoration.VERTICAL_AWAY_STEM] = m_decorationAnchors[JDecoration.VERTICAL_AWAY_STEM_OUT_STAFF] = new Point2D.Double(x, bottom);
            m_decorationAnchors[JDecoration.HORIZONTAL_AWAY_STEM] = m_decorationAnchors[JDecoration.HORIZONTAL_NEAR_STEM] = m_decorationAnchors[JDecoration.STEM_MIDDLE] = m_decorationAnchors[JDecoration.MIDDLE_STAFF] = new Point2D.Double(x, middle);
            m_decorationAnchors[JDecoration.LEFT_NOTE] = new Point2D.Double(left, middle);
            m_decorationAnchors[JDecoration.RIGHT_NOTE] = new Point2D.Double(right, middle);
        }
    }
