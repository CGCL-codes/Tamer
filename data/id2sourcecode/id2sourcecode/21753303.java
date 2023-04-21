    private static boolean getSplitFile(String key, File target, int htl, FrostDownloadItem dlItem) {
        logger.warning("ATTENTION: Using old, non-FEC download method!\n" + "           This could run, but is'nt really supported any longer.");
        String blockCount = SettingsFun.getValue(target.getPath(), "SplitFile.BlockCount");
        String splitFileSize = SettingsFun.getValue(target.getPath(), "SplitFile.Size");
        String splitFileBlocksize = SettingsFun.getValue(target.getPath(), "SplitFile.Blocksize");
        int maxThreads = 3;
        maxThreads = MainFrame.frostSettings.getIntValue("splitfileDownloadThreads");
        int intBlockCount = 0;
        try {
            intBlockCount = Integer.parseInt(blockCount, 16);
        } catch (NumberFormatException e) {
        }
        long intSplitFileSize = -1;
        try {
            intSplitFileSize = Long.parseLong(splitFileSize, 16);
        } catch (NumberFormatException e) {
        }
        int intSplitFileBlocksize = -1;
        try {
            intSplitFileBlocksize = Integer.parseInt(splitFileBlocksize, 16);
        } catch (NumberFormatException e) {
        }
        int[] blockNumbers = new int[intBlockCount];
        for (int i = 0; i < intBlockCount; i++) blockNumbers[i] = i + 1;
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < intBlockCount; i++) {
            int tmp = blockNumbers[i];
            int randomNumber = Math.abs(rand.nextInt()) % intBlockCount;
            blockNumbers[i] = blockNumbers[randomNumber];
            blockNumbers[randomNumber] = tmp;
        }
        if (dlItem != null) {
            if (dlItem.getFileSize() == null) {
                dlItem.setFileSize(new Long(intSplitFileSize));
            } else {
                if (dlItem.getFileSize().longValue() != intSplitFileSize) {
                    logger.warning("WARNING: size of fec splitfile differs from size given from download table. MUST not happen!");
                }
            }
            dlItem.setDoneBlocks(0);
            dlItem.setRequiredBlocks(intBlockCount);
            dlItem.setTotalBlocks(intBlockCount);
            dlItem.setState(FrostDownloadItem.STATE_PROGRESS);
        }
        boolean success = true;
        boolean[] results = new boolean[intBlockCount];
        Thread[] threads = new Thread[intBlockCount];
        for (int i = 0; i < intBlockCount; i++) {
            int j = blockNumbers[i];
            String chk = SettingsFun.getValue(target.getPath(), "SplitFile.Block." + Integer.toHexString(j));
            while (getActiveThreads(threads) >= maxThreads) {
                Mixed.wait(5000);
                if (dlItem != null) {
                    int doneBlocks = 0;
                    for (int z = 0; z < intBlockCount; z++) {
                        if (results[z] == true) {
                            doneBlocks++;
                        }
                    }
                    dlItem.setDoneBlocks(doneBlocks);
                    dlItem.setRequiredBlocks(intBlockCount);
                    dlItem.setTotalBlocks(intBlockCount);
                }
            }
            logger.info("Requesting: SplitFile.Block." + Integer.toHexString(j) + "=" + chk);
            int checkSize = intSplitFileBlocksize;
            if (blockNumbers[i] == intBlockCount && intSplitFileBlocksize != -1) checkSize = (int) (intSplitFileSize - (intSplitFileBlocksize * (intBlockCount - 1)));
            threads[i] = new getKeyThread(chk, new File(MainFrame.keypool + target.getName() + "-chunk-" + j), htl, results, i, checkSize);
            threads[i].start();
            if (dlItem != null) {
                int doneBlocks = 0;
                for (int z = 0; z < intBlockCount; z++) {
                    if (results[z] == true) {
                        doneBlocks++;
                    }
                }
                dlItem.setDoneBlocks(doneBlocks);
                dlItem.setRequiredBlocks(intBlockCount);
                dlItem.setTotalBlocks(intBlockCount);
            }
        }
        while (getActiveThreads(threads) > 0) {
            Mixed.wait(5000);
            if (dlItem != null) {
                int doneBlocks = 0;
                for (int z = 0; z < intBlockCount; z++) {
                    if (results[z] == true) {
                        doneBlocks++;
                    }
                }
                dlItem.setDoneBlocks(doneBlocks);
                dlItem.setRequiredBlocks(intBlockCount);
                dlItem.setTotalBlocks(intBlockCount);
            }
        }
        for (int i = 0; i < intBlockCount; i++) {
            if (!results[i]) {
                success = false;
                logger.info("NO SUCCESS");
            } else {
                logger.info("SUCCESS");
            }
        }
        if (success) {
            FileOutputStream fileOut;
            try {
                fileOut = new FileOutputStream(target);
                logger.info("Connecting chunks");
                for (int i = 1; i <= intBlockCount; i++) {
                    logger.fine("Adding chunk " + i + " to " + target.getName());
                    File toRead = new File(MainFrame.keypool + target.getName() + "-chunk-" + i);
                    fileOut.write(FileAccess.readByteArray(toRead));
                    toRead.deleteOnExit();
                    toRead.delete();
                }
                fileOut.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Write Error: " + target.getPath(), e);
            }
        } else {
            target.delete();
            logger.warning("!!!!!! Download of " + target.getName() + " failed.");
        }
        return success;
    }
