    private void run() throws IOException, XMLStreamException, InterruptedException {
        int num = Integer.parseInt(prefs.getProperty("num", "100"));
        URL url = new URL("http://www.connotea.org/data/user/" + prefs.getProperty("connotea-user") + "?num=" + num);
        URLConnection con = url.openConnection();
        String encoding = Base64.encode((prefs.getProperty("connotea-user") + ":" + prefs.getProperty("connotea-password")).getBytes());
        con.setRequestProperty("Authorization", "Basic " + encoding);
        con.connect();
        InputStream in = con.getInputStream();
        if (debug) in = new EchoInput(in);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(in);
        XMLEvent evt;
        Vector<Bookmark> bookmarks = new Vector<Bookmark>(num);
        {
            Bookmark bookmark = new Bookmark();
            while (!(evt = reader.nextEvent()).isEndDocument()) {
                if (evt.isStartElement()) {
                    StartElement e = evt.asStartElement();
                    String local = e.getName().getLocalPart();
                    if (local.equals("Post")) {
                        bookmark = new Bookmark();
                    } else if (local.equals("subject")) {
                        bookmark.subject.add(reader.getElementText());
                    } else if (local.equals("title")) {
                        bookmark.title = reader.getElementText();
                    } else if (local.equals("link")) {
                        bookmark.link = reader.getElementText();
                    } else if (local.equals("private")) {
                        bookmark.isPrivate = reader.getElementText().equals("1");
                    }
                } else if (evt.isEndElement()) {
                    EndElement e = evt.asEndElement();
                    String local = e.getName().getLocalPart();
                    if (local.equals("Post")) {
                        if (bookmark.link != null) {
                            if (bookmark.title == null) bookmark.title = bookmark.link;
                            if (bookmark.subject.isEmpty()) bookmark.subject.add("post");
                            bookmarks.addElement(bookmark);
                        }
                        bookmark = new Bookmark();
                    }
                }
            }
            in.close();
        }
        Collections.reverse(bookmarks);
        for (Bookmark bookmark : bookmarks) {
            StringBuilder sb = new StringBuilder("https://api.del.icio.us/v1/posts/add?");
            sb.append("url=" + URLEncoder.encode(bookmark.link, "UTF-8"));
            sb.append("&description=" + URLEncoder.encode(bookmark.title, "UTF-8"));
            sb.append("&tags=");
            for (String tag : bookmark.subject) {
                sb.append(URLEncoder.encode(tag, "UTF-8") + "+");
            }
            sb.append("&replace=yes");
            sb.append(bookmark.isPrivate ? "&shared=no" : "");
            System.out.print(bookmark.title + " " + sb);
            URL url2 = new URL(sb.toString());
            URLConnection con2 = url2.openConnection();
            encoding = Base64.encode((prefs.getProperty("delicious-user") + ":" + prefs.getProperty("delicious-password")).getBytes());
            con2.setRequestProperty("Authorization", "Basic " + encoding);
            con2.connect();
            InputStream in2 = con2.getInputStream();
            if (debug) in2 = new EchoInput(in2);
            XMLEventReader reader2 = factory.createXMLEventReader(in2);
            XMLEvent evt2;
            while (!(evt2 = reader2.nextEvent()).isEndDocument()) {
                if (!evt2.isStartElement()) continue;
                if (!evt2.asStartElement().getName().getLocalPart().equals("result")) continue;
                Attribute att = evt2.asStartElement().getAttributeByName(new QName("code"));
                if (att == null) continue;
                System.out.print("\t" + att.getValue());
                break;
            }
            System.out.println();
            in2.close();
            Thread.sleep(2000);
        }
        System.err.print("Done.");
    }
