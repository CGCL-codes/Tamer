    Image getLogo() {
        InputStream is;
        ByteArrayOutputStream baos;
        Image logo = null;
        if ((is = this.getClass().getResourceAsStream("/images/logo_top.gif")) != null) {
            logoPlacement = GridBagConstraints.NORTH;
        } else if ((is = this.getClass().getResourceAsStream("/images/logo_left.gif")) != null) {
            logoPlacement = GridBagConstraints.WEST;
        } else if ((is = this.getClass().getResourceAsStream("/images/logo_right.gif")) != null) {
            logoPlacement = GridBagConstraints.EAST;
        } else if ((is = this.getClass().getResourceAsStream("/images/logo_bottom.gif")) != null) {
            logoPlacement = GridBagConstraints.SOUTH;
        } else {
            return null;
        }
        baos = new ByteArrayOutputStream();
        try {
            int c;
            while ((c = is.read()) >= 0) baos.write(c);
            logo = Toolkit.getDefaultToolkit().createImage(baos.toByteArray());
        } catch (IOException e) {
        }
        return logo;
    }
