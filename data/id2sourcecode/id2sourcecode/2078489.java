    public static void showPDF(String filename) {
        SwingController controller = new SwingController();
        SwingViewBuilder factory = new SwingViewBuilder(controller);
        JFrame frame = factory.buildViewerFrame();
        controller.openDocument(filename);
        frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        frame.addWindowListener(new WindowListener() {

            public void windowClosed(WindowEvent arg0) {
            }

            public void windowActivated(WindowEvent arg0) {
            }

            public void windowClosing(WindowEvent arg0) {
            }

            public void windowDeactivated(WindowEvent arg0) {
                System.exit(0);
            }

            public void windowDeiconified(WindowEvent arg0) {
            }

            public void windowIconified(WindowEvent arg0) {
            }

            public void windowOpened(WindowEvent arg0) {
            }
        });
        frame.pack();
        frame.setVisible(true);
    }
