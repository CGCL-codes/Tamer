    private void deployFile(final File file, String groupId, String artifactId, String version, String repoUrl) throws AuthenticationException, IOException, ClientProtocolException {
        final String url = repoUrl + getArtifactPath(groupId, artifactId, version, getExtension(file.getName()));
        final HttpPut request = new HttpPut(url);
        setCredentials(request);
        request.setEntity(new FileEntity(file, "application/octet-stream"));
        LOG.info("Uploading {} to {} ...", file, url);
        final HttpResponse response = client.execute(request);
        consume(response.getEntity());
        assertThat(response.getStatusLine().getStatusCode(), is(SC_CREATED));
    }
