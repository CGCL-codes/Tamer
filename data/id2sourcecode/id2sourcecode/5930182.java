    public void testAutoFixWithMultipleDirectories() throws Exception {
        TestUtil.printTitle("DataBlockTest:testAutoFixWithMultipleDirectories()");
        File dir = TempFileUtil.createTemporaryDirectory();
        try {
            File[] dataDirectories = new File[4];
            for (int i = 0; i < dataDirectories.length; i++) {
                dataDirectories[i] = new File(dir, Integer.toString(i));
            }
            DataDirectoryConfiguration[] configs = new DataDirectoryConfiguration[dataDirectories.length];
            for (int i = 0; i < configs.length; i++) {
                configs[i] = new DataDirectoryConfiguration(dataDirectories[i].getAbsolutePath(), Long.MAX_VALUE);
            }
            DataBlockUtil dbu = new DataBlockUtil();
            for (int i = 0; i < configs.length; i++) {
                dbu.add(configs[i]);
            }
            ArrayList<BigHash> contrivedHashes = new ArrayList();
            ArrayList<BigHash> realHashes = new ArrayList();
            long totalEntries = DataBlock.getHeadersPerFile() * 10;
            for (int i = 0; i < totalEntries; i++) {
                byte[] randomData = Utils.makeRandomData((int) (Math.random() * 10 * 1024));
                BigHash hash = new BigHash(randomData);
                byte[] hashBytes = hash.toByteArray();
                byte[] copy = new byte[hashBytes.length];
                System.arraycopy(hashBytes, 0, copy, 0, copy.length);
                copy[0] = 0;
                copy[1] = (byte) (i % 2);
                BigHash fakeHash = BigHash.createFromBytes(copy);
                dbu.addData(fakeHash, randomData);
                contrivedHashes.add(fakeHash);
                realHashes.add(hash);
            }
            {
                int fileCount = 0;
                ArrayList<File> filesToCount = new ArrayList();
                for (int i = 0; i < dataDirectories.length; i++) {
                    filesToCount.add(dataDirectories[i]);
                }
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
            DataBlockUtil dbu2 = new DataBlockUtil();
            for (int i = 0; i < configs.length; i++) {
                dbu2.add(configs[i]);
            }
            for (int i = 0; i < contrivedHashes.size(); i++) {
                byte[] data = dbu2.getData(contrivedHashes.get(i));
                BigHash check = new BigHash(data);
                assertTrue("Expected the same hash.", check.equals(realHashes.get(i)));
            }
        } finally {
            IOUtil.recursiveDelete(dir);
        }
    }
