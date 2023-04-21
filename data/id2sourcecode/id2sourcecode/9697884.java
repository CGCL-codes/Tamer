    public Issue getIssue(String key) throws IssueTrackerException {
        try {
            key = prepareKey(key);
            synchronized (key2Issue) {
                if (key2Issue.containsKey(key)) {
                    return key2Issue.get(key);
                }
            }
            URL url = new URL(getBaseJiraUrl() + "/si/jira.issueviews:issue-xml/" + key + "/?" + getAuthorizedParams());
            URLConnection connection = url.openConnection();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = br.readLine();
                String id = null;
                String summary = null;
                while (line != null) {
                    line = decodeString(line);
                    Matcher matcherId = pattern_issue_id.matcher(line);
                    if (id == null && matcherId.find()) {
                        id = matcherId.group(1);
                        continue;
                    }
                    Matcher matcherSummary = pattern_summary.matcher(line);
                    if (summary == null && matcherSummary.find()) {
                        summary = matcherSummary.group(1);
                        continue;
                    }
                    if (id != null && summary != null) {
                        JiraIssue jiraIssue = new JiraIssue(key, id, summary);
                        synchronized (key2Issue) {
                            key2Issue.put(key, jiraIssue);
                        }
                        return jiraIssue;
                    }
                    line = br.readLine();
                }
            } finally {
                connection.getInputStream().close();
            }
            return null;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new IssueTrackerException(e);
        }
    }
