    private void closeStagingRepo(String profileId, String repositoryId) throws Exception {
        final StringEntity content = new StringEntity("{data: {stagedRepositoryId: '" + repositoryId + "', targetRepositoryId: '" + TARGET_REPO_ID + "', description: 'Staging close.'}}");
        final HttpResponse response = executePost("/staging/profiles/" + profileId + "/finish", content, new BasicHeader("Content-Type", "application/json"));
        consume(response.getEntity());
        assertThat(response.getStatusLine().getStatusCode(), is(201));
    }
