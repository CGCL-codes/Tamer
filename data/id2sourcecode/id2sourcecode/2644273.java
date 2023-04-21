        public void actionPerformed(ActionEvent e) {
            RegionSelector selector = new RegionSelector();
            selector.run(bounds);
            bounds = selector.getBounds();
            if (bounds.width > 0 && bounds.height > 0) {
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        try {
                            BufferedImage image = new Robot().createScreenCapture(bounds);
                            TokenTool.getFrame().getTokenCompositionPanel().setToken(image);
                        } catch (AWTException ae) {
                            ae.printStackTrace();
                        }
                    }
                });
            }
        }
