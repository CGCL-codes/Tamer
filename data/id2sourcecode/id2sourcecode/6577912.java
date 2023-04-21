    public void checkin(Object _document) {
        this.document = (Document) _document;
        synchronized (url) {
            OutputStream outputStream = null;
            try {
                if ("file".equals(url.getProtocol())) {
                    outputStream = new FileOutputStream(url.getFile());
                } else {
                    URLConnection connection = url.openConnection();
                    connection.setDoOutput(true);
                    outputStream = connection.getOutputStream();
                }
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(new DOMSource(document), new StreamResult(outputStream));
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (javax.xml.transform.TransformerException e) {
                e.printStackTrace();
            }
        }
    }
