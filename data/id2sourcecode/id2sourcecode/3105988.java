    private void invokeWebHook(String webhookUrl, VcsRepository targetVcsRepository, CodeRoot coderoot, CommitEntry commit) throws IOException, ProtocolException {
        final String projectName = coderoot.getBuildProperty("project.name");
        if (projectName == null) return;
        final URL url = new URL(webhookUrl);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.setDoOutput(true);
        final OutputStream os = connection.getOutputStream();
        try {
            final StringBuilder sbuffer = new StringBuilder();
            sbuffer.append("<event type='commit'>");
            sbuffer.append(String.format("  <project>%s</project>", projectName));
            sbuffer.append(String.format("  <contact>%s</contact>", commit.getAuthor()));
            sbuffer.append(String.format("  <eventTime>%tFT%<tT.%<tLZ</eventTime>", commit.getTimestamp()));
            sbuffer.append(String.format("  <component>%s</component>", coderoot.getDisplayName()));
            sbuffer.append(String.format("  <subject url='%s' code='%s'>%s</subject>", targetVcsRepository.getConnectionUrl(), commit.getRevision(), commit.getMessage()));
            sbuffer.append("</event>");
            os.write(sbuffer.toString().getBytes());
            os.flush();
            LOGGER.info("invoked webhook " + webhookUrl + " with data " + sbuffer);
        } finally {
            os.close();
            connection.disconnect();
        }
        if (connection.getResponseCode() != 201) {
            LOGGER.warning(String.format("Unexpected response: %d %s", connection.getResponseCode(), connection.getResponseMessage()));
        }
    }
