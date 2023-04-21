    void draw(Theme theme, GC gc, Rectangle bounds) {
        int buttonHandle = theme.buttonHandle;
        int gtkStyle = OS.gtk_widget_get_style(buttonHandle);
        int drawable = gc.getGCData().drawable;
        theme.transferClipping(gc, gtkStyle);
        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = bounds.height;
        int shadow_type = OS.GTK_SHADOW_OUT;
        if ((state[DrawData.COMBO_ARROW] & DrawData.PRESSED) != 0) shadow_type = OS.GTK_SHADOW_IN;
        int state_type = getStateType(DrawData.COMBO_ARROW);
        int relief = OS.gtk_button_get_relief(buttonHandle);
        int interior_focus = theme.getWidgetProperty(buttonHandle, "interior-focus");
        int focus_line_width = theme.getWidgetProperty(buttonHandle, "focus-line-width");
        int focus_padding = theme.getWidgetProperty(buttonHandle, "focus-padding");
        int xthickness = OS.gtk_style_get_xthickness(gtkStyle);
        int ythickness = OS.gtk_style_get_xthickness(gtkStyle);
        int arrow_width = MIN_ARROW_SIZE;
        int arrow_height = ARROW_HEIGHT;
        int x_border = xthickness + focus_padding;
        int y_border = ythickness + focus_padding;
        if (interior_focus == 0) {
            x_border += focus_line_width;
            y_border += focus_line_width;
        }
        int arrow_button_width = arrow_width + x_border * 2;
        int arrow_button_x = x + width - arrow_button_width;
        int arrow_x = arrow_button_x + (arrow_button_width - arrow_width) / 2;
        int arrow_y = y + (height - arrow_height) / 2 + 1;
        if (relief != OS.GTK_RELIEF_NONE || ((state[DrawData.COMBO_ARROW] & (DrawData.PRESSED | DrawData.HOT)) != 0)) {
            byte[] detail = Converter.wcsToMbcs(null, "button", true);
            OS.gtk_paint_box(gtkStyle, drawable, state_type, shadow_type, null, buttonHandle, detail, arrow_button_x, y, arrow_button_width, height);
        }
        byte[] arrow_detail = Converter.wcsToMbcs(null, "arrow", true);
        int arrowHandle = theme.arrowHandle;
        OS.gtk_paint_arrow(gtkStyle, drawable, state_type, OS.GTK_SHADOW_OUT, null, arrowHandle, arrow_detail, OS.GTK_ARROW_DOWN, true, arrow_x, arrow_y, arrow_width, arrow_height);
        int entryHandle = theme.entryHandle;
        gtkStyle = OS.gtk_widget_get_style(entryHandle);
        theme.transferClipping(gc, gtkStyle);
        state_type = getStateType(DrawData.WIDGET_WHOLE);
        byte[] detail = Converter.wcsToMbcs(null, "entry", true);
        OS.gtk_paint_shadow(gtkStyle, drawable, OS.GTK_STATE_NORMAL, OS.GTK_SHADOW_IN, null, entryHandle, detail, x, y, width - arrow_button_width, height);
        xthickness = OS.gtk_style_get_xthickness(gtkStyle);
        ythickness = OS.gtk_style_get_xthickness(gtkStyle);
        x += xthickness;
        y += ythickness;
        width -= 2 * xthickness;
        height -= 2 * ythickness;
        detail = Converter.wcsToMbcs(null, "entry_bg", true);
        OS.gtk_paint_flat_box(gtkStyle, drawable, state_type, OS.GTK_SHADOW_NONE, null, entryHandle, detail, x, y, width - arrow_button_width, height);
        if (clientArea != null) {
            clientArea.x = x;
            clientArea.y = y;
            clientArea.width = width - arrow_button_width;
            clientArea.height = height;
        }
    }
