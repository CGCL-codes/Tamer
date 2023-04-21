    protected void computeWidthsInFlow(TermLengthOrPercent width, boolean auto, boolean exact, int contw, boolean update) {
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
            margin.left = dec.getLength(mleft, mleftauto, 0, 0, contw);
            margin.right = dec.getLength(mright, mrightauto, 0, 0, contw);
            declMargin.left = margin.left;
            declMargin.right = margin.right;
            if (!update || isInFlow()) {
                content.width = contw - margin.left - border.left - padding.left - padding.right - border.right - margin.right;
                if (content.width < 0) content.width = 0;
            }
            preferredWidth = -1;
        } else {
            if (exact) {
                wset = true;
                wrelative = width.isPercentage();
            }
            content.width = dec.getLength(width, auto, 0, 0, contw);
            margin.left = dec.getLength(mleft, mleftauto, 0, 0, contw);
            margin.right = dec.getLength(mright, mrightauto, 0, 0, contw);
            declMargin.left = margin.left;
            declMargin.right = margin.right;
            boolean prefer = !width.isPercentage();
            int prefml = (mleft == null) || mleft.isPercentage() || mleftauto ? 0 : margin.left;
            int prefmr = (mright == null) || mright.isPercentage() || mrightauto ? 0 : margin.right;
            if (prefer) preferredWidth = prefml + border.left + padding.left + content.width + padding.right + border.right + prefmr;
            if (isInFlow() && prefer) {
                if (mleftauto && mrightauto) {
                    int rest = contw - content.width - border.left - padding.left - padding.right - border.right;
                    if (rest < 0) rest = 0;
                    margin.left = (rest + 1) / 2;
                    margin.right = rest / 2;
                } else if (mleftauto) {
                    margin.left = contw - content.width - border.left - padding.left - padding.right - border.right - margin.right;
                } else if (mrightauto) {
                    margin.right = contw - content.width - border.left - padding.left - padding.right - border.right - margin.left;
                    if (margin.right < 0 && cblock.canIncreaseWidth()) margin.right = 0;
                } else {
                    margin.right = contw - content.width - border.left - padding.left - padding.right - border.right - margin.left;
                    if (margin.right < 0 && cblock.canIncreaseWidth()) margin.right = 0;
                }
            }
        }
    }
