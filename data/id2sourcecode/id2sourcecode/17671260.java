    public synchronized void parseUrlOrFile(String urlOrFileName) throws ParserConfigurationException, SAXException, IOException, SimpleImporterException {
        Throwable urlException = null;
        Throwable fileException = null;
        InputStream in = null;
        try {
            URL url = new URL(urlOrFileName);
            URLConnection urlConnection = url.openConnection();
            in = urlConnection.getInputStream();
        } catch (MalformedURLException mue) {
            urlException = mue;
        } catch (IOException ioe) {
            urlException = ioe;
        }
        try {
            in = new FileInputStream(urlOrFileName);
        } catch (IOException ioe) {
            fileException = ioe;
        }
        if (in != null) {
            parse(new InputSource(new BufferedInputStream(in)));
        } else {
            throw new SimpleImporterException("Could not parse " + urlOrFileName + ", is neither URL (" + urlException.getMessage() + ") nor file (" + fileException.getMessage() + ").");
        }
    }
