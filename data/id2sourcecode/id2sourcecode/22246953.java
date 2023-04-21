    public MediaServerFrame(BundleContext bc) {
        super(TITLE);
        this.bc = bc;
        try {
            File f = new File("qos-mediaserver_description.xml");
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
            msDev = new MediaServerDevice(f, bc);
        } catch (InvalidDescriptionException e) {
            Debug.warning(e);
        }
        getContentPane().setLayout(new BorderLayout());
        msPane = new MediaServerPane(msDev);
        msDev.setComponent(msPane);
        getContentPane().add(msPane, BorderLayout.CENTER);
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
    }
