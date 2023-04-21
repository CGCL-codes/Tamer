    public void loadNewCluster() {
        loadpage("Create New Cluster");
        final VerticalPanel panel = new VerticalPanel();
        view.add(panel);
        panel.add(new Label("Cluster Name:"));
        final TextBox name = new TextBox();
        name.setName("vnet_name");
        panel.add(name);
        panel.add(new HTML("<br />"));
        final HTML error_name = HTMLwriter.getSystemMessage("red", "Name cannot be blank  or contain spaces.");
        final HTML error_nodata = HTMLwriter.getSystemMessage("red", "Please select at least one host.");
        final HTML error_dup = HTMLwriter.getSystemMessage("yellow", "Host already in list.");
        error_name.setVisible(false);
        panel.add(error_name);
        FlowPanel f = new FlowPanel();
        panel.add(f);
        final ListBox nets = new ListBox();
        Button add = new Button("Add Host to Cluster");
        Button remove = new Button("Remove Host from Cluster");
        final ListBox selected = new ListBox();
        nets.setWidth("186px");
        selected.setWidth("186px");
        VerticalPanel vb1 = new VerticalPanel();
        vb1.add(new Label("Select Hosts"));
        vb1.add(nets);
        vb1.add(add);
        f.add(vb1);
        VerticalPanel vb3 = new VerticalPanel();
        f.add(vb3);
        vb3.add(new Label("Selected Hosts:"));
        vb3.add(selected);
        vb3.add(remove);
        f.add(error_nodata);
        f.add(error_dup);
        error_nodata.setVisible(false);
        error_dup.setVisible(false);
        nets.setVisibleItemCount(8);
        selected.setVisibleItemCount(8);
        add.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                error_nodata.setVisible(false);
                error_dup.setVisible(false);
                if (nets.getSelectedIndex() == -1) return;
                String network = nets.getItemText(nets.getSelectedIndex());
                int i = 0, size = selected.getItemCount();
                while (i < size) {
                    if (selected.getItemText(i).equals(network)) {
                        error_dup.setVisible(true);
                        return;
                    }
                    i++;
                }
                selected.addItem(network);
            }
        });
        remove.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                error_dup.setVisible(false);
                if (selected.getSelectedIndex() == -1) return;
                selected.removeItem(selected.getSelectedIndex());
            }
        });
        final AsyncCallback<String[]> callback_clusters = new AsyncCallback<String[]>() {

            public void onFailure(Throwable caught) {
                caught.getMessage();
                HTML msg = HTMLwriter.getSystemMessage("red", "Error contacting server: " + caught.getMessage());
                panel.add(msg);
            }

            public void onSuccess(String[] hosts) {
                int i = 0, size = hosts.length;
                nets.clear();
                while (i < size) {
                    nets.addItem(hosts[i]);
                    i++;
                }
            }
        };
        setRefreshTask(new FunctionObject() {

            public void function() {
                serverCall.getHostNames(callback_clusters);
            }
        });
        Button submit = new Button("Create New Cluster");
        panel.add(submit);
        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                caught.getMessage();
                panel.clear();
                HTML msg = HTMLwriter.getSystemMessage("red", "Error contacting server: " + caught.getMessage());
                panel.add(msg);
                stopRefreshTask();
            }

            public void onSuccess(String b) {
                HTML msg = new HTML();
                if (b.equals("ok")) msg = HTMLwriter.getSystemMessage("green", "Cluster created successfully." + " You can now view it."); else {
                    msg = HTMLwriter.getSystemMessage("red", "There was an error creating the cluster." + b);
                }
                panel.clear();
                panel.add(msg);
                stopRefreshTask();
            }
        };
        submit.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                error_name.setVisible(false);
                if (selected.getItemCount() == 0) {
                    error_nodata.setVisible(true);
                    return;
                }
                if (!(Parser.isvalidName(name.getText()))) {
                    error_name.setVisible(true);
                    return;
                }
                String[] itemlist = Parser.createArrayFromListBox(selected);
                serverCall.adminNewCluster(name.getText(), itemlist, callback);
            }
        });
    }
