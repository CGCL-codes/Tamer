    public ComboBar(View view, ItemListener dropDownHandler, ItemListener radioButtonHandler) {
        this.view = view;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel jp1 = new JPanel();
        jp1.setBorder(BorderFactory.createTitledBorder("Filter"));
        jp1.add(rssiDropDown = new MyDropDown(dropDownHandler, getRssiList(), "RSSI Filter", "FilterSettingsChanged"));
        jp1.add(channelDropDown = new MyDropDown(dropDownHandler, getChannelList(), "Channel Filter", "FilterSettingsChanged"));
        jp1.add(dirDropDown = new MyDropDown(dropDownHandler, DIRECTION.values(), "Show data unicast both directions or selected direction only", "FilterSettingsChanged"));
        jp1.add(staDropDown = new MyDropDown(dropDownHandler, getStaList(), "STA Filter", "FilterSettingsChanged"));
        jp1.add(apDropDown = new MyDropDown(dropDownHandler, getApList(), "AP Filter", "FilterSettingsChanged"));
        JPanel jp2 = new JPanel();
        jp2.setBorder(BorderFactory.createTitledBorder("Details level"));
        ButtonGroup bg = new ButtonGroup();
        JRadioButton rb1 = new JRadioButton();
        addButton(jp2, bg, rb1, "1", "Hide details");
        addButton(jp2, bg, new JRadioButton(), "2", "Add sequence numbers and header details at higher zoom levels");
        addButton(jp2, bg, new JRadioButton(), "3", "Add transaction lines at higher zoom levels");
        addButton(jp2, bg, new JRadioButton(), "4", "Show transactions");
        addButton(jp2, bg, new JRadioButton(), "5", "Show fill packets at higher zoom levels");
        rb1.setSelected(true);
        add(jp1);
        add(jp2);
    }
