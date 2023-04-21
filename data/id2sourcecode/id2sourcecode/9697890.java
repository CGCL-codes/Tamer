    private void addWorklog(final TimeSlot timeSlot, final String key, final String issueId, Attribute statusAttribute, String methodName, long duration) throws MalformedURLException, UnsupportedEncodingException, IOException {
        URL url = new URL(getBaseJiraUrl() + "/secure/" + methodName + ".jspa");
        URLConnection connection = url.openConnection();
        if (connection instanceof HttpURLConnection) {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter writer = new OutputStreamWriter(httpConnection.getOutputStream());
            try {
                String startDateStr = new SimpleDateFormat("dd/MMM/yy KK:mm a").format(timeSlot.getStartDate());
                writer.append(getAuthorizedParams()).append(getPair("id", issueId)).append(getPair("worklogId", "")).append(getPair("timeLogged", (duration / 1000 / 60) + "m")).append(getPair("comment", URLEncoder.encode(timeSlot.getDescription(), "UTF-8"))).append(getPair("startDate", URLEncoder.encode(startDateStr, "UTF-8"))).append(getPair("adjustEstimate", "auto")).append(getPair("newEstimate", "")).append(getPair("commentLevel", ""));
            } finally {
                writer.flush();
                writer.close();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = br.readLine();
            br.close();
            LOG.finest("jira result: " + line);
            if (statusAttribute == null) {
                statusAttribute = new Attribute(issueWorklogStatusType);
                List<Attribute> list = new ArrayList<Attribute>(timeSlot.getAttributes());
                list.add(statusAttribute);
                timeSlot.setAttributes(list);
            }
            statusAttribute.set(timeSlot.getTime());
            LOG.info("Updated jira worklog with key: " + key);
        }
    }
