    private void monitorUploadProgress(String uploadKey) throws IOException {
        HttpPost monitorRequest = new HttpPost(POLL_UPLOAD_URL + "?key=" + uploadKey + "&MFULConfig=jvjxdyyujlnnvegcrjfhmuvc82cy1nzy");
        String quickKey = null;
        boolean[] L = { false };
        while (quickKey == null) {
            HttpResponse hr = httpClient.execute(monitorRequest);
            String rep = EntityUtils.toString(hr.getEntity());
            hr.getEntity().consumeContent();
            System.out.println("rep=" + rep);
            quickKey = fetchQuickKey(rep, L);
            if (L[0] == true) break;
        }
        if (quickKey != null) this.quickKey = quickKey;
    }
