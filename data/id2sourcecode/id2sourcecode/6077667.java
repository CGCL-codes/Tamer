    public void doAction() {
        String local = new FileSaveDialog(main, main.getI18N("i18n.save_opml_to"), "*.opml").show();
        if (local != null) {
            main.status(main.getI18N("i18n.exporting_opml_to") + "\"" + local + "\"" + main.getI18N("i18n...."));
            List channels = null;
            try {
                channels = DAOChannel.getChannelsOrderByTitle();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (channels == null) return;
            File file = new File(local);
            FileWriter fw = null;
            try {
                fw = new FileWriter(file);
                fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                fw.write("<opml version=\"1.1\">");
                fw.write("</opml>");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fw != null) fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            SAXBuilder builder = new SAXBuilder();
            Document document = null;
            try {
                document = builder.build(file);
            } catch (JDOMException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            if (document == null) return;
            Element root = document.getRootElement();
            Element head = new Element("head");
            root.addContent(head);
            Element title = new Element("title");
            title.setText("OPML created by ThinFeeder (http://thinfeeder.sf.net) at " + new Date());
            head.addContent(title);
            Element body = new Element("body");
            root.addContent(body);
            Iterator iter = channels.iterator();
            while (iter.hasNext()) {
                ChannelIF channel = (ChannelIF) iter.next();
                String linkTitle = (String) channel.getTitle();
                Element outlineLink = new Element("outline");
                outlineLink.setAttribute("text", linkTitle);
                outlineLink.setAttribute("title", linkTitle);
                outlineLink.setAttribute("type", "rss");
                outlineLink.setAttribute("xmlUrl", channel.getLocation().toExternalForm());
                body.addContent(outlineLink);
            }
            try {
                OutputStream os = new FileOutputStream(file);
                XMLOutputter outp = new XMLOutputter();
                outp.setEncoding("UTF-8");
                outp.setIndent("  ");
                outp.setNewlines(false);
                outp.output(document, os);
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            main.status(main.getI18N("i18n.subscriptions_exported") + file);
        }
    }
