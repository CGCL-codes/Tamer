    @SuppressWarnings("rawtypes")
    @Override
    public List<IElement> process(List<IElement> elements) {
        ArrayList<IElement> result = new ArrayList<IElement>();
        for (IElement el : elements) {
            String urlString = (String) el.getElement();
            log.info("Preprocessing url : " + urlString);
            TypedElement te = new TypedElement();
            te.setType("htmlcontent");
            try {
                URL url = new URL(urlString);
                URLConnection connection = url.openConnection();
                if ((connection.getContentType() != null) && !connection.getContentType().toLowerCase().startsWith("text/")) {
                }
                InputStream is = connection.getInputStream();
                Reader r = new InputStreamReader(is);
                CharBuffer cbuff = CharBuffer.allocate(1000);
                StringBuffer sb = new StringBuffer();
                while (r.read(cbuff) != -1) {
                    log.trace("CBUFF-" + cbuff.length());
                    for (int i = 0; i < 1000 - cbuff.length(); i++) {
                        sb.append(cbuff.get(i));
                    }
                    sb.append(cbuff.toString());
                    cbuff = CharBuffer.allocate(1000);
                }
                log.trace("SB:" + sb.toString());
                te.setElement(sb.toString());
                result.add(te);
            } catch (Exception e) {
                log.error("Error trying to process the URL.", e);
            }
        }
        return result;
    }
