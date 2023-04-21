    public HelpFrame(String helpFile) {
        super(Strings.getString("help.title", helpFile));
        TextArea text = new TextArea();
        text.setEditable(false);
        String resourcePath = "/doc/" + helpFile + ".txt";
        URL url = getClass().getResource(resourcePath);
        if (url != null) {
            try {
                byte buf[] = new byte[8192];
                int n;
                InputStream in = url.openStream();
                while ((n = in.read(buf, 0, buf.length)) > 0) {
                    text.append(new String(buf, 0, n));
                }
                in.close();
            } catch (Exception e) {
            }
        } else {
            text.append(Strings.getString("not found: " + resourcePath));
        }
        add("Center", text);
        Button b;
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(1, 1));
        b = new Button(Strings.getString("close"));
        b.setActionCommand("doClose");
        b.addActionListener(this);
        buttonPanel.add(b);
        add("South", buttonPanel);
        addWindowListener(this);
        setSize(getPreferredSize());
        pack();
        show();
    }
