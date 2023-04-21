    public void testIterator() throws Exception {
        String xml = "<root><status veld=\"veldValue\">ok</status><tabs>" + "<tab id=\"getArticles\">Articles</tab>" + "<tab id=\"getFavourites\">Favourites</tab>" + "<tab id=\"getChannelsWithNews\">Channels with news</tab>" + "<tabdusniet>dusniet</tabdusniet>" + "</tabs>" + "<tab>Losse tab</tab>" + "</root>";
        XMLHandler handler = getXmlHandler();
        XMLElement root = handler.parse(new StringReader(xml));
        XMLElementIterator it = new XMLElementIterator(root, "status");
        while (it.hasNext()) {
            XMLElement xmlElement = (XMLElement) it.next();
            assertTrue(xmlElement.getValue().equals("ok"));
            assertTrue(xmlElement.getValue("veld").equals("veldValue"));
        }
        it = new XMLElementIterator(root, "tab");
        StringBuffer sb = new StringBuffer();
        while (it.hasNext()) {
            XMLElement xmlElement = (XMLElement) it.next();
            sb.append(xmlElement.getValue()).append(",");
        }
        assertTrue(sb.toString().equals("Articles,Favourites,Channels with news,Losse tab,"));
        it = new XMLElementIterator(root, "tabs/tab");
        sb = new StringBuffer();
        while (it.hasNext()) {
            XMLElement xmlElement = (XMLElement) it.next();
            sb.append(xmlElement.getValue()).append(",");
        }
        assertTrue(sb.toString().equals("Articles,Favourites,Channels with news,"));
        it = new XMLElementIterator(root, "/tab");
        assertFalse(it.hasNext());
    }
