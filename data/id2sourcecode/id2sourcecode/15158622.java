    public void layoutContainer(Container target) {
        Insets insets = target.getInsets();
        Dimension dim = target.getSize();
        int cavityX = 0, cavityY = 0;
        int cavityWidth = dim.width - (insets.left + insets.right);
        int cavityHeight = dim.height - (insets.top + insets.bottom);
        int frameX, frameY, frameWidth, frameHeight;
        int width, height, x, y;
        Component current = firstcomp;
        if (debug > 0) {
            System.out.println("Laying out container at size: " + String.valueOf(cavityWidth) + "x" + String.valueOf(cavityHeight));
        }
        while (current != null) {
            Hashtable ptable = (Hashtable) compinfo.get(current);
            String side = (String) ptable.get(F_SIDE);
            int padx = ((Integer) ptable.get(F_PADX)).intValue() * 2;
            int pady = ((Integer) ptable.get(F_PADY)).intValue() * 2;
            int ipadx = ((Integer) ptable.get(F_IPADX)).intValue();
            int ipady = ((Integer) ptable.get(F_IPADY)).intValue();
            boolean expand = ((Boolean) ptable.get(F_EXPAND)).booleanValue();
            boolean fillx = ((Boolean) ptable.get(F_FILLX)).booleanValue();
            boolean filly = ((Boolean) ptable.get(F_FILLY)).booleanValue();
            int anchor = ((Integer) ptable.get(F_ANCHOR)).intValue();
            String name = (String) ptable.get(F_NAME);
            current.doLayout();
            if (side.equals(SIDE_TOP) || side.equals(SIDE_BOTTOM)) {
                frameWidth = cavityWidth;
                frameHeight = current.getPreferredSize().height + pady + ipady;
                if (expand) frameHeight += YExpansion(current, cavityHeight);
                cavityHeight -= frameHeight;
                if (cavityHeight < 0) {
                    frameHeight += cavityHeight;
                    cavityHeight = 0;
                }
                frameX = cavityX;
                if (side.equals(SIDE_TOP)) {
                    frameY = cavityY;
                    cavityY += frameHeight;
                } else {
                    frameY = cavityY + cavityHeight;
                }
            } else {
                frameHeight = cavityHeight;
                frameWidth = current.getPreferredSize().width + padx + ipadx;
                if (expand) frameWidth += XExpansion(current, cavityWidth);
                cavityWidth -= frameWidth;
                if (cavityWidth < 0) {
                    frameWidth += cavityWidth;
                    cavityWidth = 0;
                }
                frameY = cavityY;
                if (side.equals(SIDE_LEFT)) {
                    frameX = cavityX;
                    cavityX += frameWidth;
                } else {
                    frameX = cavityX + cavityWidth;
                }
            }
            width = current.getPreferredSize().width + ipadx;
            if (fillx || (width > (frameWidth - padx))) width = frameWidth - padx;
            height = current.getPreferredSize().height + ipady;
            if (filly || (height > (frameHeight - pady))) height = frameHeight - pady;
            padx /= 2;
            pady /= 2;
            switch(anchor) {
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
            if (debug > 0) {
                System.out.println("Component size: " + String.valueOf(width) + "x" + String.valueOf(height));
            }
            current.setBounds(insets.left + x, y + insets.top, width, height);
            current = (Component) ptable.get(COMPONENT_NEXT);
        }
    }
