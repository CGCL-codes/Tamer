    private void addPSInformation() {
        try {
            if (_local == true) {
                return;
            }
            Reader reader = null;
            if (_local == true) {
                reader = new FileReader("psummary.html");
            } else {
                StringBuffer urlName = new StringBuffer();
                urlName.append("http://vspx27.stanford.edu/");
                urlName.append("psummary.html");
                try {
                    URL url = new URL(urlName.toString());
                    InputStream stream = url.openStream();
                    reader = new InputStreamReader(stream);
                } catch (MalformedURLException mue) {
                    mue.printStackTrace();
                }
            }
            HTMLDocument htmlDoc = new HTMLDocumentPSummary();
            HTMLEditorKit htmlEditor = new HTMLEditorKit();
            htmlEditor.read(reader, htmlDoc, 0);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (BadLocationException e) {
        }
    }
