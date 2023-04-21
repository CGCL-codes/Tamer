    static Properties loadFilePrefsImpl(final File file) {
        Properties result = new Properties();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return result;
        }
        if (file.canRead()) {
            InputStream in = null;
            FileLock lock = null;
            try {
                FileInputStream istream = new FileInputStream(file);
                in = new BufferedInputStream(istream);
                FileChannel channel = istream.getChannel();
                lock = channel.lock(0L, Long.MAX_VALUE, true);
                Document doc = builder.parse(in);
                NodeList entries = XPathAPI.selectNodeList(doc.getDocumentElement(), "entry");
                int length = entries.getLength();
                for (int i = 0; i < length; i++) {
                    Element node = (Element) entries.item(i);
                    String key = node.getAttribute("key");
                    String value = node.getAttribute("value");
                    result.setProperty(key, value);
                }
                return result;
            } catch (IOException e) {
            } catch (SAXException e) {
            } catch (TransformerException e) {
                throw new AssertionError(e);
            } finally {
                releaseQuietly(lock);
                closeQuietly(in);
            }
        } else {
            file.delete();
        }
        return result;
    }
