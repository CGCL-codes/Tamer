    private void copyInBotJars() {
        Set<String> copyJars = new HashSet<String>();
        copyJars.add(spec.getChallenger().getJarName());
        for (Battle battle : spec.getSeasonBattles()) {
            copyJars.add(getCompetitor(battle).getJarName());
        }
        File destDir = new File(Constants.ROBOCODE_DIR, "robots");
        for (String jarName : copyJars) {
            File fromJarFile = new File("robocode_bots", jarName);
            if (fromJarFile.exists()) {
                File toJarFile = new File(destDir, jarName);
                try {
                    FileUtils.copyFile(fromJarFile, toJarFile);
                } catch (IOException e1) {
                    System.err.println("Could not copy JAR file: " + jarName);
                } finally {
                    if (toJarFile.exists()) {
                        toJarFile.deleteOnExit();
                    }
                }
            }
        }
    }
