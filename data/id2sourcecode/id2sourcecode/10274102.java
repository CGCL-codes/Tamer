    private int computeBaselineDifference(InlineElement box) {
        int a = box.getBaselineOffset();
        int b = box.getBelowBaseline();
        CSSProperty.VerticalAlign va = box.getVerticalAlign();
        int dif = 0;
        if (va == CSSProperty.VerticalAlign.BASELINE) dif = 0; else if (va == CSSProperty.VerticalAlign.MIDDLE) {
            int midbox = (a + b) / 2;
            int halfex = (int) Math.round(parent.getVisualContext().getEx() / 2);
            int na = midbox + halfex;
            dif = a - na;
        } else if (va == CSSProperty.VerticalAlign.SUB) dif = (int) Math.round(0.3 * parent.getLineHeight()); else if (va == CSSProperty.VerticalAlign.SUPER) dif = -(int) Math.round(0.3 * parent.getLineHeight()); else if (va == CSSProperty.VerticalAlign.TEXT_TOP) {
            int na = parent.getVisualContext().getBaselineOffset();
            dif = a - na;
        } else if (va == CSSProperty.VerticalAlign.TEXT_BOTTOM) {
            int nb = parent.getVisualContext().getFontHeight() - parent.getVisualContext().getBaselineOffset();
            dif = nb - b;
        } else if (va == CSSProperty.VerticalAlign.length || va == CSSProperty.VerticalAlign.percentage) {
            CSSDecoder dec = new CSSDecoder(((ElementBox) box).getVisualContext());
            int len = dec.getLength(((ElementBox) box).getLengthValue("vertical-align"), false, 0, 0, box.getLineHeight());
            dif = -len;
        }
        return dif;
    }
