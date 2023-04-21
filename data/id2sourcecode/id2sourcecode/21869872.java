    private void addInformation(String userName, int teamNum, int range) {
        StringBuffer urlName = new StringBuffer();
        urlName.append("http://vspx27.stanford.edu/");
        urlName.append("cgi-bin/main.py?qtype=userpagedet");
        urlName.append("&username=");
        urlName.append(userName);
        urlName.append("&teamnum=");
        urlName.append(teamNum);
        urlName.append("&prange=");
        urlName.append(range);
        try {
            URL url = new URL(urlName.toString());
            InputStream stream = url.openStream();
            InputStreamReader reader = new InputStreamReader(stream);
            HTMLDocument htmlDoc = new HTMLDocumentContribution(this);
            HTMLEditorKit htmlEditor = new HTMLEditorKit() {

                protected HTMLEditorKit.Parser getParser() {
                    return new ParserDelegator() {

                        public void parse(Reader r, HTMLEditorKit.ParserCallback cb, boolean ignoreCharSet) throws IOException {
                            super.parse(r, cb, true);
                        }
                    };
                }
            };
            htmlEditor.read(reader, htmlDoc, 0);
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        }
    }
