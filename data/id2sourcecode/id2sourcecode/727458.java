    public void paint(Graphics g) {
        treeList.removeAllElements();
        cells.removeAllElements();
        makeListFromTree(root, new Vector(0, 1));
        Dimension size = getSize();
        if (null == img_gfx || size.width != image.getWidth(this) || size.height != image.getHeight(this)) {
            createImageBuffer(size);
        }
        img_gfx.setColor(getBackground());
        img_gfx.fillRect(0, 0, size.width, size.height);
        img_gfx.setColor(getForeground());
        FontMetrics fm = img_gfx.getFontMetrics();
        int x = 0;
        int y = 0;
        int line = 0;
        int lineHeight = fm.getHeight() + 1;
        int baseline = fm.getMaxDescent();
        for (Enumeration e = treeList.elements(); e.hasMoreElements(); ) {
            TreeListEntry entry = (TreeListEntry) e.nextElement();
            Vector prefixes = entry.getPrefixes();
            String prestring = "";
            for (Enumeration p = prefixes.elements(); p.hasMoreElements(); ) {
                Integer i = (Integer) p.nextElement();
                switch(i.intValue()) {
                    case VisualTable.COLLAPSED:
                        img_gfx.drawImage(imgCollapsed, 2, line * lineHeight, 13, 13, this);
                        break;
                    case VisualTable.EXPANDED:
                        img_gfx.drawImage(imgExpanded, 2, line * lineHeight, 13, 13, this);
                        break;
                    case VisualTable.ENTRY:
                        break;
                    case VisualTable.LEAF:
                        img_gfx.drawImage(imgLeaf, 2, line * lineHeight, 13, 13, this);
                        break;
                }
            }
            boolean node_selected = entry.getNode().isSelected();
            String content = prestring + entry.getContent();
            int x1 = 2 + 13 + 2;
            int x2 = x1 + fm.stringWidth(content);
            int y1 = line * lineHeight;
            int y2 = (line + 1) * lineHeight - 1;
            cells.addElement(new SimpleRectangle(x1, y1, x2, y2, entry.getNode()));
            if (node_selected) {
                img_gfx.setColor(selbg);
                img_gfx.fillRect(x1, y1, size.width, lineHeight);
                img_gfx.setColor(getForeground());
            }
            if (node_selected || null != eirc.getChannel(entry.getContent())) {
                img_gfx.setColor(selfg);
            }
            img_gfx.drawString(entry.getContent(), x1 + fm.stringWidth(prestring), y2 - baseline);
            img_gfx.setColor(getForeground());
            line++;
        }
        int maxindent = 0;
        for (Enumeration e = cells.elements(); e.hasMoreElements(); ) {
            SimpleRectangle cell = (SimpleRectangle) e.nextElement();
            if (maxindent < cell.getX2()) {
                maxindent = cell.getX2();
            }
        }
        line = 0;
        for (Enumeration e = treeList.elements(); e.hasMoreElements(); ) {
            TreeListEntry entry = (TreeListEntry) e.nextElement();
            if (!entry.getNode().isNode()) {
                String users = entry.getNode().getUsers() + users_postfix;
                int x1 = maxindent + 6;
                int x2 = x1 + fm.stringWidth(users);
                int y1 = line * lineHeight;
                int y2 = (line + 1) * lineHeight;
                cells.addElement(new SimpleRectangle(x1, y1, x2, y2, entry.getNode()));
                if (entry.getNode().isSelected() || null != eirc.getChannel(entry.getContent())) {
                    img_gfx.setColor(selfg);
                }
                img_gfx.drawString(users, x1, y2 - baseline);
                img_gfx.setColor(getForeground());
            }
            line++;
        }
        for (Enumeration e = cells.elements(); e.hasMoreElements(); ) {
            SimpleRectangle cell = (SimpleRectangle) e.nextElement();
            if (maxindent < cell.getX2()) {
                maxindent = cell.getX2();
            }
        }
        line = 0;
        for (Enumeration e = treeList.elements(); e.hasMoreElements(); ) {
            TreeListEntry entry = (TreeListEntry) e.nextElement();
            if (!entry.getNode().isNode()) {
                String topic = entry.getNode().getDescription();
                int x1 = maxindent + 6;
                int x2 = x1 + fm.stringWidth(topic);
                int y1 = line * lineHeight;
                int y2 = (line + 1) * lineHeight;
                cells.addElement(new SimpleRectangle(x1, y1, x2, y2, entry.getNode()));
                if (entry.getNode().isSelected() || null != eirc.getChannel(entry.getContent())) {
                    img_gfx.setColor(selfg);
                }
                img_gfx.drawString(topic, x1, y2 - baseline);
                img_gfx.setColor(getForeground());
            }
            line++;
        }
        Dimension preferred_size = getPreferredSize();
        if (!preferred_size.equals(size)) {
            setSize(preferred_size);
            repaint();
            superscrollpane.doLayout();
            return;
        }
        g.drawImage(image, 0, 0, this);
    }
