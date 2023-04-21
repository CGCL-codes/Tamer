    public SubmissionResult submitBug(String submitterEmail, String submitterBugzillaPassword, String bugSubject, String bugText) {
        String bugComponent = "Plugins";
        Pattern cookiePattern = Pattern.compile("^(\\w+)=(\\w+);.*$");
        Pattern linuxPattern = Pattern.compile("^Linux.*$", Pattern.CASE_INSENSITIVE);
        Pattern windowsPattern = Pattern.compile("^Windows.*$", Pattern.CASE_INSENSITIVE);
        Pattern macPattern = Pattern.compile("^Mac ?OS.*$", Pattern.CASE_INSENSITIVE);
        Pattern badAuthentication = Pattern.compile("^.*username or password you entered is not valid.*$");
        StringBuffer submissionReply = new StringBuffer();
        StringBuffer authenticationReply = new StringBuffer();
        try {
            URL url = new URL(bugzillaBaseURI + "enter_bug.cgi");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            PrintStream ps = new PrintStream(connection.getOutputStream());
            ps.println("Bugzilla_login=" + e(submitterEmail) + "&Bugzilla_password=" + e(submitterBugzillaPassword) + "&classification=__all" + "&GoAheadAndLogIn=" + e("Log in"));
            ps.close();
            HashMap<String, String> cookies = new HashMap<String, String>();
            Map<String, List<String>> headers = connection.getHeaderFields();
            List<String> setCookiesFields = headers.get("Set-Cookie");
            if (setCookiesFields != null) for (String s : setCookiesFields) {
                Matcher cookieMatcher = cookiePattern.matcher(s);
                if (cookieMatcher.matches()) {
                    String key = cookieMatcher.group(1);
                    String value = cookieMatcher.group(2);
                    cookies.put(key, value);
                }
            }
            if (cookies.size() == 0) {
                return new SubmissionResult(LOGIN_FAILURE, -1, null, authenticationReply.toString(), submissionReply.toString());
            }
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            boolean authenticationFailed = false;
            while ((line = br.readLine()) != null) {
                authenticationReply.append(line);
                if (badAuthentication.matcher(line).matches()) {
                    authenticationFailed = true;
                }
            }
            if (authenticationFailed) {
                return new SubmissionResult(LOGIN_FAILURE, -1, null, authenticationReply.toString(), submissionReply.toString());
            }
            String ccString = "";
            if (submitterEmail != null && submitterEmail.trim().length() > 0) ccString = "&cc=" + e(submitterEmail.trim());
            url = new URL(bugzillaBaseURI + "post_bug.cgi");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            boolean firstCookie = true;
            String cookieValueToSend = "";
            for (String cookieKey : cookies.keySet()) {
                String value = cookies.get(cookieKey);
                if (firstCookie) {
                    firstCookie = false;
                    cookieValueToSend += cookieKey + "=" + value;
                } else {
                    cookieValueToSend += "; " + cookieKey + "=" + value;
                }
            }
            connection.setRequestProperty("Cookie", cookieValueToSend);
            String osParameterValue = null;
            String platformParameterValue = null;
            String osName = System.getProperty("os.name");
            if (linuxPattern.matcher(osName).matches()) {
                osParameterValue = "Linux";
                platformParameterValue = "PC";
            } else if (windowsPattern.matcher(osName).matches()) {
                osParameterValue = "Windows";
                platformParameterValue = "PC";
            } else if (macPattern.matcher(osName).matches()) {
                osParameterValue = "Mac OS";
                platformParameterValue = "Macintosh";
            } else {
                osParameterValue = "Other";
                platformParameterValue = "Other";
            }
            ps = new PrintStream(connection.getOutputStream());
            ps.println("product=Fiji" + "&component=" + e(bugComponent) + "&rep_platform=" + e(platformParameterValue) + "&op_sys=" + e(osParameterValue) + "&priority=P4" + "&bug_severity=normal" + "&target_milestone=" + e("---") + "&version=unspecified" + "&bug_file_loc=" + e("http://") + "&bug_status=NEW" + "&assigned_to=" + e(bugzillaAssignee) + ccString + "&short_desc=" + e(bugSubject) + "&comment=" + e(bugText) + "&commentprivacy=0" + "&dependson=" + "&blocked=" + "&hidden=enter_bug");
            ps.close();
            Pattern successfullySubmitted = Pattern.compile("^.*Bug\\s+(\\d+)\\s+Submitted.*$", Pattern.CASE_INSENSITIVE);
            Pattern ccEmailUnknown = Pattern.compile("^.*did not match anything.*$");
            int bugNumber = -1;
            boolean unknownCC = false;
            is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            line = null;
            while ((line = br.readLine()) != null) {
                submissionReply.append(line);
                Matcher submittedMatcher = successfullySubmitted.matcher(line);
                if (submittedMatcher.matches()) {
                    bugNumber = Integer.parseInt(submittedMatcher.group(1), 10);
                } else if (ccEmailUnknown.matcher(line).matches()) {
                    unknownCC = true;
                }
            }
            if (unknownCC) {
                IJ.error("Your email address (" + submitterEmail + ") didn't match" + " a Bugzilla account.\nEither create an account for that" + " email address, which is the\nrecommended option," + " or leave the email field blank.");
                return new SubmissionResult(CC_UNKNOWN_FAILURE, -1, null, authenticationReply.toString(), submissionReply.toString());
            }
            if (bugNumber < 1) {
                return new SubmissionResult(OTHER_FAILURE, -1, null, authenticationReply.toString(), submissionReply.toString());
            } else {
                return new SubmissionResult(SUCCESS, bugNumber, bugzillaBaseURI + "show_bug.cgi?id=" + bugNumber, authenticationReply.toString(), submissionReply.toString());
            }
        } catch (IOException e) {
            System.out.println("Got an IOException: " + e);
            e.printStackTrace();
            return new SubmissionResult(IOEXCEPTION_FAILURE, -1, null, authenticationReply.toString(), submissionReply.toString());
        }
    }
