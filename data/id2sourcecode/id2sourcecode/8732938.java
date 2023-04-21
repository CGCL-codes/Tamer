    private void givenClosedStagingRepo(String repositoryId) throws AuthenticationException, IOException {
        HttpResponse response = executePost("/staging/profiles/" + STAGING_PROFILE_ID + "/finish", new StringEntity(format(PROMOTE_REQUEST, repositoryId)), new BasicHeader("Content-Type", "application/xml"));
        assertEquals(SC_CREATED, response.getStatusLine().getStatusCode());
        consume(response.getEntity());
    }
