    protected void calculateAlignmentValues() {
        textMin = 0;
        textMax = _0130_width;
        switch(style) {
            case ICON_ONLY:
            case TEXT_AND_ICON:
                iconY = (_0131_height - iconHeight) / 2;
                switch(iconAlign) {
                    case LEFT:
                        iconX = 0;
                        textMin = iconWidth;
                        break;
                    case CENTER:
                        iconX = (_0130_width - iconWidth) / 2;
                        break;
                    case RIGHT:
                        iconX = _0130_width - iconWidth;
                        textMax = iconX;
                        break;
                }
                break;
        }
        if (style == TEXT_ONLY || style == TEXT_AND_ICON) {
            switch(textHAlign) {
                case LEFT:
                    textX = textMin;
                    break;
                case CENTER:
                    textX = textMin + (textMax - textMin - textWidth) / 2;
                    break;
                case RIGHT:
                    textX = textMax - textWidth;
                    break;
            }
            switch(textVAlign) {
                case TOP:
                    textY = textHeight + 1;
                    break;
                case MIDDLE:
                    textY = (_0131_height + textHeight) / 2;
                    break;
                case BOTTOM:
                    textY = _0131_height - 1;
                    break;
            }
        }
    }
