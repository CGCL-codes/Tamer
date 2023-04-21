    private static File findDurableSubscriptionDir(final String subscriptionId, final File topicDir, final FileSystem fileSystem) {
        LogHelper.logMethod(log, null, "findDurableSubscriptionDir(), subscriptionId = " + subscriptionId + ", topicDir = " + topicDir);
        final File[] subDirs = topicDir.listFiles(new FileFilter() {

            public boolean accept(final File pathname) {
                return pathname.isDirectory() && pathname.getName().indexOf(DELETED_NAME_PREFIX) == -1 && !pathname.getName().equals(TOPIC_INCOMING_DIR_NAME);
            }
        });
        log.trace("found " + subDirs.length + " subdirectories");
        File subscriptionDir = null;
        for (int i = 0; i < subDirs.length && subscriptionDir == null; i++) {
            final File subDir = subDirs[i];
            log.trace("analyzing " + subDir);
            if (subDir.isDirectory() && subDir.getName().indexOf(DELETED_NAME_PREFIX) == -1 && !subDir.getName().equals(TOPIC_INCOMING_DIR_NAME)) {
                if (subDir.getName().equals(SUBSCRIPTIONS_DIR_NAME)) {
                    final File potentialSubDir = new File(subDir, subscriptionId);
                    if (fileSystem.exists(potentialSubDir)) {
                        subscriptionDir = potentialSubDir;
                    } else {
                        final File[] subscriptionDirs = subDir.listFiles();
                        for (int j = 0; j < subscriptionDirs.length; j++) {
                            final File potentialNoLocalSubDir = subscriptionDirs[j];
                            if (potentialNoLocalSubDir.getName().startsWith(subscriptionId + ".")) {
                                subscriptionDir = potentialNoLocalSubDir;
                            }
                        }
                    }
                } else {
                    subscriptionDir = findDurableSubscriptionDir(subscriptionId, subDir, fileSystem);
                }
            }
        }
        return subscriptionDir;
    }
