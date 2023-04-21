    private void thenEnsureThatRepositoryMetadataDoesNotContainOldUrl(String repositoryId) throws AuthenticationException, IOException {
        HttpResponse response = executeGetWithResponse(NEXUS_BASE_URL + "/content/repositories/production/repodata/primary.xml.gz");
        assertEquals(SC_OK, statusCode(response));
        String content = gzipResponseContent(response);
        LOG.info("Content of primary.xml : {}", content);
        assertThat(content, not(containsString(repositoryId)));
        assertThat(content, containsString("test-rpm-1.45.rpm"));
        assertThat(content, containsString("dummy-artifakt-1.4.5.rpm"));
    }
