    SplashWindow(String ruta, GLCanvas barra, DataGame dataGame) {
        super(null);
        this.setLayout(null);
        Image splashImage = Imagen.cargarImagen(ruta);
        Rectangle maxWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int w = splashImage.getWidth(null);
        int h = splashImage.getHeight(null);
        this.setBounds(new Rectangle((maxWindowBounds.width - w) / 2, (maxWindowBounds.height - h) / 2, w, h));
        if (ruta.toLowerCase().endsWith(".jpg")) fondo = splashImage; else {
            BufferedImage captura = null;
            try {
                Robot robot = new Robot();
                captura = robot.createScreenCapture(this.getBounds());
                captura.getGraphics().drawImage(splashImage, 0, 0, null);
            } catch (AWTException ignorada) {
            }
            fondo = captura;
        }
        loading = new ModificableBoolean(true);
        this.barra = barra;
        this.barra.setBounds(50, h - 10, w - 100, 5);
        this.barra.addGLEventListener(new GLEventListenerLoader(dataGame, loading));
        this.add(barra);
    }
