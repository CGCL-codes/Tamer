    public void testSplitAndAutoFix() throws Exception {
        TestUtil.printTitle("DataBlockTest:testSplitAndAutoFix()");
        File dir = TempFileUtil.createTemporaryDirectory();
        DataBlockUtilLog logger = null;
        try {
            DataDirectoryConfiguration ddc = new DataDirectoryConfiguration(dir.getAbsolutePath(), Long.MAX_VALUE);
            DataBlockUtil dbu = new DataBlockUtil();
            dbu.add(ddc);
            DataBlockUtil.setIsLogging(true);
            logger = DataBlockUtil.getLogger();
            ArrayList<BigHash> contrivedHashes = new ArrayList();
            ArrayList<BigHash> realHashes = new ArrayList();
            int bytesWritten = 0;
            int maxFileSize = 100 * 1024;
            while (bytesWritten <= DataBlock.getMaxBlockSize() * 2) {
                byte[] randomData = Utils.makeRandomData((int) (Math.random() * maxFileSize));
                BigHash hash = new BigHash(randomData);
                byte[] hashBytes = hash.toByteArray();
                byte[] copy = new byte[hashBytes.length];
                System.arraycopy(hashBytes, 0, copy, 0, copy.length);
                copy[0] = 0;
                copy[1] = 1;
                BigHash fakeHash = BigHash.createFromBytes(copy);
                dbu.addData(fakeHash, randomData);
                bytesWritten += randomData.length;
                contrivedHashes.add(fakeHash);
                realHashes.add(hash);
            }
            String[] checkForDir = dir.list();
            assertTrue(2 == checkForDir.length || 1 == checkForDir.length);
            boolean atLeastOneDirectory = false;
            for (File f : dir.listFiles()) {
                if (f.isDirectory()) {
                    atLeastOneDirectory = true;
                }
            }
            assertTrue(atLeastOneDirectory);
            {
                int fileCount = 0;
                ArrayList<File> filesToCount = new ArrayList();
                filesToCount.add(dir);
                while (!filesToCount.isEmpty()) {
                    File fileToCount = filesToCount.remove(0);
                    if (fileToCount.isDirectory()) {
                        File[] files = fileToCount.listFiles();
                        for (File file : files) {
                            filesToCount.add(file);
                        }
                    } else {
                        fileCount++;
                    }
                }
                assertTrue("Expected more than one split block files.", 1 < fileCount);
            }
            for (int i = 0; i < contrivedHashes.size(); i++) {
                byte[] data = dbu.getData(contrivedHashes.get(i));
                BigHash check = new BigHash(data);
                assertTrue("Expected the same hash.", check.equals(realHashes.get(i)));
            }
            {
                DataBlock block = null;
                for (int i = 0; i < contrivedHashes.size(); i++) {
                    block = dbu.getDataBlockToAddChunk(contrivedHashes.get(i));
                    if (block.getHashes(false).size() > 0) {
                        break;
                    }
                }
                dbu.purposelyFailMerge = 0;
                try {
                    block.cleanUpDataBlock(true);
                    fail("Expected a failure!");
                } catch (Exception e) {
                }
                dbu.purposelyFailMerge = Integer.MAX_VALUE;
            }
            ArrayList<BigHash> missingData = new ArrayList();
            for (int i = 0; i < contrivedHashes.size(); i++) {
                try {
                    dbu.getData(contrivedHashes.get(i));
                } catch (FileNotFoundException e) {
                    missingData.add(contrivedHashes.get(i));
                }
            }
            assertTrue("Expected at least one hash to be missing from the tree.", missingData.size() > 0);
            File toAddFile = null;
            ArrayList<File> filesToCount = new ArrayList();
            filesToCount.add(dir);
            while (!filesToCount.isEmpty()) {
                File fileToCount = filesToCount.remove(0);
                if (fileToCount.isDirectory()) {
                    File[] files = fileToCount.listFiles();
                    for (File file : files) {
                        filesToCount.add(file);
                    }
                } else {
                    if (fileToCount.getName().endsWith(".backup")) {
                        if (toAddFile != null) {
                            fail("More than one 'toadd' file was found!?");
                        }
                        toAddFile = fileToCount;
                    }
                }
            }
            assertNotNull(toAddFile);
            dbu.mergeOldDataBlock(toAddFile);
            for (int i = 0; i < contrivedHashes.size(); i++) {
                byte[] data = dbu.getData(contrivedHashes.get(i));
                BigHash check = new BigHash(data);
                assertTrue("Expected the same hash.", check.equals(realHashes.get(i)));
            }
            assertTrue("Should be some run time.", logger.getRuntime() > 0);
            assertTrue("Should be some time spent merging.", logger.getTimeSpentMerging() > 0);
            assertTrue("Time spent merging should be less than total log time.", logger.getRuntime() > logger.getTimeSpentMerging());
        } finally {
            IOUtil.recursiveDelete(dir);
            if (logger != null) {
                logger.close();
            }
            DataBlockUtil.setIsLogging(false);
        }
    }
