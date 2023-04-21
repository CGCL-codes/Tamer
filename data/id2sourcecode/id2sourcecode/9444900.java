    public Document readDocumentFromFile(String fileName) {
        try {
            SAXBuilder builder = new SAXBuilder();
            URL url = getURL(fileName);
            if (fileName.substring(fileName.lastIndexOf("."), fileName.length()).equalsIgnoreCase(".aopz")) {
                FileInputStream fileInputStream = null;
                BufferedInputStream inputStream;
                if (url == null) {
                    fileInputStream = new FileInputStream(fileName);
                    inputStream = new BufferedInputStream(fileInputStream);
                } else {
                    inputStream = new BufferedInputStream(url.openStream());
                    remoteFile = true;
                }
                ZipInputStream zipInputEntry = new ZipInputStream(inputStream);
                ZipEntry entry = zipInputEntry.getNextEntry();
                if (entry != null) {
                    String temp = readZIPEntry(zipInputEntry, (int) entry.getSize());
                    Document doc = builder.build(new StringReader(temp));
                    zipInputEntry.close();
                    if (url == null) fileInputStream.close();
                    return doc;
                }
                return null;
            } else {
                if (url != null) return builder.build(url.openStream()); else return builder.build(new File(fileName));
            }
        } catch (JDOMException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
