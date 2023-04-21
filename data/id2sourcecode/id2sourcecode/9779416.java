    public void process(Element e, TagEnvironment env) throws TagBoxException {
        String href = e.getAttribute("href");
        if (href.equals("")) throw new TagBoxProcessingException(e, "missing 'href' attribute");
        href = evaluate(href, env, e);
        if (href.startsWith("/") || href.startsWith("..")) href = baseurl + href;
        Log.info("HitURL: " + href);
        String type = e.getAttribute("type");
        if (type.equals("")) type = evaluate(type, env, e);
        InputHandler handler;
        if (type.equals("xml")) handler = new InputHandler(InputHandler.TYPE_XML); else if (type.equals("text")) handler = new InputHandler(InputHandler.TYPE_TEXT); else handler = new InputHandler(InputHandler.TYPE_NONE);
        try {
            URL url = new URL(href);
            InputStream response = url.openStream();
            Node result = handler.handle(response);
            if (result == null) e.getParentNode().removeChild(e); else {
                result = e.getOwnerDocument().importNode(result, true);
                e.getParentNode().replaceChild(result, e);
            }
            response.close();
        } catch (MalformedURLException exc) {
            throw new TagBoxProcessingException(e, exc);
        } catch (IOException exc) {
            throw new TagBoxProcessingException(e, exc);
        } catch (ParserConfigurationException exc) {
            throw new TagBoxProcessingException(e, exc);
        } catch (SAXException exc) {
            throw new TagBoxProcessingException(e, exc);
        }
    }
