    private Resource loadFromURI(Collection collection, URI uri, String docName, boolean binary, String mimeType) throws XPathException {
        Resource resource;
        if ("file".equals(uri.getScheme())) {
            String path = uri.getPath();
            if (path == null) throw new XPathException(this, "Cannot read from URI: " + uri.toASCIIString());
            File file = new File(path);
            if (!file.canRead()) throw new XPathException(this, "Cannot read path: " + path);
            resource = loadFromFile(collection, file, docName, binary, mimeType);
        } else {
            File temp = null;
            try {
                temp = File.createTempFile("existDBS", ".xml");
                temp.deleteOnExit();
                OutputStream os = new FileOutputStream(temp);
                InputStream is = uri.toURL().openStream();
                byte[] data = new byte[1024];
                int read = 0;
                while ((read = is.read(data)) > -1) {
                    os.write(data, 0, read);
                }
                is.close();
                os.close();
                resource = loadFromFile(collection, temp, docName, binary, mimeType);
                temp.delete();
            } catch (MalformedURLException e) {
                throw new XPathException(this, "Malformed URL: " + uri.toString(), e);
            } catch (IOException e) {
                throw new XPathException(this, "IOException while reading from URL: " + uri.toString(), e);
            } finally {
                if (temp != null) temp.delete();
            }
        }
        return resource;
    }
