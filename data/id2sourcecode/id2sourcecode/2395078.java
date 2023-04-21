    public static MWAuthentication login(String api_url, PasswordAuthentication authentication) throws IOException, XMLStreamException {
        MWAuthentication log = new MWAuthentication();
        URL url = new URL(api_url);
        URLConnection con = url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        PrintWriter wr = new PrintWriter(con.getOutputStream());
        wr.print("format=xml&action=login&lgname=");
        wr.print(URLEncoder.encode(authentication.getUserName(), "UTF-8"));
        wr.print("&lgpassword=");
        wr.print(URLEncoder.encode(new String(authentication.getPassword()), "UTF-8"));
        wr.flush();
        String result = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        XMLEventReader reader = factory.createXMLEventReader(con.getInputStream());
        while (reader.hasNext()) {
            XMLEvent evt = reader.nextEvent();
            if (!evt.isStartElement()) continue;
            StartElement start = evt.asStartElement();
            if (!start.getName().getLocalPart().equals("login")) continue;
            for (Iterator<?> it = start.getAttributes(); it.hasNext(); ) {
                Attribute att = Attribute.class.cast(it.next());
                String name = att.getName().getLocalPart();
                if (name.equals("lguserid")) {
                    log.userid = att.getValue();
                } else if (name.equals("result")) {
                    result = att.getValue();
                } else if (name.equals("lgusername")) {
                    log.username = att.getValue();
                } else if (name.equals("lgtoken")) {
                    log.token = att.getValue();
                } else if (name.equals("cookieprefix")) {
                    log.cookieprefix = att.getValue();
                } else if (name.equals("sessionid")) {
                    log.sessionid = att.getValue();
                }
            }
        }
        wr.close();
        reader.close();
        if (result == null || !result.equals("Success")) throw new IOException("Authentication failed");
        return log;
    }
