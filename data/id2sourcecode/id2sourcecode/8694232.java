    String checkArticle(String name, int namespaceType) {
        String revid = null;
        InputStream in = null;
        try {
            String prefix = WP_PREFIX;
            if (namespaceType == 14) prefix += "Category:";
            if (name.equals(prefix)) {
                alert("just the WP prefix " + name);
                return null;
            }
            if (name.startsWith(prefix) || Cast.URL.isA(name)) return name;
            name = name.replace(' ', '_');
            if (namespaceType == 14 && !name.startsWith("Category:")) {
                name = "Category:" + name;
            }
            URL url = new URL("http://en.wikipedia.org/w/api.php?action=query&prop=revisions&format=xml&titles=" + URLEncoder.encode(name, "UTF-8"));
            URLConnection con = url.openConnection();
            con.setConnectTimeout(10000);
            in = con.getInputStream();
            XMLEventReader reader = this.xmlInputFactory4WP.createXMLEventReader(in);
            while (reader.hasNext()) {
                XMLEvent evt = reader.nextEvent();
                if (!evt.isStartElement()) continue;
                StartElement e = evt.asStartElement();
                String local = e.getName().getLocalPart();
                if (local.equals("page")) {
                    Attribute att = e.getAttributeByName(new QName("title"));
                    if (att != null) name = att.getValue();
                }
                if (local.equals("rev")) {
                    Attribute att = e.getAttributeByName(new QName("revid"));
                    if (att != null) revid = att.getValue();
                    break;
                }
            }
            reader.close();
            if (revid == null) {
                alert("Cannot find " + name + " in " + url);
                return null;
            }
            name = WP_PREFIX + name;
            return name;
        } catch (Exception e) {
            e.printStackTrace();
            alert(String.valueOf(e.getMessage()));
            return null;
        } finally {
            if (in != null) try {
                in.close();
            } catch (Throwable err) {
            }
        }
    }
