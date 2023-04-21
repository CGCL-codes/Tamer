    public void init() {
        Container c = getContentPane();
        c.setLayout(new GridLayout(4, 3));
        c.add(new JLabel("ѡ����Դ�����ļ���"));
        c.add(resourceFile);
        c.add(openResourceFile);
        c.add(new JLabel("������������ļ���"));
        c.add(pdFiles);
        c.add(addPDFile);
        c.add(new JLabel("�������ʱ�䣺"));
        c.add(endTime);
        c.add(new JLabel());
        c.add(new JLabel());
        c.add(begin);
        c.add(new JLabel());
        openResourceFile.addActionListener(this);
        openResourceFile.setActionCommand("openResourceFile");
        addPDFile.addActionListener(this);
        addPDFile.setActionCommand("addPDFile");
        begin.addActionListener(this);
        begin.setActionCommand("begin");
        this.pack();
        this.setLocation(400, 300);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
