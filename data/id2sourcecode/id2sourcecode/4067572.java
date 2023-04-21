    protected void addContacts(DefaultHttpClient client, List contacts, char pageChar) throws ContactListImporterException, URISyntaxException, InterruptedException, HttpException, IOException {
        String listUrl = (new StringBuilder(String.valueOf(getContactListURL()))).append(pageChar).toString();
        getLogger().info((new StringBuilder("Retrieve hyves contacts page ")).append(listUrl).toString());
        HttpGet get = new HttpGet(listUrl);
        HttpResponse resp = client.execute(get, client.getDefaultContext());
        parseAndAdd(readInputStream(resp.getEntity().getContent(), C.HYVES_ENCODE), contacts);
    }
