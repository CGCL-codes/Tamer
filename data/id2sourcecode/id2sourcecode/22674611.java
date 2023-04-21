    @Override
    public void displayURL(String urlstring) {
        try {
            if (!urlstring.startsWith("http:") && !urlstring.startsWith("ftp:") && !urlstring.startsWith("file:")) urlstring = "http://" + urlstring;
            URL url = new URL(urlstring);
            urlText.setText(url.toString());
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; BoxBrowserTest/2.x; Linux) CSSBox/2.x (like Gecko)");
            InputStream is = con.getInputStream();
            System.out.println("Parsing PDF: " + url);
            PDDocument doc = loadPdf(is);
            is.close();
            contentCanvas = new PdfBrowserCanvas(doc, null, contentScroll.getSize(), url);
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
            Document dom = ((PdfBrowserCanvas) contentCanvas).getBoxTree().getDocument();
            domTree.setModel(new DefaultTreeModel(createDomTree(dom)));
        } catch (Exception e) {
            System.err.println("*** Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
