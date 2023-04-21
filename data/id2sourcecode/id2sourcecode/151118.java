    public void layoutContainer(Container target) {
        Insets insets = target.getInsets();
        Dimension dim = target.getSize();
        int cavityX = 0, cavityY = 0;
        int cavityWidth = dim.width - (insets.left + insets.right);
        int cavityHeight = dim.height - (insets.top + insets.bottom);
        int frameX, frameY, frameWidth, frameHeight;
        int width, height, x, y;
        Component current = firstcomp;
        PackRecord pr;
        int padx, pady;
        Object anchor;
        boolean fillx, filly;
        Dimension prefsize;
        while (current != null) {
            pr = (PackRecord) component_table.get(current);
            padx = pr.padx * 2;
            pady = pr.pady * 2;
            anchor = pr.anchor;
            fillx = (pr.fill == FILL_OBJ_X) || (pr.fill == FILL_OBJ_BOTH);
            filly = (pr.fill == FILL_OBJ_Y) || (pr.fill == FILL_OBJ_BOTH);
            current.doLayout();
            if (pr.side == SIDE_OBJ_TOP || pr.side == SIDE_OBJ_BOTTOM) {
                frameWidth = cavityWidth;
                frameHeight = current.getPreferredSize().height + pady + pr.ipady;
                if (pr.expand == EXPAND_OBJ_TRUE) frameHeight += YExpansion(current, cavityHeight);
                cavityHeight -= frameHeight;
                if (cavityHeight < 0) {
                    frameHeight += cavityHeight;
                    cavityHeight = 0;
                }
                frameX = cavityX;
                if (pr.side == SIDE_OBJ_TOP) {
                    frameY = cavityY;
                    cavityY += frameHeight;
                } else {
                    frameY = cavityY + cavityHeight;
                }
            } else {
                frameHeight = cavityHeight;
                frameWidth = current.getPreferredSize().width + padx + pr.ipadx;
                if (pr.expand == EXPAND_OBJ_TRUE) frameWidth += XExpansion(current, cavityWidth);
                cavityWidth -= frameWidth;
                if (cavityWidth < 0) {
                    frameWidth += cavityWidth;
                    cavityWidth = 0;
                }
                frameY = cavityY;
                if (pr.side == SIDE_OBJ_LEFT) {
                    frameX = cavityX;
                    cavityX += frameWidth;
                } else {
                    frameX = cavityX + cavityWidth;
                }
            }
            prefsize = current.getPreferredSize();
            width = prefsize.width + pr.ipadx;
            if (fillx || (width > (frameWidth - padx))) width = frameWidth - padx;
            height = prefsize.height + pr.ipady;
            if (filly || (height > (frameHeight - pady))) height = frameHeight - pady;
            padx /= 2;
            pady /= 2;
            if (anchor == ANCHOR_OBJ_N) {
                x = frameX + (frameWidth - width) / 2;
                y = frameY + pady;
            } else if (anchor == ANCHOR_OBJ_NE) {
                x = frameX + frameWidth - width - padx;
                y = frameY + pady;
            } else if (anchor == ANCHOR_OBJ_E) {
                x = frameX + frameWidth - width - padx;
                y = frameY + (frameHeight - height) / 2;
            } else if (anchor == ANCHOR_OBJ_SE) {
                x = frameX + frameWidth - width - padx;
                y = frameY + frameHeight - height - pady;
            } else if (anchor == ANCHOR_OBJ_S) {
                x = frameX + (frameWidth - width) / 2;
                y = frameY + frameHeight - height - pady;
            } else if (anchor == ANCHOR_OBJ_SW) {
                x = frameX + padx;
                y = frameY + frameHeight - height - pady;
            } else if (anchor == ANCHOR_OBJ_W) {
                x = frameX + padx;
                y = frameY + (frameHeight - height) / 2;
            } else if (anchor == ANCHOR_OBJ_NW) {
                x = frameX + padx;
                y = frameY + pady;
            } else if (anchor == ANCHOR_OBJ_C) {
                x = frameX + (frameWidth - width) / 2;
                y = frameY + (frameHeight - height) / 2;
            } else {
                throw new RuntimeException("no match for ANCHOR type");
            }
            current.setBounds(insets.left + x, y + insets.top, width, height);
            current = pr.next;
        }
    }
