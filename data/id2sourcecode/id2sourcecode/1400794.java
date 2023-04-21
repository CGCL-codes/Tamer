    public static float fitText(Font font, String text, Rectangle rect, float maxFontSize, int runDirection) {
        try {
            ColumnText ct = null;
            int status = 0;
            if (maxFontSize <= 0) {
                int cr = 0;
                int lf = 0;
                char t[] = text.toCharArray();
                for (int k = 0; k < t.length; ++k) {
                    if (t[k] == '\n') ++lf; else if (t[k] == '\r') ++cr;
                }
                int minLines = Math.max(cr, lf) + 1;
                maxFontSize = Math.abs(rect.getHeight()) / minLines - 0.001f;
            }
            font.setSize(maxFontSize);
            Phrase ph = new Phrase(text, font);
            ct = new ColumnText(null);
            ct.setSimpleColumn(ph, rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop(), maxFontSize, Element.ALIGN_LEFT);
            ct.setRunDirection(runDirection);
            status = ct.go(true);
            if ((status & ColumnText.NO_MORE_TEXT) != 0) return maxFontSize;
            float precision = 0.1f;
            float min = 0;
            float max = maxFontSize;
            float size = maxFontSize;
            for (int k = 0; k < 50; ++k) {
                size = (min + max) / 2;
                ct = new ColumnText(null);
                font.setSize(size);
                ct.setSimpleColumn(new Phrase(text, font), rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop(), size, Element.ALIGN_LEFT);
                ct.setRunDirection(runDirection);
                status = ct.go(true);
                if ((status & ColumnText.NO_MORE_TEXT) != 0) {
                    if (max - min < size * precision) return size;
                    min = size;
                } else max = size;
            }
            return size;
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
