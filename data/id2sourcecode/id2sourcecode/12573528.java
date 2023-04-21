    private void drawGridLabels(Graphics graphics) {
        if (m_fieldSize < 15) return;
        graphics.setColor(m_gridLabelColor);
        setFont(graphics, m_fieldSize);
        int offset = (m_fieldSize + m_fieldOffset) / 2;
        Point point;
        char c = 'A';
        for (int x = 0; x < m_size; ++x) {
            String string = Character.toString(c);
            point = getLocation(x, 0);
            point.y += offset;
            drawLabel(graphics, point, string);
            point = getLocation(x, m_size - 1);
            point.y -= offset;
            drawLabel(graphics, point, string);
            ++c;
            if (c == 'I') ++c;
        }
        for (int y = 0; y < m_size; ++y) {
            String string = Integer.toString(y + 1);
            point = getLocation(0, y);
            point.x -= offset;
            drawLabel(graphics, point, string);
            point = getLocation(m_size - 1, y);
            point.x += offset;
            drawLabel(graphics, point, string);
        }
    }
