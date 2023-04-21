    private void reorderProfiles(String topId) throws Exception {
        topId = "'" + topId + "'";
        final List<String> profileIds = getAllProfileIds();
        if (profileIds.contains(topId)) {
            profileIds.remove(topId);
            profileIds.add(0, topId);
        }
        final StringEntity content = new StringEntity("{data: [" + join(profileIds, ",") + "]}");
        final HttpResponse response = executePost("/staging/profile_order", content, new BasicHeader("Content-Type", "application/json"));
        consume(response.getEntity());
        assertThat(response.getStatusLine().getStatusCode(), is(201));
    }
