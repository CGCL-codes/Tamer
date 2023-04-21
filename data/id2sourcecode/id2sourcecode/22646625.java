    public PolicyHandlerFrame(BundleContext bc) {
        super(TITLE);
        this.bc = bc;
        try {
            File f = new File("qos-policyhandler_description.xml");
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
            phDev = new PolicyHandlerDevice(f);
            getContentPane().setLayout(new BorderLayout());
            phPane = new PolicyHandlerPane(phDev);
            getContentPane().add(phPane, BorderLayout.CENTER);
            addWindowListener(this);
        } catch (InvalidDescriptionException e) {
            Debug.warning(e);
        }
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
        this.setSize(new Dimension(280, 230));
        this.setVisible(true);
    }
