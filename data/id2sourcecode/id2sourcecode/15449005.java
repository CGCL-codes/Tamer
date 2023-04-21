    private void bokActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String xmlTest = "";
            xmlTest = unicodeTextField1.getText();
            System.out.println("xmlReq.." + xmlTest);
            java.net.URL url = new java.net.URL("http://localhost:8080/newgenlibctxt/PrimaryCataloguingServlet");
            java.net.URLConnection urlconn = (java.net.URLConnection) url.openConnection();
            System.out.println("connected   " + (urlconn == null));
            urlconn.setDoOutput(true);
            urlconn.setRequestProperty("Content-type", "text/xml; charset=UTF-8");
            java.io.OutputStream os = urlconn.getOutputStream();
            java.util.zip.CheckedOutputStream cos = new java.util.zip.CheckedOutputStream(os, new java.util.zip.Adler32());
            java.util.zip.GZIPOutputStream gop = new java.util.zip.GZIPOutputStream(cos);
            java.io.OutputStreamWriter dos = new java.io.OutputStreamWriter(gop, "UTF-8");
            dos.write(xmlTest);
            dos.flush();
            dos.close();
            System.out.println("xmlRes  :" + xmlTest);
        } catch (Exception exp1) {
            exp1.printStackTrace();
        }
        try {
            for (Enumeration cards = NetworkInterface.getNetworkInterfaces(); cards.hasMoreElements(); ) {
                NetworkInterface card = (NetworkInterface) cards.nextElement();
                System.out.println("THIS CARD");
                System.out.println("\tDisplayName: " + card.getDisplayName());
                System.out.println("\tName: " + card.getName());
                System.out.println("\tHashCode: " + card.hashCode());
                System.out.println("\tToString: " + card.toString());
                for (Enumeration addresses = card.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress add = (InetAddress) addresses.nextElement();
                    System.out.println("\tAddress");
                    System.out.println("\t\tCanonicalHostName: " + add.getCanonicalHostName());
                    System.out.println("\t\tName: " + add.getHostAddress());
                    System.out.println("\t\tHostName: " + add.getHostName());
                    System.out.println("\t\tHashCode: " + add.hashCode());
                    System.out.println("\t\tToString: " + add.toString());
                }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
