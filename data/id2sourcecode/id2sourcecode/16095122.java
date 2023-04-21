    public static void moveTab(JTabbedPane tabPane, int srcIndex, int dstIndex) {
        int cnt = tabPane.getTabCount();
        Component[] components = new Component[cnt];
        for (int i = 0; i < cnt; i++) {
            components[i] = tabPane.getComponent(i);
        }
        String[] captions = new String[cnt];
        for (int i = 0; i < cnt; i++) {
            captions[i] = tabPane.getTitleAt(i);
        }
        Icon[] icons = new Icon[cnt];
        for (int i = 0; i < cnt; i++) {
            icons[i] = tabPane.getIconAt(i);
        }
        Component srcComp = components[srcIndex];
        String srcCap = captions[srcIndex];
        Icon srcIcon = icons[srcIndex];
        if (srcIndex > dstIndex) {
            for (int i = srcIndex; i > dstIndex; i--) {
                components[i] = components[i - 1];
                captions[i] = captions[i - 1];
                icons[i] = icons[i - 1];
            }
        } else {
            for (int i = srcIndex; i < dstIndex; i++) {
                components[i] = components[i + 1];
                captions[i] = captions[i + 1];
                icons[i] = icons[i + 1];
            }
        }
        components[dstIndex] = srcComp;
        captions[dstIndex] = srcCap;
        icons[dstIndex] = srcIcon;
        tabPane.removeAll();
        for (int i = 0; i < cnt; i++) {
            tabPane.addTab(captions[i], icons[i], components[i]);
        }
        tabPane.setSelectedIndex(dstIndex);
        tabPane.repaint();
    }
