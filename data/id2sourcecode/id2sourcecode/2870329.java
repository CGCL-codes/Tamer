    protected void computeHeightsAbsolute(TermLengthOrPercent height, boolean auto, boolean exact, int contw, int conth, boolean update) {
        CSSDecoder dec = new CSSDecoder(ctx);
        if (height == null) auto = true;
        boolean mtopauto = style.getProperty("margin-top") == CSSProperty.Margin.AUTO;
        TermLengthOrPercent mtop = getLengthValue("margin-top");
        boolean mbottomauto = style.getProperty("margin-bottom") == CSSProperty.Margin.AUTO;
        TermLengthOrPercent mbottom = getLengthValue("margin-bottom");
        if (cblock != null && cblock.hset) {
            hset = (exact && !auto && height != null);
            if (!update) content.height = dec.getLength(height, auto, 0, 0, conth);
        } else {
            hset = (exact && !auto && height != null && !height.isPercentage());
            if (!update) content.height = dec.getLength(height, auto, 0, 0, 0);
        }
        int constr = 0;
        if (hset) constr++;
        if (topset) constr++;
        if (bottomset) constr++;
        if (constr < 3) {
            if (mtopauto) margin.top = 0; else margin.top = dec.getLength(mtop, false, 0, 0, contw);
            if (mbottomauto) margin.bottom = 0; else margin.bottom = dec.getLength(mbottom, false, 0, 0, contw);
        } else {
            if (mtopauto && mbottomauto) {
                int rest = conth - coords.top - coords.bottom - border.top - border.bottom - padding.top - padding.bottom - content.height;
                margin.top = (rest + 1) / 2;
                margin.bottom = rest / 2;
            } else if (mtopauto) {
                margin.bottom = dec.getLength(mbottom, false, 0, 0, contw);
                margin.top = conth - coords.top - coords.bottom - border.top - border.bottom - padding.top - padding.bottom - content.height - margin.bottom;
            } else if (mbottomauto) {
                margin.top = dec.getLength(mtop, false, 0, 0, contw);
                margin.bottom = conth - coords.top - coords.bottom - border.top - border.bottom - padding.top - padding.bottom - content.height - margin.top;
            } else {
                margin.top = dec.getLength(mtop, false, 0, 0, contw);
                margin.bottom = dec.getLength(mbottom, false, 0, 0, contw);
            }
        }
        if (!topset && !bottomset) {
            topstatic = true;
            coords.bottom = conth - coords.top - border.top - border.bottom - padding.top - padding.bottom - margin.top - margin.bottom - content.height;
        } else if (!topset) {
            coords.top = conth - coords.bottom - border.top - border.bottom - padding.top - padding.bottom - margin.top - margin.bottom - content.height;
        } else if (!bottomset) {
            coords.bottom = conth - coords.top - border.top - border.bottom - padding.top - padding.bottom - margin.top - margin.bottom - content.height;
        } else {
            if (auto) content.height = conth - coords.top - coords.bottom - border.top - border.bottom - padding.top - padding.bottom - margin.top - margin.bottom; else coords.bottom = conth - coords.top - border.top - border.bottom - padding.top - padding.bottom - margin.top - margin.bottom - content.height;
        }
    }
