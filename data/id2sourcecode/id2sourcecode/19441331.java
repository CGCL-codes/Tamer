    private void paintMnemonic(Graphics2D g, int srcX, int srcY, int destX, int destY, Color color, boolean isFilled) {
        int midX = (srcX + destX) / 2;
        int midY = (srcY + destY) / 2;
        if (!isFilled) {
            g.setColor(Color.WHITE);
            g.fillOval(midX - HALF_CIRCLE_DIAMETER, midY - HALF_CIRCLE_DIAMETER, DEFAULT_CIRCLE_DIAMETER, DEFAULT_CIRCLE_DIAMETER);
            g.setColor(color);
            g.drawOval(midX - HALF_CIRCLE_DIAMETER, midY - HALF_CIRCLE_DIAMETER, DEFAULT_CIRCLE_DIAMETER, DEFAULT_CIRCLE_DIAMETER);
        } else {
            g.setColor(color);
            g.fillOval(midX - HALF_CIRCLE_DIAMETER, midY - HALF_CIRCLE_DIAMETER, DEFAULT_CIRCLE_DIAMETER, DEFAULT_CIRCLE_DIAMETER);
        }
        String mnemonic = (String) this.relation.getAttribute("mnemonic");
        if (!mnemonic.equals("")) {
            g.setColor(Color.black);
            g.setFont(this.font);
            g.drawString(mnemonic, midX - 8, midY + 9);
        }
    }
