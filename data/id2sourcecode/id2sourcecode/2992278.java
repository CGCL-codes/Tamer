    public void layoutContainer(Container target) {
        Insets insets = target.getInsets();
        Dimension dim = target.getSize();
        int cavityX = 0, cavityY = 0;
        int cavityWidth = dim.width - (insets.left + insets.right);
        int cavityHeight = dim.height - (insets.top + insets.bottom);
        int frameX, frameY, frameWidth, frameHeight;
        int width, height, x, y;
        PackTable current;
        if (cavityWidth < 0) cavityWidth = 0;
        if (cavityHeight < 0) cavityHeight = 0;
        if (debug > 0) {
            System.out.println("Laying out container at size: " + String.valueOf(cavityWidth) + "x" + String.valueOf(cavityHeight));
        }
        for (current = firstcomp; current != null; current = current.next) {
            int padx = current.padx * 2;
            int pady = current.pady * 2;
            int ipadx = current.ipadx;
            int ipady = current.ipady;
            boolean fillx = current.fillx;
            boolean filly = current.filly;
            int minHeight = current.height;
            int minWidth = current.width;
            if (minHeight == -1) minHeight = current.comp.getMinimumSize().height;
            if (minWidth == -1) minWidth = current.comp.getMinimumSize().width;
            current.comp.doLayout();
            if (current.side == ISIDE_TOP || current.side == ISIDE_BOTTOM) {
                frameWidth = cavityWidth;
                frameHeight = minHeight + pady + ipady;
                if (current.expand) frameHeight += YExpansion(current, cavityHeight);
                cavityHeight -= frameHeight;
                if (cavityHeight < 0) {
                    frameHeight += cavityHeight;
                    cavityHeight = 0;
                }
                frameX = cavityX;
                if (current.side == ISIDE_TOP) {
                    frameY = cavityY;
                    cavityY += frameHeight;
                } else frameY = cavityY + cavityHeight;
            } else {
                frameHeight = cavityHeight;
                frameWidth = minWidth + padx + ipadx;
                if (current.expand) frameWidth += XExpansion(current, cavityWidth);
                cavityWidth -= frameWidth;
                if (cavityWidth < 0) {
                    frameWidth += cavityWidth;
                    cavityWidth = 0;
                }
                frameY = cavityY;
                if (current.side == ISIDE_LEFT) {
                    frameX = cavityX;
                    cavityX += frameWidth;
                } else frameX = cavityX + cavityWidth;
            }
            width = minWidth + ipadx;
            if (fillx && (width < (frameWidth - padx))) width = frameWidth - padx;
            height = minHeight + ipady;
            if (filly && (height < (frameHeight - pady))) height = frameHeight - pady;
            padx /= 2;
            pady /= 2;
            switch(current.anchor) {
                case ANCHOR_N:
                    x = frameX + (frameWidth - width) / 2;
                    y = frameY + pady;
                    break;
                case ANCHOR_NE:
                    x = frameX + frameWidth - width - padx;
                    y = frameY + pady;
                    break;
                case ANCHOR_E:
                    x = frameX + frameWidth - width - padx;
                    y = frameY + (frameHeight - height) / 2;
                    break;
                case ANCHOR_SE:
                    x = frameX + frameWidth - width - padx;
                    y = frameY + frameHeight - height - pady;
                    break;
                case ANCHOR_S:
                    x = frameX + (frameWidth - width) / 2;
                    y = frameY + frameHeight - height - pady;
                    break;
                case ANCHOR_SW:
                    x = frameX + padx;
                    y = frameY + frameHeight - height - pady;
                    break;
                case ANCHOR_W:
                    x = frameX + padx;
                    y = frameY + (frameHeight - height) / 2;
                    break;
                case ANCHOR_NW:
                    x = frameX + padx;
                    y = frameY + pady;
                    break;
                case ANCHOR_CENTER:
                default:
                    x = frameX + (frameWidth - width) / 2;
                    y = frameY + (frameHeight - height) / 2;
                    break;
            }
            if (debug > 0 || current.debug > 0) {
                System.out.println("size: " + width + "x" + height);
            }
            current.comp.setBounds(insets.left + x, y + insets.top, width, height);
        }
    }
