    private void addPSCInformation() {
        try {
            long psDate = System.currentTimeMillis();
            if (_local == true) {
                File psFile = new File("psummary.html");
                if (!psFile.exists()) {
                    return;
                }
                psDate = psFile.lastModified();
            }
            if (psDate > this._psFileDate) {
                this._psFileDate = psDate;
                this._psDate = psDate;
                for (int ii = 0; ii < this._projectInfo.size(); ii++) {
                    Information info = getInfo(ii);
                    if (info != null) {
                        info._psAtoms = null;
                        info._psContact = null;
                        info._psCore = null;
                        info._psDeadline = null;
                        info._psFrames = null;
                        info._psName = null;
                        info._psPreferred = null;
                        info._psServer = null;
                        info._psValue = null;
                    }
                }
                Reader reader = null;
                if (_local == true) {
                    reader = new FileReader("psummary.html");
                } else {
                    StringBuffer urlName = new StringBuffer();
                    urlName.append("http://fah-web.stanford.edu/");
                    urlName.append("psummaryC.html");
                    try {
                        URL url = new URL(urlName.toString());
                        InputStream stream = url.openStream();
                        reader = new InputStreamReader(stream);
                    } catch (MalformedURLException mue) {
                        mue.printStackTrace();
                    }
                }
                HTMLDocument htmlDoc = new HTMLDocumentPSummaryC();
                HTMLEditorKit htmlEditor = new HTMLEditorKit();
                htmlEditor.read(reader, htmlDoc, 0);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (BadLocationException e) {
        }
    }
