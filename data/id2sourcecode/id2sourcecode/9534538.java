    public LightFrame(BundleContext bc) {
        super(TITLE);
        this.bc = bc;
        try {
            File f = new File("light_description.xml");
            try {
                InputStream inputStream = getClass().getResourceAsStream(DESCRIPTION_FILE_URL);
                OutputStream out = new FileOutputStream(f);
                byte buf[] = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) out.write(buf, 0, len);
                out.close();
                inputStream.close();
            } catch (IOException e) {
            }
            lightDev = new LightDevice(f);
        } catch (InvalidDescriptionException e) {
            Debug.warning(e);
        }
        getContentPane().setLayout(new BorderLayout());
        lightPane = new LightPane();
        lightPane.setDevice(lightDev);
        lightDev.setComponent(lightPane);
        getContentPane().add(lightPane, BorderLayout.CENTER);
        addWindowListener(this);
        try {
            Image icon = null;
            String localPath = "/images/Icon16x16.png";
            URL url = getClass().getResource(localPath);
            if (url != null) {
                icon = Toolkit.getDefaultToolkit().createImage(url);
                setIconImage(icon);
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        pack();
        setVisible(true);
        lightDev.start();
    }
