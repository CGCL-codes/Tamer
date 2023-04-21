    @Override
    public void getFilterIssues(final String filterId, final IssueHandler handler) throws IssueTrackerException {
        Runnable command = new Runnable() {

            @Override
            public void run() {
                try {
                    String uriStr = getBaseJiraUrl() + "/sr/jira.issueviews:searchrequest-xml/" + filterId + "/SearchRequest-" + filterId + ".xml?tempMax=1000" + "&" + getAuthorizedParams();
                    URL url = new URL(uriStr);
                    URLConnection connection = url.openConnection();
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String line = br.readLine();
                        String id = null;
                        String key = null;
                        String summary = null;
                        while (line != null && !handler.stopProcess()) {
                            line = decodeString(line);
                            Matcher matcherId = pattern_issue_id.matcher(line);
                            if (id == null && matcherId.find()) {
                                id = matcherId.group(1);
                                key = matcherId.group(2);
                                continue;
                            }
                            Matcher matcherSummary = pattern_summary.matcher(line);
                            if (summary == null && matcherSummary.find()) {
                                summary = matcherSummary.group(1);
                                continue;
                            }
                            if (id != null && summary != null) {
                                JiraIssue jiraIssue = new JiraIssue(key, id, summary);
                                handler.handle(jiraIssue);
                                id = key = summary = null;
                            }
                            line = br.readLine();
                        }
                    } finally {
                        connection.getInputStream().close();
                    }
                } catch (FileNotFoundException e) {
                    LOG.throwing("", "", e);
                } catch (IssueTrackerException e) {
                    LOG.throwing("", "", e);
                } catch (IOException e) {
                    LOG.throwing("", "", e);
                }
            }
        };
        executorService.execute(command);
    }
