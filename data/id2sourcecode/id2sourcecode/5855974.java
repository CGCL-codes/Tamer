    public static void run(Container<Component> canvas) {
        List<Component> frameKids = canvas.getChildren();
        Button versionInfoBtn = new Button("Show Version Info");
        versionInfoBtn.addActionListener(Button.ACTION_CLICK, new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                Map<String, String> versionInfo = Application.getPlatformVersionInfo();
                StringBuilder sb = new StringBuilder();
                for (String key : versionInfo.keySet()) {
                    sb.append(key).append('=').append(versionInfo.get(key)).append('\n');
                }
                MessageBox.confirm(sb.toString());
            }
        });
        frameKids.add(versionInfoBtn.setBounds(10, 10, 100, 30));
        Button licenseHeaderBtn = new Button("Show License Header");
        licenseHeaderBtn.addActionListener(Button.ACTION_CLICK, new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                byte[] licenseBytes = Application.getResourceBytes("class:///" + Application.class.getName() + "/resources/licenseHeader.txt");
                MessageBox.confirm(new String(licenseBytes));
            }
        });
        frameKids.add(licenseHeaderBtn.setBounds(10, 45, 125, 30));
        Button webXMLBtn = new Button("Show web.xml");
        webXMLBtn.addActionListener(Button.ACTION_CLICK, new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                try {
                    File webXML = Application.current().getRelativeFile("WEB-INF/web.xml");
                    InputStream is = new FileInputStream(webXML);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] bytes = new byte[128];
                    int size;
                    while ((size = is.read(bytes)) != -1) baos.write(bytes, 0, size);
                    is.close();
                    TextArea ta = (TextArea) new TextArea(baos.toString()).setSize(600, 400);
                    MessageBox.confirm("", "", ta, "");
                } catch (Exception e) {
                    if (e instanceof RuntimeException) throw (RuntimeException) e;
                    throw new RuntimeException(e);
                }
            }
        });
        frameKids.add(webXMLBtn.setBounds(10, 80, 100, 30));
        frameKids.add(new Label("Base Folder: " + Application.current().getBaseFolder()).setBounds(10, 115, 800, 20));
        final TextField tf = new TextField();
        tf.setEditMask("MM/dd/yyyy");
        tf.addPropertyChangeListener(TextField.PROPERTY_TEXT, new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent ev) {
                try {
                    Date d = DATE_FORMAT.parse((String) ev.getNewValue());
                    if (d.compareTo(new Date()) > 0) {
                        Application.current().addTimerTask(new Runnable() {

                            public void run() {
                                tf.setText(DATE_FORMAT.format(new Date()));
                            }
                        }, 100);
                    }
                } catch (ParseException e) {
                }
            }
        });
        frameKids.add(new Label("Type a date in the past:").setBounds(10, 140, 120, 20));
        frameKids.add(tf.setBounds(130, 140, 150, 20));
    }
