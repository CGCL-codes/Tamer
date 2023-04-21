    private void startBattle(Battle battle) throws IOException, UniversalException {
        File threadDir = new File(Constants.WORKING_DIR, "copy" + id);
        File robotsDir = new File(threadDir, "robots");
        robotsDir.mkdirs();
        File[] existing = robotsDir.listFiles();
        Arrays.sort(existing);
        for (Bot bot : battle.getBots()) {
            File destFile = new File(robotsDir, bot.getJarName());
            if (Arrays.binarySearch(existing, destFile) < 0) {
                OutputStream dest = new FileOutputStream(destFile);
                InputStream source = repository.getBotJar(bot);
                FileUtils.copyFile(source, dest);
                dest.close();
                source.close();
            }
        }
        List<String> extraArgs = new ArrayList<String>();
        extraArgs.add("-nodisplay");
        curRound = -1;
        currentRunner = new BattleRunner(battle, 0, this, threadDir, extraArgs);
        new Thread(currentRunner).start();
    }
