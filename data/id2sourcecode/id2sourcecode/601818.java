    public void run() {
        webpage = new String("");
        tmp = new String();
        cleanup("trying to import from " + baseurl);
        try {
            adminURL = new URL("http://" + l + ".org/w/" + baseurl);
            try {
                HttpURLConnection urlconn = (HttpURLConnection) adminURL.openConnection();
                urlconn.disconnect();
                urlconn.setRequestProperty("User-agent", configuration.getConfigurationObject().version);
                urlconn.connect();
                adminbuf = new BufferedInputStream(urlconn.getInputStream());
                try {
                    i = adminbuf.available();
                    while (i > 0) {
                        byte b[] = new byte[i];
                        try {
                            adminbuf.read(b);
                        } catch (IOException e) {
                            System.out.println("IOException, reading the stream: ");
                            e.printStackTrace();
                            cleanup("IOException, reading the stream: ");
                        }
                        webpage = new String(webpage + new String(b));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            System.out.println("failed to sleep: ");
                        }
                        try {
                            i = adminbuf.available();
                        } catch (IOException e) {
                            System.out.println("IOException, trying availability of the stream: ");
                            e.printStackTrace();
                            cleanup("IOException, trying availability of the stream: ");
                        }
                        Pattern p = Pattern.compile("<li><a href=\"/wiki/[^\"]*\" title=\"[^:]*:([^\"]*)\"");
                        Matcher m = p.matcher(webpage);
                        while (m.find()) {
                            config.whitelistimport(m.group(1) + "#" + l);
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            System.out.println("failed to sleep: ");
                        }
                        int tmp = data.editstableModel.getRowCount();
                        for (j = tmp - 1; j >= 0; j--) if (config.whiteListModel.contains((String) data.editstableModel.getValueAt(j, config.editorcol) + "#" + (String) data.editstableModel.getValueAt(j, config.projectcol))) data.editstableModel.removeRow(j);
                        cleanup("Successfully imported admins from " + l);
                    }
                } catch (IOException e) {
                    System.out.println("IOException, trying availability of the stream: ");
                    e.printStackTrace();
                    cleanup("IOException, trying availability of the stream: ");
                }
            } catch (IOException e) {
                System.out.println("IOException, creating BufferedInputStream: ");
                e.printStackTrace();
                cleanup("IOException, creating BufferedInputStream: ");
            }
        } catch (MalformedURLException e) {
            System.out.println("malformed url: " + adminURL);
            e.printStackTrace();
            cleanup("malformed url: " + adminURL);
        }
    }
