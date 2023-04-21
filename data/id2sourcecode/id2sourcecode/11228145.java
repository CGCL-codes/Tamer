    public Splash(String imageName) {
        super();
        this.setUndecorated(true);
        this.setBackground(Color.WHITE);
        URL imageURL = getClass().getResource("/" + imageName);
        ImageIcon icon = null;
        if (imageURL != null) icon = new ImageIcon(imageURL); else if (new File(imageName).exists()) icon = new ImageIcon(imageName);
        if (icon != null) {
            this.image = icon.getImage();
            int width = this.image.getWidth(null);
            int height = this.image.getHeight(null);
            this.setSize(width, height);
            this.setLocationRelativeTo(null);
            try {
                this.screenShot = new Robot().createScreenCapture(this.getBounds());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
