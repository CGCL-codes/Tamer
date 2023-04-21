    protected void paintMenuItemOneIcon(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon, Color background, Color foreground, int iconGap) {
        JMenuItem b = (JMenuItem) c;
        ButtonModel model = b.getModel();
        JComponent parent = (JComponent) b.getParent();
        Integer val = (Integer) parent.getClientProperty(MAX_ACC_WIDTH);
        int maxAccWidth = (val == null ? 0 : val.intValue());
        val = (Integer) parent.getClientProperty(MAX_ICON_WIDTH);
        int maxIconWidth = (val == null ? 0 : val.intValue());
        val = (Integer) parent.getClientProperty(MAX_LABEL_WIDTH);
        int maxLabelWidth = (val == null ? 0 : val.intValue());
        val = (Integer) parent.getClientProperty(MAX_TEXT_WIDTH);
        int maxTextWidth = (val == null ? 0 : val.intValue());
        int menuWidth = b.getWidth();
        int menuHeight = b.getHeight();
        Insets insets = c.getInsets();
        boolean isTopLevelMenu = isTopLevelMenu();
        boolean isLeftToRight = c.getComponentOrientation().isLeftToRight();
        int horizontalAlignment = b.getHorizontalAlignment();
        int horizontalTextPosition = b.getHorizontalTextPosition();
        resetRects();
        viewRect.setBounds(0, 0, menuWidth, menuHeight);
        viewRect.x += insets.left;
        viewRect.y += insets.top;
        viewRect.width -= insets.right + insets.left;
        viewRect.height -= insets.bottom + insets.top;
        Font holdf = g.getFont();
        Font f = c.getFont();
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics(f);
        FontMetrics fmAccel = g.getFontMetrics(acceleratorFont);
        String acceleratorText = getAcceleratorText(b.getAccelerator());
        Icon ic = b.getIcon();
        Icon cIcon = null;
        Icon paintIcon = ic;
        if (b instanceof JCheckBoxMenuItem || b instanceof JRadioButtonMenuItem) {
            if (ic == null) {
                cIcon = checkIcon;
            }
        }
        String text = layoutMenuItem(fm, b.getText(), fmAccel, acceleratorText, ic, cIcon, arrowIcon, b.getVerticalAlignment(), b.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect, iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect, b.getText() == null ? 0 : iconGap, false);
        if (!isTopLevelMenu) {
            if (VERBOSE) System.out.println("*** " + getSwingConstantsString(horizontalTextPosition, horizontalAlignment) + ", menu.size=" + menuWidth + ", " + menuHeight);
            if (isLeftToRight) {
                checkIconRect.x = insets.left;
                acceleratorRect.x = insets.left + maxLabelWidth + DEFAULT_ICON_GAP * 2;
                if (horizontalTextPosition == SwingConstants.RIGHT || horizontalTextPosition == SwingConstants.TRAILING) {
                    iconRect.x = checkIconRect.x;
                    textRect.x = iconRect.x + maxIconWidth + (maxIconWidth == 0 ? 0 : DEFAULT_ICON_GAP);
                    if (horizontalAlignment == SwingConstants.RIGHT || horizontalAlignment == SwingConstants.TRAILING) {
                        int right = insets.left + maxLabelWidth;
                        int xd = right - (textRect.x + textRect.width);
                        textRect.x = right - textRect.width;
                        iconRect.x = textRect.x - DEFAULT_ICON_GAP - iconRect.width;
                    } else if (horizontalAlignment == SwingConstants.CENTER) {
                        int labelWidth = iconRect.width + (iconRect.width == 0 ? 0 : DEFAULT_ICON_GAP) + textRect.width;
                        iconRect.x = insets.left + (maxLabelWidth - labelWidth) / 2;
                        textRect.x = iconRect.x + iconRect.width + (iconRect.width == 0 ? 0 : DEFAULT_ICON_GAP);
                    }
                } else if (horizontalTextPosition == SwingConstants.LEFT || horizontalTextPosition == SwingConstants.LEADING) {
                    textRect.x = insets.left + maxIconWidth + (maxIconWidth == 0 ? 0 : DEFAULT_ICON_GAP);
                    iconRect.x = textRect.x + textRect.width + DEFAULT_ICON_GAP;
                    if (horizontalAlignment == SwingConstants.RIGHT || horizontalAlignment == SwingConstants.TRAILING) {
                        int right = insets.left + maxLabelWidth;
                        int xd = right - (textRect.x + textRect.width);
                        iconRect.x = right - iconRect.width;
                        textRect.x = iconRect.x - DEFAULT_ICON_GAP - textRect.width;
                    } else if (horizontalAlignment == SwingConstants.CENTER) {
                        int labelWidth = iconRect.width + (iconRect.width == 0 ? 0 : DEFAULT_ICON_GAP) + textRect.width;
                        textRect.x = insets.left + (maxLabelWidth - labelWidth) / 2;
                        iconRect.x = textRect.x + textRect.width + DEFAULT_ICON_GAP;
                    }
                } else if (horizontalTextPosition == SwingConstants.CENTER) {
                    int labelWidth = (textRect.width > iconRect.width ? textRect.width : iconRect.width);
                    int labelx = insets.left + maxIconWidth + (maxIconWidth == 0 ? 0 : DEFAULT_ICON_GAP);
                    int xd = (textRect.width > iconRect.width ? labelx - textRect.x : labelx - iconRect.x);
                    iconRect.x += xd;
                    textRect.x += xd;
                    if (horizontalAlignment == SwingConstants.RIGHT || horizontalAlignment == SwingConstants.TRAILING) {
                        int right = insets.left + maxLabelWidth;
                        labelx = right - labelWidth;
                        xd = (textRect.width > iconRect.width ? labelx - textRect.x : labelx - iconRect.x);
                        iconRect.x += xd;
                        textRect.x += xd;
                    } else if (horizontalAlignment == SwingConstants.CENTER) {
                        labelx = insets.left + (maxLabelWidth - labelWidth) / 2;
                        xd = (textRect.width > iconRect.width ? labelx - textRect.x : labelx - iconRect.x);
                        iconRect.x += xd;
                        textRect.x += xd;
                    }
                }
            } else {
                checkIconRect.x = viewRect.x + viewRect.width - CHECK_WIDTH + DEFAULT_ICON_GAP;
                acceleratorRect.x = insets.left + ARROW_WIDTH + maxAccWidth - acceleratorRect.width;
                if (horizontalTextPosition == SwingConstants.RIGHT || horizontalTextPosition == SwingConstants.TRAILING) {
                    iconRect.x = viewRect.x + viewRect.width - iconRect.width;
                    textRect.x = checkIconRect.x - DEFAULT_ICON_GAP - maxIconWidth - (maxIconWidth == 0 ? 0 : DEFAULT_ICON_GAP) - textRect.width;
                    if (horizontalAlignment == SwingConstants.RIGHT || horizontalAlignment == SwingConstants.TRAILING) {
                        int left = insets.left + ARROW_WIDTH + maxAccWidth + (maxAccWidth == 0 ? 0 : DEFAULT_ACC_GAP);
                        textRect.x = left;
                        iconRect.x = left + textRect.width + DEFAULT_ICON_GAP;
                    } else if (horizontalAlignment == SwingConstants.CENTER) {
                        int left = insets.left + ARROW_WIDTH + maxAccWidth + (maxAccWidth == 0 ? 0 : DEFAULT_ACC_GAP);
                        int labelWidth = iconRect.width + (iconRect.width == 0 ? 0 : DEFAULT_ICON_GAP) + textRect.width;
                        textRect.x = left + (maxLabelWidth - labelWidth) / 2;
                        iconRect.x = textRect.x + textRect.width + DEFAULT_ICON_GAP;
                    }
                } else if (horizontalTextPosition == SwingConstants.LEFT || horizontalTextPosition == SwingConstants.LEADING) {
                    textRect.x = checkIconRect.x - DEFAULT_ICON_GAP - textRect.width - maxIconWidth - (maxIconWidth == 0 ? 0 : DEFAULT_ICON_GAP);
                    iconRect.x = textRect.x - DEFAULT_ICON_GAP - iconRect.width;
                    if (horizontalAlignment == SwingConstants.RIGHT || horizontalAlignment == SwingConstants.TRAILING) {
                        int left = insets.left + ARROW_WIDTH + maxAccWidth + (maxAccWidth == 0 ? 0 : DEFAULT_ACC_GAP);
                        iconRect.x = left;
                        textRect.x = left + iconRect.width + DEFAULT_ICON_GAP;
                    } else if (horizontalAlignment == SwingConstants.CENTER) {
                        int left = insets.left + ARROW_WIDTH + maxAccWidth + (maxAccWidth == 0 ? 0 : DEFAULT_ACC_GAP);
                        int labelWidth = iconRect.width + (iconRect.width == 0 ? 0 : DEFAULT_ICON_GAP) + textRect.width;
                        iconRect.x = left + (maxLabelWidth - labelWidth) / 2;
                        textRect.x = iconRect.x + iconRect.width + DEFAULT_ICON_GAP;
                    }
                } else if (horizontalTextPosition == SwingConstants.CENTER) {
                    int left = insets.left + ARROW_WIDTH + maxAccWidth + (maxAccWidth == 0 ? 0 : DEFAULT_ACC_GAP);
                    int labelWidth = (textRect.width > iconRect.width ? textRect.width : iconRect.width);
                    int labelx = left + maxTextWidth - labelWidth;
                    int xd = (textRect.width > iconRect.width ? labelx - textRect.x : labelx - iconRect.x);
                    iconRect.x += xd;
                    textRect.x += xd;
                    if (horizontalAlignment == SwingConstants.RIGHT || horizontalAlignment == SwingConstants.TRAILING) {
                        xd = (textRect.width > iconRect.width ? left - textRect.x : left - iconRect.x);
                        iconRect.x += xd;
                        textRect.x += xd;
                    } else if (horizontalAlignment == SwingConstants.CENTER) {
                        labelx = left + (maxLabelWidth - labelWidth) / 2;
                        xd = (textRect.width > iconRect.width ? labelx - textRect.x : labelx - iconRect.x);
                        iconRect.x += xd;
                        textRect.x += xd;
                    }
                }
            }
        }
        if (VERBOSE && !isTopLevelMenu) {
            System.out.println("paintMenuItem() \"" + b.getText() + "\"");
            System.out.println("  checkIconRect=" + p(checkIconRect) + ", iconRect=" + p(iconRect) + ", textRect=" + p(textRect) + ", acceleratorRect=" + p(acceleratorRect) + ", arrowIconRect=" + p(arrowIconRect) + ", viewRect=" + p(viewRect));
            System.out.println("  maxIconWidth=" + maxIconWidth + ", maxTextWidth=" + maxTextWidth + ", maxAccWidth=" + maxAccWidth + ", maxLabelWidth=" + maxLabelWidth);
        }
        paintBackground(g, b, background, isLeftToRight);
        Color holdc = g.getColor();
        if (cIcon != null && !isTopLevelMenu) {
            if (model.isArmed() || (c instanceof JMenu && model.isSelected())) {
                g.setColor(foreground);
            } else {
                g.setColor(holdc);
            }
            cIcon.paintIcon(c, g, checkIconRect.x, checkIconRect.y);
            g.setColor(holdc);
        }
        if (paintIcon != null) {
            Icon icon = paintIcon;
            Icon selectedIcon = null;
            Icon tmpIcon = null;
            if (model.isSelected()) {
                selectedIcon = (Icon) b.getSelectedIcon();
                if (selectedIcon != null) {
                    icon = selectedIcon;
                }
            }
            if (!model.isEnabled()) {
                if (model.isSelected()) {
                    tmpIcon = b.getDisabledSelectedIcon();
                    if (tmpIcon == null) {
                        tmpIcon = selectedIcon;
                    }
                }
                if (tmpIcon == null) {
                    tmpIcon = b.getDisabledIcon();
                }
            } else if (model.isPressed() && model.isArmed()) {
                tmpIcon = b.getPressedIcon();
            } else if (model.isArmed()) {
                if (model.isSelected()) {
                    tmpIcon = b.getRolloverSelectedIcon();
                    if (tmpIcon == null) {
                        tmpIcon = selectedIcon;
                    }
                }
                if (tmpIcon == null) {
                    tmpIcon = b.getRolloverIcon();
                }
            }
            if (tmpIcon != null) {
                icon = tmpIcon;
            }
            if (icon != null) {
                icon.paintIcon(c, g, iconRect.x, iconRect.y);
                if (((b instanceof JCheckBoxMenuItem) || (b instanceof JRadioButtonMenuItem)) && b.getSelectedIcon() == null && model.isSelected()) {
                    paintSelected(g, b);
                }
            }
        }
        if (text != null) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            g.setColor(Theme.menuItemFontColor.getColor());
            if (v != null) {
                v.paint(g, textRect);
            } else {
                paintText(g, b, textRect, text);
            }
        }
        if (!"".equals(acceleratorText)) {
            g.setFont(acceleratorFont);
            if (!model.isEnabled()) {
                g.setColor(Theme.menuItemDisabledFgColor.getColor());
                BasicGraphicsUtils.drawString(g, acceleratorText, 0, acceleratorRect.x, acceleratorRect.y + fmAccel.getAscent());
            } else {
                if (model.isArmed() || (c instanceof JMenu && model.isSelected())) {
                    g.setColor(Theme.menuItemSelectedTextColor.getColor());
                } else {
                    g.setColor(Theme.menuItemFontColor.getColor());
                }
                BasicGraphicsUtils.drawString(g, acceleratorText, 0, acceleratorRect.x, acceleratorRect.y + fmAccel.getAscent());
            }
        }
        if (arrowIcon != null) {
            if (model.isArmed() || (c instanceof JMenu && model.isSelected())) {
                g.setColor(foreground);
            }
            if (!isTopLevelMenu) {
                arrowIcon.paintIcon(c, g, arrowIconRect.x, arrowIconRect.y);
            }
        }
        g.setColor(holdc);
        g.setFont(holdf);
    }
