    private void checkForUpgrade() {
        final SharedPreferences prefs = this.context.getSharedPreferences(GatherService.class.getName(), Context.MODE_PRIVATE);
        final long nextUpgradeCheck = prefs.getLong("nextUpgradeCheck", -1);
        if (System.currentTimeMillis() > nextUpgradeCheck) {
            PackageInfo pi;
            try {
                pi = this.context.getPackageManager().getPackageInfo("ca.theplanet.phoneplanwatcher", 0);
            } catch (final NameNotFoundException e1) {
                throw new RuntimeException(e1);
            }
            final String currentVersion = pi.versionName;
            try {
                final URL url = new URL("http://www.theplanet.ca/phoneplanwatcher.php?version=" + currentVersion);
                final URLConnection con = url.openConnection();
                final InputStream input = con.getInputStream();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(input), 30);
                final String line = reader.readLine();
                if (line != null && line.trim().length() > 0) {
                    this.storage.setPreference("upgradeMarketId", line.trim());
                } else {
                    this.storage.setPreference("upgradeMarketId", null);
                }
            } catch (final Exception e) {
                Log.e(GatherServiceImpl.TAG, "Couldn't check for upgrades", e);
            }
            final Editor editor = prefs.edit();
            editor.putLong("nextUpgradeCheck", System.currentTimeMillis() + 604800000);
            editor.commit();
        }
    }
