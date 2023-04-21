    public void renderSingleText(IGraphics g, int x, int y, int w, int h) {
        m_CharH = g.getStringHeight();
        if (Lable != m_CurText) {
            m_CurText = Lable;
        }
        String str = m_CurText;
        if (IsPassword) {
            char[] m = new char[m_CurText.length()];
            for (int s = 0; s < m.length; s++) {
                m[s] = '*';
            }
            str = new String(m);
        }
        {
            int sw = g.getStringWidth(str);
            try {
                int c = CaretPosition;
                int c1 = Math.max(Math.min(PrewCaretPosition, CaretPosition), 0);
                int c2 = Math.min(Math.max(PrewCaretPosition, CaretPosition), str.length());
                String s = str.substring(0, c);
                String s1 = str.substring(0, c1);
                String s2 = str.substring(0, c2);
                int swc = g.getStringWidth(s);
                int s1w = g.getStringWidth(s1);
                int s2w = g.getStringWidth(s2);
                int dy = y + (H - m_CharH) / 2;
                int dxc = x + swc;
                int dx1 = x + s1w;
                int dx2 = x + s2w;
                if (sw > w) {
                    if (dxc - OffsetX + 2 > x + w) {
                        OffsetX += ((dxc - OffsetX + 2) - (x + w));
                    } else if (dxc - OffsetX < x) {
                        OffsetX -= (x - (dxc - OffsetX));
                    }
                    OffsetX = Math.max(OffsetX, 0);
                    OffsetX = Math.min(OffsetX, sw);
                } else {
                    OffsetX = 0;
                }
                if (c1 != c2) {
                    g.setColor(SelectRegionColor);
                    g.fillRect(dx1 - OffsetX, dy, dx2 - dx1, m_CharH);
                }
                g.setColor(TextColor);
                g.drawString(str, x - OffsetX, dy);
                if (getCurrentInputListener() == this && this.isFocused() && getTimer() / 4 % 2 == 0) {
                    g.setColor(CaretColor);
                    g.fillRect(dxc - OffsetX, dy, 2, m_CharH);
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
