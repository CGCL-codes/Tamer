    private void searchSynopsis(SearchParams search) throws Exception {
        String targetReqId = "[ Retriever ]" + search.toString();
        m_propertyChangeSupport.firePropertyChange("TARGET_REQUEST", "", targetReqId);
        PostMethod getReference = new PostMethod(INIT_WEB_PAGE);
        NameValuePair[] data = { new NameValuePair("rdb_Tipo", "rdbLocalizacion"), new NameValuePair("rdb_UrbRus", "rbLRusticos"), new NameValuePair("txtPar", search.getParcela()), new NameValuePair("slcMunicipios", "TALVEILA"), new NameValuePair("slcProvincias", "42"), new NameValuePair("tipoBusqueda", "Alfa"), new NameValuePair("txtPol", search.getPoligono()), new NameValuePair("__EVENTVALIDATION", m_credentials[1]), new NameValuePair("__VIEWSTATE", m_credentials[0]) };
        getReference.setRequestBody(data);
        m_client.executeMethod(getReference);
        String refLocation = getReference.getResponseHeader("Location").getValue();
        String refCat = parseQuery(refLocation).get("RefC");
        System.out.println(refCat);
        PostMethod pdf = new PostMethod(PDF_WEB_PAGE + refCat);
        m_client.executeMethod(pdf);
        InputStream imageStream = pdf.getResponseBodyAsStream();
        BufferedInputStream bufferedInput = new BufferedInputStream(imageStream);
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(refCat + ".pdf"));
        byte[] buffer = new byte[1024 * 16];
        int read = 0;
        while ((read = bufferedInput.read(buffer)) != -1) {
            bufferedOutput.write(buffer, 0, read);
        }
        bufferedOutput.close();
    }
