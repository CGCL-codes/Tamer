    public void displayURL(String urlstring) {
        try {
            if (!urlstring.startsWith("http:") && !urlstring.startsWith("ftp:") && !urlstring.startsWith("file:")) urlstring = "http://" + urlstring;
            URL url = new URL(urlstring);
            urlText.setText(url.toString());
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; BoxBrowserTest/2.x; Linux) CSSBox/2.x (like Gecko)");
            InputStream is = con.getInputStream();
            System.out.println("Parsing: " + url);
            DOMSource parser = new DOMSource(is);
            parser.setContentType(con.getHeaderField("Content-Type"));
            Document doc = parser.parse();
            DOMAnalyzer da = new DOMAnalyzer(doc, url);
            da.attributesToStyles();
            da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT);
            da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT);
            da.getStyleSheets();
            is.close();
            contentCanvas = new BrowserCanvas(da.getRoot(), da, contentScroll.getSize(), url);
            contentCanvas.addMouseListener(new MouseListener() {

                public void mouseClicked(MouseEvent e) {
                    System.out.println("Click: " + e.getX() + ":" + e.getY());
                    canvasClick(e.getX(), e.getY());
                }

                public void mousePressed(MouseEvent e) {
                }

                public void mouseReleased(MouseEvent e) {
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }
            });
            contentScroll.setViewportView(contentCanvas);
            Viewport viewport = ((BrowserCanvas) contentCanvas).getViewport();
            root = createBoxTree(viewport);
            boxTree.setModel(new DefaultTreeModel(root));
            domTree.setModel(new DefaultTreeModel(createDomTree(doc)));
        } catch (Exception e) {
            System.err.println("*** Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
