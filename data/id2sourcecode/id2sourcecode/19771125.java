    public boolean sendLogThroughMail(String filePath) {
        try {
            String serverIp = newgen.presentation.NewGenMain.getAppletInstance().getCodeBase().getHost();
            java.net.URL url = new java.net.URL("http://" + serverIp + ":8080/newgenlibctxt/DBFileHandler");
            url.openConnection();
            java.io.InputStream ins = url.openStream();
            org.jdom.input.SAXBuilder saxBuild = new org.jdom.input.SAXBuilder();
            org.jdom.Document doc = saxBuild.build(ins);
            java.util.List l2 = doc.getRootElement().getChild("mbean").getChildren();
            String user = null;
            String password = null;
            String server = null;
            String fromEmail = null;
            for (int i = 0; i < l2.size(); i++) {
                org.jdom.Element e = (org.jdom.Element) l2.get(i);
                if (e.getName().equals("attribute")) {
                    if (e.getAttributeValue("name").equals("User")) {
                        user = e.getText();
                    }
                }
                if (e.getName().equals("attribute")) {
                    if (e.getAttributeValue("name").equals("Password")) {
                        password = e.getText();
                    }
                }
                java.util.List l3 = e.getChildren();
                if (l3.size() != 0) {
                    org.jdom.Element e1 = (org.jdom.Element) l3.get(0);
                    java.util.List l4 = e1.getChildren();
                    for (int j = 0; j < l4.size(); j++) {
                        org.jdom.Element e2 = (org.jdom.Element) l4.get(j);
                        if (e2.getAttributeValue("name").equals("mail.smtp.host")) server = e2.getAttributeValue("value");
                        if (e2.getAttributeValue("name").equals("mail.from")) fromEmail = e2.getAttributeValue("value");
                    }
                }
            }
            HtmlEmail htmlEmail = new HtmlEmail();
            htmlEmail.setAuthentication(user, password);
            htmlEmail.setHostName(server);
            htmlEmail.addTo("poorna@verussolutions.biz", "NewGenLib");
            htmlEmail.setFrom(fromEmail, "Library");
            htmlEmail.setSubject("Log File");
            EmailAttachment emailAttach = new EmailAttachment();
            System.out.println(filePath + "/server.zip");
            emailAttach.setPath(filePath + "/server.zip");
            String text = "Here is the zipped log file which is attached to this mail";
            htmlEmail.setTextMsg("<HTML><BODY>" + text + "<HTML><BODY>");
            try {
                htmlEmail.send();
                System.out.println("mail successfully sent");
                return true;
            } catch (org.apache.commons.mail.EmailException eme) {
                eme.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
