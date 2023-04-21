    public void searchWinPLZ(String str, String nr, String pf, String plz, String ort) {
        searchFrame = new JFrame();
        searchFrame.setSize(new Dimension(750, 300));
        tmppath = System.getProperty("java.io.tmpdir");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension win = searchFrame.getSize();
        searchFrame.setLocation(screen.width / 2 - win.width / 2, screen.height / 2 - win.height / 2);
        try {
            url = new URL("http://149.239.160.196/cgi-bin/plz_suche/search_warp.cgi");
            urlConn = url.openConnection();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        urlConn.setDoInput(true);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try {
            printout = new OutputStreamWriter(urlConn.getOutputStream());
            writer = new BufferedWriter(new FileWriter(tmppath + "/return.html"));
            String content = "src=dpag&action=popup&str=" + URLEncoder.encode(str, "iso-8859-1") + "&nr=" + nr + "&pf=" + URLEncoder.encode(pf, "iso-8859-1") + "&plz=" + plz + "&ort=" + URLEncoder.encode(ort, "iso-8859-1");
            printout.write(content.toCharArray());
            printout.flush();
            printout.close();
            input = new InputStreamReader(urlConn.getInputStream());
            String string;
            reader = new BufferedReader(input);
            while (null != ((string = reader.readLine()))) {
                if (string.indexOf("Error") < 0 && string.indexOf("include") < 0 && string.indexOf("<img src") < 0) {
                    if (string.indexOf("</body>") >= 0) {
                        String copyright = "&copy;&nbsp;Deutsche Post AG  -  <a href='http://www.postdirekt.de/plz_suche/nutzungsbedingungen.html'>Nutzungsbedingungen</a>";
                        writer.write(copyright);
                    }
                    writer.write(string);
                    writer.newLine();
                }
            }
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
        jPane.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    JEditorPane pane = (JEditorPane) e.getSource();
                    if (e instanceof HTMLFrameHyperlinkEvent) {
                        HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
                        HTMLDocument doc = (HTMLDocument) pane.getDocument();
                        doc.processHTMLFrameHyperlinkEvent(evt);
                    } else try {
                        pane.setPage(e.getURL());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        new ErrorHandler(ex);
                    }
                }
            }
        });
        viewerScrollPane = new JScrollPane(jPane);
        Dimension winSize = this.getSize();
        viewerScrollPane.setPreferredSize(new Dimension(winSize.width, winSize.height / 3 * 2));
        searchFrame.getContentPane().add(viewerScrollPane);
        searchFrame.show();
    }
