        protected void init() {
            if (height < 11) height = 11;
            text_y = 2 + (height - 11) / 2;
            int text_width = fonts.getTextWidth(2, text);
            if (width < text_width + 4) width = text_width + 4;
            text_x = (width - text_width) >> 1;
        }
