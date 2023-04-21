    public static Date getBuildTime(URL url, Box<String> versionStringRef) {
        try {
            InputStream urls = url.openStream();
            InputStreamReader is = null;
            BufferedReader br = null;
            is = new InputStreamReader(urls);
            br = new BufferedReader(is);
            String line = br.readLine();
            if (versionStringRef != null) {
                versionStringRef.set(line);
            }
            br.close();
            final String DRJAVA_PREFIX = "drjava-";
            if (!line.startsWith(DRJAVA_PREFIX)) {
                return null;
            }
            line = line.substring(DRJAVA_PREFIX.length());
            final String STABLE_PREFIX = "stable-";
            if (line.startsWith(STABLE_PREFIX)) {
                line = line.substring(STABLE_PREFIX.length());
            }
            final String BETA_PREFIX = "beta-";
            if (line.startsWith(BETA_PREFIX)) {
                line = line.substring(BETA_PREFIX.length());
            }
            final String WEEKLY_PREFIX = "weekly-";
            if (line.startsWith(WEEKLY_PREFIX)) {
                line = line.substring(WEEKLY_PREFIX.length());
            }
            int releasePos = line.indexOf("-r");
            if (releasePos >= 0) {
                line = line.substring(0, releasePos);
            }
            return new SimpleDateFormat("yyyyMMdd z").parse(line + " GMT");
        } catch (Exception e) {
            return null;
        }
    }
