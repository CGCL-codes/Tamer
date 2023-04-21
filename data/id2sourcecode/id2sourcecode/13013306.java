    private void createLayout() throws FontFormatException, IOException {
        int dx = 23;
        int dy = 105;
        daysleft.setLocation(dx, dy);
        int font_size = 85;
        String font_href = "none";
        String font_jar = "none";
        String font_name = "none";
        Font font = null;
        try {
            if (!font_jar.equals("none")) {
                u.p("font jar = " + font_jar);
                URL font_url = getClass().getResource("/fonts/" + font_jar);
                u.p("font url = " + font_url);
                font = Font.createFont(Font.TRUETYPE_FONT, font_url.openStream());
            } else if (!font_href.equals("none")) {
                u.p("font url = " + font_href);
                URL font_url = new URL(font_href);
                font = Font.createFont(Font.TRUETYPE_FONT, font_url.openStream());
            } else {
                font = new Font(font_name, Font.PLAIN, font_size);
            }
        } catch (Exception ex) {
            u.p("ex: " + ex);
            font = new Font("Arial", Font.PLAIN, font_size);
        }
        font = font.deriveFont((float) font_size);
        daysleft.setFont(font);
        daysleft.setPreferredSize(new Dimension(250, 200));
        daysleft.setSize(250, 200);
        panel.add(daysleft);
    }
