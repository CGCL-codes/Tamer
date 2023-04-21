        public void performUpdateCheck() {
            URL url;
            Properties props;
            try {
                url = new URL(versionURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setUseCaches(false);
                connection.setRequestProperty("User-Agent", Environment.getPhexVendor());
                InputStream inStream = connection.getInputStream();
                props = new Properties();
                props.load(inStream);
            } catch (java.net.MalformedURLException exp) {
                updateCheckError = exp;
                Logger.logError(exp);
                throw new RuntimeException();
            } catch (UnknownHostException exp) {
                updateCheckError = exp;
                return;
            } catch (SocketException exp) {
                updateCheckError = exp;
                return;
            } catch (IOException exp) {
                updateCheckError = exp;
                Logger.logWarning(exp);
                return;
            }
            ServiceManager.sCfg.lastUpdateCheckTime = System.currentTimeMillis();
            if (!ServiceManager.sCfg.showUpdateNotification) {
                ServiceManager.sCfg.save();
                return;
            }
            releaseVersion = props.getProperty(RELEASE_VERSION_KEY, "0");
            if (ServiceManager.sCfg.showBetaUpdateNotification) {
                betaVersion = props.getProperty(BETA_VERSION_KEY, "0");
            } else {
                betaVersion = "0";
            }
            int releaseCompare = VersionUtils.compare(releaseVersion, VersionUtils.getProgramVersion());
            int betaCompare = VersionUtils.compare(betaVersion, VersionUtils.getProgramVersion());
            if (releaseCompare <= 0 && betaCompare <= 0) {
                ServiceManager.sCfg.save();
                return;
            }
            releaseCompare = VersionUtils.compare(releaseVersion, ServiceManager.sCfg.lastUpdateCheckVersion);
            betaCompare = VersionUtils.compare(betaVersion, ServiceManager.sCfg.lastBetaUpdateCheckVersion);
            int verDiff = VersionUtils.compare(betaVersion, releaseVersion);
            boolean triggerUpdateNotification = false;
            if (releaseCompare > 0) {
                ServiceManager.sCfg.lastUpdateCheckVersion = releaseVersion;
                triggerUpdateNotification = true;
            }
            if (betaCompare > 0) {
                ServiceManager.sCfg.lastBetaUpdateCheckVersion = betaVersion;
                triggerUpdateNotification = true;
            }
            if (verDiff > 0) {
                releaseVersion = null;
            } else {
                betaVersion = null;
            }
            ServiceManager.sCfg.save();
            if (triggerUpdateNotification) {
                fireUpdateNotification();
            }
        }
