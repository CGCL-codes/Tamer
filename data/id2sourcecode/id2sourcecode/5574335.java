    private void updateAnonymousId() {
        URL url = null;
        boolean needId = false;
        ApplicationPreferences preferences = ApplicationPreferences.getInstance();
        preferences.loadPreferences();
        boolean hasRun = preferences.getBoolean(IPreferenceConstants.P_IDE_HAS_RUN);
        String applicationId = preferences.getString(IPreferenceConstants.P_IDE_ID);
        Preferences p = CoreUIPlugin.getDefault().getPluginPreferences();
        String workspaceId = p.getString(IPreferenceConstants.P_IDE_ID);
        if (applicationId == null || applicationId.length() == 0) {
            if (workspaceId == null || workspaceId.length() == 0) {
                applicationId = "none";
                needId = true;
            } else {
                applicationId = workspaceId;
                preferences.setString(IPreferenceConstants.P_IDE_ID, applicationId);
                preferences.savePreferences();
            }
        } else {
            if (workspaceId != null && workspaceId.length() > 0 && workspaceId.equals(applicationId) == false) {
                applicationId = workspaceId;
            }
        }
        String queryString = null;
        try {
            String version = PluginUtils.getPluginVersion(AptanaCorePlugin.getDefault());
            String eclipseVersion = System.getProperty("osgi.framework.version");
            String product = StringUtils.replaceNullWithEmpty(System.getProperty("eclipse.product"));
            String osarch = StringUtils.replaceNullWithEmpty(StringUtils.urlEncodeForSpaces(System.getProperty("os.arch")));
            String osname = StringUtils.replaceNullWithEmpty(StringUtils.urlEncodeForSpaces(System.getProperty("os.name")));
            String osversion = StringUtils.replaceNullWithEmpty(StringUtils.urlEncodeForSpaces(System.getProperty("os.version")));
            queryString = "id=" + applicationId + "&v=" + version + "&p=" + product + "&ev=" + eclipseVersion + "&osa=" + osarch + "&osn=" + osname + "&osv=" + osversion;
            url = new URL("http://www.aptana.com/update.php?" + queryString);
            URLConnection conn = url.openConnection();
            OutputStreamWriter wr = null;
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            if (wr != null) {
                wr.close();
            }
            rd.close();
            String newId = sb.toString();
            if (needId == true) {
                preferences.setString(IPreferenceConstants.P_IDE_ID, newId);
                preferences.setBoolean(IPreferenceConstants.P_IDE_HAS_RUN, true);
                preferences.savePreferences();
            }
        } catch (UnknownHostException e) {
            return;
        } catch (MalformedURLException e) {
            IdeLog.logError(CoreUIPlugin.getDefault(), StringUtils.format(Messages.SchedulerStartup_UrlIsMalformed, url), e);
        } catch (IOException e) {
            if (needId && hasRun == false) {
                WorkbenchHelper.launchBrowser("http://www.aptana.com/install.php?" + queryString);
                preferences.setBoolean(IPreferenceConstants.P_IDE_HAS_RUN, true);
                preferences.savePreferences();
            }
        } catch (Exception e) {
            IdeLog.logError(CoreUIPlugin.getDefault(), Messages.SchedulerStartup_UnableToContactUpdateServer, e);
        }
    }
