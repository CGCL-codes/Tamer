    public MFrame(int additionalWidth, int additionalHeight) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH = screenSize.width;
        SCREEN_HEIGHT = screenSize.height;
        this.additionalWidth = additionalWidth;
        this.additionalHeight = additionalHeight;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameWidth = (SCREEN_WIDTH + additionalWidth) / 2;
        frameHeight = (SCREEN_HEIGHT + additionalHeight) / 2;
        this.setPreferredSize(new Dimension(frameWidth, frameHeight));
        this.setSize(this.getPreferredSize());
        this.pack();
        this.setBackground(Color.LIGHT_GRAY);
        this.setVisible(true);
        setFrame();
    }
