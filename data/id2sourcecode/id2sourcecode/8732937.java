    private void whenUserPromotesStagingRepo(String repositoryId) throws Exception {
        HttpResponse response = executePost("/staging/profiles/" + STAGING_PROFILE_ID + "/promote", new StringEntity(format(PROMOTE_REQUEST, repositoryId)), new BasicHeader("Content-Type", "application/xml"));
        System.out.println(EntityUtils.toString(response.getEntity()));
        assertEquals(SC_CREATED, response.getStatusLine().getStatusCode());
        consume(response.getEntity());
    }
