    private void loadTheme(String themefile) {
        PropertiesMetalTheme myTheme = null;
        URL url = this.getClass().getResource(themefile);
        if (url != null) {
            try {
                InputStream is = url.openStream();
                try {
                    myTheme = new PropertiesMetalTheme(is);
                } finally {
                    is.close();
                }
                if (myTheme != null) {
                    MetalLookAndFeel.setCurrentTheme(myTheme);
                }
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            } catch (Exception ex) {
                System.out.println("Failed loading Metal");
                System.out.println(ex);
            }
        }
    }
