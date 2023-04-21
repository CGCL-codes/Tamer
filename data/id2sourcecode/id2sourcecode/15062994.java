    public void LoadCreateNetwork() {
        loadpage("Create New Network");
        VerticalPanel panel = new VerticalPanel();
        view.add(panel);
        Label label1 = new Label("Network Name:");
        panel.add(label1);
        name = new TextBox();
        name.setName("vnet_name");
        panel.add(name);
        panel.add(new HTML("<br />"));
        final HTML error_name = HTMLwriter.getSystemMessage("red", "Name cannot be blank  or contain spaces.");
        final HTML error_ip = HTMLwriter.getSystemMessage("red", "Please specify at least one IP address.");
        final HTML error_ip2 = HTMLwriter.getSystemMessage("red", "Invalid IP address specified.");
        final HTML error_ipdup = HTMLwriter.getSystemMessage("red", "IP already in list.");
        final HTML error_subnet = HTMLwriter.getSystemMessage("red", "Invalid subnet specified.");
        error_name.setVisible(false);
        panel.add(error_name);
        panel.add(new Label("Network Type:"));
        subnet_rb = new RadioButton("myRadioGroup", "Subnet");
        list_rb = new RadioButton("myRadioGroup", "List of IPs");
        subnet_rb.setValue(true);
        VerticalPanel fpanel = new VerticalPanel();
        panel.add(subnet_rb);
        panel.add(list_rb);
        panel.add(fpanel);
        panel.add(new HTML("<br />"));
        final VerticalPanel subnetpanel = new VerticalPanel();
        Label label2 = new Label("Subnet Address:");
        subnetpanel.add(label2);
        FlowPanel f1 = new FlowPanel();
        subnet = new TextBox();
        subnet.setName("vnet_subnet");
        subnet.addStyleName("tinytext");
        HTML h1 = new HTML("192.168.");
        HTML h2 = new HTML(".x");
        h1.setStyleName("inline");
        h2.setStyleName("inline");
        f1.add(h1);
        f1.add(subnet);
        f1.add(h2);
        f1.add(error_subnet);
        error_subnet.setVisible(false);
        subnetpanel.add(f1);
        panel.add(subnetpanel);
        final VerticalPanel listpanel = new VerticalPanel();
        listpanel.add(new Label("Please type an IP address:"));
        FlowPanel f = new FlowPanel();
        final TextBox ip = new TextBox();
        ip.addFocusHandler(new FocusHandler() {

            public void onFocus(FocusEvent event) {
                error_ip2.setVisible(false);
                error_ipdup.setVisible(false);
            }
        });
        f.add(ip);
        Button add = new Button("Add to List");
        f.add(add);
        error_ip.setVisible(false);
        f.add(error_ip);
        error_ipdup.setVisible(false);
        f.add(error_ipdup);
        error_ip2.setVisible(false);
        f.add(error_ip2);
        listpanel.add(f);
        final ListBox lb = new ListBox();
        lb.setWidth("186px");
        FlowPanel f2 = new FlowPanel();
        lb.setVisibleItemCount(4);
        f2.add(lb);
        Button remove = new Button("Remove from List");
        f2.add(remove);
        listpanel.add(f2);
        listpanel.setVisible(false);
        panel.add(listpanel);
        add.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                error_ip2.setVisible(false);
                error_ipdup.setVisible(false);
                if (Parser.isvalidIP(ip.getText())) {
                    int i = 0;
                    while (i < lb.getItemCount()) {
                        String temp = lb.getItemText(i);
                        if (temp.equals(ip.getText())) {
                            error_ipdup.setVisible(true);
                            return;
                        }
                        i++;
                    }
                    lb.addItem(ip.getText());
                    ip.setText("");
                    error_ip.setVisible(false);
                } else {
                    error_ip2.setVisible(true);
                }
            }
        });
        remove.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                int a = lb.getSelectedIndex();
                if (a == -1) return;
                lb.removeItem(a);
            }
        });
        subnet_rb.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                subnetpanel.setVisible(true);
                listpanel.setVisible(false);
            }
        });
        list_rb.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                listpanel.setVisible(true);
                subnetpanel.setVisible(false);
            }
        });
        Button submit = new Button("Create network");
        final HashMap<String, Object> map_info = new HashMap<String, Object>();
        map_info.put("Action", "SubmitNet");
        map_info.put("Parent", panel);
        map_info.put("Name", name);
        map_info.put("IPs", lb);
        map_info.put("Name_error", error_name);
        map_info.put("IP_error", error_ip);
        map_info.put("Subnet_error", error_subnet);
        map_info.put("Subnet", subnet);
        map_info.put("IsList", list_rb);
        panel.add(new HTML("<br />"));
        panel.add(submit);
        submit.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                submit(map_info);
            }
        });
    }
