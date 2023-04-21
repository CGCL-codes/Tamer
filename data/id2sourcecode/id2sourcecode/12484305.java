    protected void paintComponent(Graphics _g) {
        if (isOpaque()) {
            Rectangle r = new Rectangle(0, 0, getWidth(), getHeight());
            r = r.intersection(_g.getClipBounds());
            _g.setColor(getBackground());
            _g.fillRect(r.x, r.y, r.width, r.height);
        }
        Insets insets = getInsets();
        Font ft = getFont();
        String tt = getText();
        int xm = insets.left;
        int ym = insets.top;
        int wm = getWidth() - insets.left - insets.right;
        int hm = getHeight() - insets.top - insets.bottom;
        int al = getHorizontalAlignment();
        int va = getVerticalAlignment();
        switch(va) {
            case CENTER:
                {
                    int ph = Math.max(0, getHeight() - getPreferredSize().height);
                    ym += ph / 2;
                    hm -= 2 * (ph / 2);
                }
                break;
            case BOTTOM:
                {
                    int ph = Math.max(0, getHeight() - getPreferredSize().height);
                    ym += ph;
                    hm -= 2 * (ph / 2);
                }
                break;
        }
        Icon icon = getIcon();
        if (icon != null) {
            int gap = getIconTextGap();
            int wi = icon.getIconWidth();
            int hi = icon.getIconHeight();
            int hp = getHorizontalTextPosition();
            int vp = getVerticalTextPosition();
            int xi, yi;
            switch(hp) {
                case LEFT:
                    xi = xm + Math.max(0, wm - wi - gap);
                    wm -= wi + gap;
                    break;
                case CENTER:
                    xi = xm + Math.max(0, (wm - wi) / 2);
                    break;
                default:
                    xi = xm;
                    xm += wi + gap;
                    wm -= wi + gap;
                    break;
            }
            switch(vp) {
                case TOP:
                    yi = ym + Math.max(0, hm - hi - gap);
                    if (hp == CENTER) hm -= hi + gap;
                    break;
                case CENTER:
                    yi = ym + Math.max(0, (hm - hi) / 2);
                    break;
                default:
                    yi = ym;
                    if (hp == CENTER) {
                        ym += hi + gap;
                        hm -= hi + gap;
                    }
                    break;
            }
            icon.paintIcon(this, _g, xi, yi);
        }
        boolean first = firstMode_;
        if (first) ft = BuLib.deriveFont(ft, Font.BOLD, 0);
        _g.setFont(ft);
        _g.setColor(getForeground());
        FontMetrics fm = _g.getFontMetrics();
        int x = xm;
        int y = fm.getAscent() + ym;
        int hs = fm.getAscent() + fm.getDescent() - 1;
        while (!tt.equals("")) {
            int i, j;
            String s;
            i = tt.indexOf('\n');
            if (i >= 0) {
                s = tt.substring(0, i);
                tt = tt.substring(i + 1);
            } else {
                s = tt;
                tt = "";
            }
            while (!"".equals(s)) {
                int ws = 0;
                String ss = null;
                boolean forced = false;
                j = 0;
                if (getWrapMode() == WORD) {
                    for (j = s.length(); j > 0; j--) {
                        if ((j < s.length()) && !Character.isWhitespace(s.charAt(j - 1))) continue;
                        ws = BuFontChooser.stringWidthWithStyle(fm, s.substring(0, j), style_);
                        if (ws < wm) {
                            ss = s.substring(0, j);
                            s = s.substring(j).trim();
                            break;
                        }
                    }
                    if (j == 0) forced = true;
                }
                if (forced || (getWrapMode() == LETTER)) {
                    for (j = s.length(); j > 0; j--) {
                        if (Character.isWhitespace(s.charAt(j - 1))) continue;
                        ws = BuFontChooser.stringWidthWithStyle(fm, s.substring(0, j), style_);
                        if (ws < wm) {
                            ss = s.substring(0, j);
                            s = s.substring(j).trim();
                            break;
                        }
                    }
                }
                if (j == 0) {
                    ss = s;
                    s = "";
                    ws = BuFontChooser.stringWidthWithStyle(fm, ss, style_);
                }
                if (al == LEFT) x = xm; else if (al == RIGHT) x = xm + wm - ws; else x = xm + (wm - ws) / 2;
                drawSingleLine(_g, ss, x, y, ws);
                y += hs;
                if (first) {
                    first = false;
                    _g.setColor(_g.getColor().brighter());
                    _g.setFont(BuLib.deriveFont(ft, Font.PLAIN, -1));
                    fm = _g.getFontMetrics();
                    hs = fm.getAscent() + fm.getDescent() - 1;
                }
            }
        }
    }
