    public void searchWinBLZ(String blz, String bnm, String plz, String ort) {
        searchFrame = new JFrame();
        searchFrame.setSize(new Dimension(750, 300));
        tmppath = System.getProperty("java.io.tmpdir");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension win = searchFrame.getSize();
        searchFrame.setLocation(screen.width / 2 - win.width / 2, screen.height / 2 - win.height / 2);
        try {
            url = new URL("http://www.hsh-nordbank.de/home/eBanking/BLZ/liste.jsp");
            urlConn = url.openConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
            new ErrorHandler(ex);
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try {
            printout = new OutputStreamWriter(urlConn.getOutputStream());
            writer = new BufferedWriter(new FileWriter(tmppath + "/return.html"));
            String content = "blz=" + blz + "&bnm=" + bnm + "&ort=" + ort + "&plz=" + plz + "";
            printout.write(content.toCharArray());
            printout.flush();
            printout.close();
            input = new InputStreamReader(urlConn.getInputStream());
            String string;
            boolean go = false;
            reader = new BufferedReader(input);
            while (null != ((string = reader.readLine()))) {
                if (go == true) {
                    writer.write(string);
                    writer.newLine();
                }
                if (string.indexOf("<TABLE BORDER=\"0\" CELLPADDING=\"2\" CELLSPACING=\"0\" WIDTH=\"370\">") > 0) {
                    writer.write("<b>Die Suche ergab folgende Treffer:</b><br><br><TABLE BORDER=\"0\" CELLPADDING=\"5\" CELLSPACING=\"0\" WIDTH=\"100%\">");
                    go = true;
                }
                if (go == true && string.indexOf("</TABLE>") > 0) {
                    writer.write("</TABLE>");
                    writer.newLine();
                    go = false;
                }
            }
            writer.write("<br><br><b>&copy;&nbsp;HSH Nordbank -  Quelle: www.bundesbank.de</b>");
            input.close();
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        jPane = new JEditorPane();
        try {
            jPane.setPage("file:///" + tmppath + "/return.html");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        jPane.setContentType("text/html");
        jPane.setEditable(false);
        viewerScrollPane = new JScrollPane(jPane);
        Dimension winSize = this.getSize();
        viewerScrollPane.setPreferredSize(new Dimension(winSize.width, winSize.height / 3 * 2));
        searchFrame.getContentPane().add(viewerScrollPane);
        searchFrame.show();
    }
