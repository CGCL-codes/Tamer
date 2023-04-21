    public void addOnt(Ont o, boolean infer) {
        try {
            sor.getConnection();
        } catch (SORException e) {
            e.printStackTrace();
        }
        if (loadedOnts.contains(o)) return;
        addOntToDocumentManager(o);
        try {
            URL url = null;
            try {
                String urlOfOnto = o.getURL();
                url = URLFactory.newURL(urlOfOnto);
                InputStream fis = url.openStream();
                sor.addOWLDocument(fis, "", false);
                try {
                    sor.commit();
                    loadedOnts.add(o);
                    fireStateChanged();
                } catch (SORException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SORException e) {
            e.printStackTrace();
        }
    }
