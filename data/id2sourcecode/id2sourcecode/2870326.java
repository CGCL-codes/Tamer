    protected void computeWidthsAbsolute(TermLengthOrPercent width, boolean auto, boolean exact, int contw, boolean update) {
        CSSDecoder dec = new CSSDecoder(ctx);
        if (width == null) auto = true;
        boolean mleftauto = style.getProperty("margin-left") == CSSProperty.Margin.AUTO;
        TermLengthOrPercent mleft = getLengthValue("margin-left");
        boolean mrightauto = style.getProperty("margin-right") == CSSProperty.Margin.AUTO;
        TermLengthOrPercent mright = getLengthValue("margin-right");
        preferredWidth = -1;
        if (!widthComputed) update = false;
        if (auto) {
            if (exact) wset = false;
            if (!update) content.width = dec.getLength(width, auto, 0, 0, contw);
            preferredWidth = -1;
        } else {
            if (exact) {
                wset = true;
                wrelative = width.isPercentage();
            }
            content.width = dec.getLength(width, auto, 0, 0, contw);
        }
        int constr = 0;
        if (wset) constr++;
        if (leftset) constr++;
        if (rightset) constr++;
        if (constr < 3) {
            if (mleftauto) margin.left = 0; else margin.left = dec.getLength(mleft, false, 0, 0, contw);
            if (mrightauto) margin.right = 0; else margin.right = dec.getLength(mright, false, 0, 0, contw);
        } else {
            if (mleftauto && mrightauto) {
                int rest = contw - coords.left - coords.right - border.left - border.right - padding.left - padding.right - content.width;
                margin.left = (rest + 1) / 2;
                margin.right = rest / 2;
            } else if (mleftauto) {
                margin.right = dec.getLength(mright, false, 0, 0, contw);
                margin.left = contw - coords.right - border.left - border.right - padding.left - padding.right - content.width - margin.right;
            } else if (mrightauto) {
                margin.left = dec.getLength(mright, false, 0, 0, contw);
                margin.right = contw - coords.right - border.left - border.right - padding.left - padding.right - content.width - margin.left;
            } else {
                margin.left = dec.getLength(mleft, false, 0, 0, contw);
                margin.right = dec.getLength(mright, false, 0, 0, contw);
            }
        }
        declMargin.left = margin.left;
        declMargin.right = margin.right;
        if (!leftset && !rightset) {
            leftstatic = true;
            coords.right = contw - coords.left - border.left - border.right - padding.left - padding.right - content.width - margin.left - margin.right;
        } else if (!leftset) {
            coords.left = contw - coords.right - border.left - border.right - padding.left - padding.right - content.width - margin.left - margin.right;
        } else if (!rightset) {
            coords.right = contw - coords.left - border.left - border.right - padding.left - padding.right - content.width - margin.left - margin.right;
        } else {
            if (auto) content.width = contw - coords.left - coords.right - border.left - border.right - padding.left - padding.right - margin.left - margin.right; else coords.right = contw - coords.left - border.left - border.right - padding.left - padding.right - content.width - margin.left - margin.right;
        }
    }
