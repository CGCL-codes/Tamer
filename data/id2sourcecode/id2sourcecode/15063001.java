    public void submit(final HashMap<String, Object> map) {
        HTML h2 = (HTML) map.get("Name_error");
        h2.setVisible(false);
        VerticalPanel a = (VerticalPanel) map.get("Parent");
        TextBox temp_name = (TextBox) map.get("Name");
        String netname = temp_name.getText();
        RadioButton list = (RadioButton) map.get("IsList");
        String type = "", info = "";
        if (list.getValue()) {
            HTML h3 = (HTML) map.get("IP_error");
            h3.setVisible(false);
            type = "list";
            int i = 0;
            ListBox lb = (ListBox) map.get("IPs");
            int size = lb.getItemCount();
            if (size == 0) {
                h3.setVisible(true);
                return;
            }
            while (i < size) {
                if (i != 0) info += '-';
                info += lb.getItemText(i);
                if (!(Parser.isvalidIP(lb.getItemText(i)))) {
                    h3.setVisible(true);
                    if (!(Parser.isvalidName(temp_name.getText()))) {
                        HTML h4 = (HTML) map.get("Name_error");
                        h4.setVisible(true);
                    }
                    return;
                }
                i++;
            }
        } else {
            HTML h3 = (HTML) map.get("Subnet_error");
            h3.setVisible(false);
            type = "subnet";
            TextBox temp_subnet = (TextBox) map.get("Subnet");
            info = temp_subnet.getText();
            HTML h = (HTML) map.get("Subnet_error");
            if (!(Parser.isvalidIPNumber(info))) {
                h.setVisible(true);
                if (!(Parser.isvalidName(temp_name.getText()))) {
                    HTML h4 = (HTML) map.get("Name_error");
                    h4.setVisible(true);
                }
                return;
            }
        }
        if (!(Parser.isvalidName(temp_name.getText()))) {
            HTML h = (HTML) map.get("Name_error");
            h.setVisible(true);
            return;
        }
        a.clear();
        boolean admin = user.isAdmin();
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable caught) {
                caught.getMessage();
                VerticalPanel a = (VerticalPanel) map.get("Parent");
                HTML msg = HTMLwriter.getSystemMessage("red", "Error contacting server.");
                a.add(msg);
            }

            public void onSuccess(String b) {
                VerticalPanel a = (VerticalPanel) map.get("Parent");
                HTML msg = new HTML();
                if (b.equals("ok")) msg = HTMLwriter.getSystemMessage("green", "Network created successfully." + " You can now view it."); else {
                    msg = HTMLwriter.getSystemMessage("red", "There was an error creating the Network. The name already exists.");
                }
                a.add(msg);
            }
        };
        if (admin) {
            serverCall.AdminCreateNetwork(netname, type, info, callback);
        } else {
            serverCall.UserCreateNetwork(netname, type, info, callback);
        }
    }
