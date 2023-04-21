    private void setButtons() {
        int x = PADDING, y = topHeight;
        System.out.println(y);
        for (int i = 0; i < buttons.length; i++) {
            if (i < selections.size()) {
                buttons[i].setHireling(selections.get(i));
                Dimension size = buttons[i].getPreferredSize();
                if (i % 2 == 1) {
                    x = DISPLAY_WIDTH - size.width;
                } else {
                    x = PADDING;
                }
                if (i > 1 && i % 2 == 0) {
                    y += size.height;
                    y += PADDING;
                }
                buttons[i].setBounds(x, y, size.width, size.height);
                if (i == selections.size() - 1) {
                    y += size.height;
                    y += PADDING;
                }
            } else {
                buttons[i].setBounds(0, 0, 0, 0);
            }
        }
        int height = GUIFactory.getStringHeight(btnOK, btnOK.getText());
        height += Globals.BUTTON_TOP_PADDING;
        int width = GUIFactory.getStringWidth(btnOK, btnOK.getText());
        width += Globals.BUTTON_SIDE_PADDING;
        x = PADDING + (DISPLAY_WIDTH - width) / 2;
        btnOK.setBounds(x, y, width, height);
        y += height + PADDING;
        totalHeight = y;
    }
