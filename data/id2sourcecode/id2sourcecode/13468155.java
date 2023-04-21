    public static boolean getSplitFile(String key, File target, int htl) {
        String blockCount = SettingsFun.getValue(target.getPath(), "SplitFile.BlockCount");
        String splitFileSize = SettingsFun.getValue(target.getPath(), "SplitFile.Size");
        String splitFileBlocksize = SettingsFun.getValue(target.getPath(), "SplitFile.Blocksize");
        int maxThreads = 3;
        maxThreads = frame1.frostSettings.getIntValue("splitfileDownloadThreads");
        int intBlockCount = 0;
        try {
            intBlockCount = Integer.parseInt(blockCount, 16);
        } catch (NumberFormatException e) {
        }
        int intSplitFileSize = -1;
        try {
            intSplitFileSize = Integer.parseInt(splitFileSize, 16);
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
        boolean success = true;
        boolean[] results = new boolean[intBlockCount];
        Thread[] threads = new Thread[intBlockCount];
        for (int i = 0; i < intBlockCount; i++) {
            int j = blockNumbers[i];
            String chk = SettingsFun.getValue(target.getPath(), "SplitFile.Block." + Integer.toHexString(j));
            if (DEBUG) System.out.println("Requesting: SplitFile.Block." + Integer.toHexString(j) + "=" + chk);
            while (getActiveThreads(threads) >= maxThreads) mixed.wait(5000);
            int checkSize = intSplitFileBlocksize;
            if (blockNumbers[i] == intBlockCount && intSplitFileBlocksize != -1) checkSize = intSplitFileSize - (intSplitFileBlocksize * (intBlockCount - 1));
            threads[i] = new getKeyThread(chk, new File(frame1.keypool + target.getName() + "-chunk-" + j), htl, results, i, checkSize);
            threads[i].start();
        }
        while (getActiveThreads(threads) > 0) {
            if (DEBUG) System.out.println("Active Splitfile request remaining (htl " + htl + "): " + getActiveThreads(threads));
            mixed.wait(5000);
        }
        for (int i = 0; i < intBlockCount; i++) {
            if (!results[i]) {
                success = false;
                if (DEBUG) System.out.println("NO SUCCESS");
            } else {
                if (DEBUG) System.out.println("SUCCESS");
            }
        }
        if (success) {
            FileOutputStream fileOut;
            try {
                fileOut = new FileOutputStream(target);
                if (DEBUG) System.out.println("Connecting chunks");
                for (int i = 1; i <= intBlockCount; i++) {
                    if (DEBUG) System.out.println("Adding chunk " + i + " to " + target.getName());
                    File toRead = new File(frame1.keypool + target.getName() + "-chunk-" + i);
                    fileOut.write(FileAccess.readByteArray(toRead));
                    toRead.deleteOnExit();
                    toRead.delete();
                }
                fileOut.close();
            } catch (IOException e) {
                if (DEBUG) System.out.println("Write Error: " + target.getPath());
            }
        } else {
            target.delete();
            if (DEBUG) System.out.println("!!!!!! Download of " + target.getName() + " failed.");
        }
        return success;
    }
