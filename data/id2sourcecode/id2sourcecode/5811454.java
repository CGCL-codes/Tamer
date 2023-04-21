    protected String givenStagingProfile(String profileId, String targetRepoId, String targetGroupRepoId) throws Exception {
        final StringEntity content = new StringEntity("{data:{name:'" + profileId + "', repositoryTemplateId:'maven2yum_hosted_release', repositoryType:'maven2', repositoryTargetId:'5', order: 0, targetGroups:['" + targetGroupRepoId + "'], promotionTargetRepository:'" + targetRepoId + "', mode:'BOTH', finishNotifyRoles:[], promotionNotifyRoles:[], dropNotifyRoles: [], closeRuleSets: [], promoteRuleSets: []}}");
        final HttpResponse response = executePost("/staging/profiles", content, new BasicHeader("Content-Type", "application/json"));
        final String responseContent = EntityUtils.toString(response.getEntity());
        assertThat(response.getStatusLine().getStatusCode(), is(201));
        final JsonNode node = new ObjectMapper().readTree(responseContent);
        return node.get("data").get("id").asText();
    }
