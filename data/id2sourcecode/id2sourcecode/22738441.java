        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (isOpaque()) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
            }
            final int w = this.getParent().getWidth();
            setSize(new Dimension(w, WIDTH_CIRCLE * 2));
            final Font font = new Font("Microsoft Sans Serif", Font.PLAIN, 11);
            final FontMetrics metric = this.getFontMetrics(font);
            final int circleHalf = (WIDTH_CIRCLE + 1) / 2;
            int title_w = metric.stringWidth(_title);
            int title_h = metric.getAscent();
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2.setColor(COLOR_BORDER);
            if (_title.equals("")) {
                g2.drawLine(MARGIN_SIDE, MARGIN_TOP, w - MARGIN_SIDE, MARGIN_TOP);
            } else {
                g2.drawLine(MARGIN_SIDE, MARGIN_TOP, SPACE_TITLE_LEFT - SPACE_TITLE, MARGIN_TOP);
                if (w - MARGIN_SIDE > SPACE_TITLE_LEFT + title_w + SPACE_TITLE) {
                    g2.drawLine(SPACE_TITLE_LEFT + title_w + SPACE_TITLE, MARGIN_TOP, w - MARGIN_SIDE, MARGIN_TOP);
                }
            }
            boolean enabled = true;
            synchronized (ComponentHider.this) {
                enabled = _enabled;
            }
            if (enabled) {
                g2.setColor(COLOR_TEXT);
            } else {
                g2.setColor(Color.GRAY);
            }
            g2.setFont(font);
            g2.drawString(_title, SPACE_TITLE_LEFT, MARGIN_TOP + title_h / 2);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int nMinPosition = Math.max(title_w + SPACE_TITLE_LEFT + SPACE_TITLE, w - POSITION_CIRCLE);
            g2.setColor(COLOR_BORDER);
            g2.fillOval(nMinPosition - 1, MARGIN_TOP - circleHalf, WIDTH_CIRCLE + 2, WIDTH_CIRCLE + 2);
            Color colorUsed;
            if (enabled) {
                colorUsed = COLOR_TEXT;
            } else {
                colorUsed = Color.GRAY;
            }
            g2.setPaint((new GradientPaint(nMinPosition, MARGIN_TOP, colorUsed, nMinPosition + circleHalf, MARGIN_TOP + circleHalf, colorUsed.darker(), false)));
            g2.fillOval(nMinPosition, MARGIN_TOP - circleHalf + 1, WIDTH_CIRCLE, WIDTH_CIRCLE);
            g2.setColor(COLOR_BORDER);
            g2.setStroke(new BasicStroke(2));
            if (_expanded == true) {
                g2.drawLine(nMinPosition + 3, MARGIN_TOP / 2 + 7, nMinPosition + 6, MARGIN_TOP / 2 + 4);
                g2.drawLine(nMinPosition + 6, MARGIN_TOP / 2 + 4, nMinPosition + 9, MARGIN_TOP / 2 + 7);
                displayComponent(true);
            } else {
                g2.drawLine(nMinPosition + 3, MARGIN_TOP / 2 + 4, nMinPosition + 6, MARGIN_TOP / 2 + 7);
                g2.drawLine(nMinPosition + 6, MARGIN_TOP / 2 + 7, nMinPosition + 9, MARGIN_TOP / 2 + 4);
                displayComponent(false);
            }
            _clickable = new Rectangle(MARGIN_SIDE, MARGIN_TOP - circleHalf, w - MARGIN_SIDE, WIDTH_CIRCLE);
        }
