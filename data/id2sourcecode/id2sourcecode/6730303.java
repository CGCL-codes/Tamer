    @Override
    protected Long doInBackground(Object... params) {
        HttpClient client = (HttpClient) params[0];
        HttpGet getLibs = new HttpGet((String) params[1]);
        Boolean reload = params.length > 2 ? (Boolean) params[2] : false;
        File storageDir = context.getExternalFilesDir(null);
        File libXml = new File(storageDir, "libraries.xml");
        InputSource inputSource = null;
        try {
            if (libXml.exists() && !reload) {
                inputSource = new InputSource(new FileInputStream(libXml));
            } else {
                HttpResponse resp = client.execute(getLibs);
                InputStream is = resp.getEntity().getContent();
                libXml.delete();
                if (libXml.createNewFile()) {
                    FileOutputStream fos = new FileOutputStream(libXml);
                    try {
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = is.read(buf)) > 0) {
                            fos.write(buf, 0, len);
                        }
                    } finally {
                        fos.close();
                        is.close();
                    }
                    inputSource = new InputSource(new FileInputStream(libXml));
                } else {
                    inputSource = new InputSource(is);
                }
            }
            rootNode = (Node) rootXpath.evaluate(inputSource, XPathConstants.NODE);
            NodeList libraryNodes = (NodeList) libraries.evaluate(rootNode, XPathConstants.NODESET);
            for (int i = 0; i < libraryNodes.getLength(); i++) {
                Node libNode = libraryNodes.item(i);
                String stateText = state.evaluate(libNode);
                distinctStates.add(stateText);
            }
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "xpath error creating preferences", e);
        } finally {
            try {
                inputSource.getByteStream().close();
            } catch (Exception e) {
                Log.e(this.getClass().getName(), "Failed to close InputStream", e);
            }
        }
        return libXml.lastModified();
    }
