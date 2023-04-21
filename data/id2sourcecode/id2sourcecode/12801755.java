    protected void scanJobLog() {
        File jobLog = getJobLog();
        launchCount = 0;
        if (!jobLog.exists()) return;
        try {
            Pattern launchLine = Pattern.compile("(\\S+) (\\S+) Job launched");
            long startPosition = 0;
            if (jobLog.length() > FileUtils.ONE_KB * 100) {
                isLaunchInfoPartial = true;
                startPosition = jobLog.length() - (FileUtils.ONE_KB * 100);
            }
            FileInputStream jobLogIn = new FileInputStream(jobLog);
            jobLogIn.getChannel().position(startPosition);
            BufferedReader jobLogReader = new BufferedReader(new InputStreamReader(jobLogIn));
            String line;
            while ((line = jobLogReader.readLine()) != null) {
                Matcher m = launchLine.matcher(line);
                if (m.matches()) {
                    launchCount++;
                    lastLaunch = new DateTime(m.group(1));
                }
            }
            jobLogReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
