    @Todo(desc = "Why duplicate test?", day = 0, month = 0, year = 0, author = "Unknown")
    public void testAutoFixWithMultipleDirectories2() throws Exception {
        TestUtil.printTitle("DataBlockTest:testAutoFixWithMultipleDirectories2()");
        File dir = TempFileUtil.createTemporaryDirectory();
        Random random = new Random(1);
        try {
            File[] dataDirectories = new File[4];
            for (int i = 0; i < dataDirectories.length; i++) {
                dataDirectories[i] = new File(dir, Integer.toString(i));
            }
            FlatFileTrancheServer ffts = new FlatFileTrancheServer(dir);
            Configuration config = ffts.getConfiguration();
            Set<DataDirectoryConfiguration> ddcs = config.getDataDirectories();
            for (int i = 0; i < dataDirectories.length; i++) {
                ddcs.add(new DataDirectoryConfiguration(dataDirectories[i].getAbsolutePath(), Long.MAX_VALUE));
            }
            UserZipFile uzf = DevUtil.createUser("foo", "bar", new File(dir, "user.temp").getAbsolutePath(), true, false);
            config.addUser(uzf);
            ArrayList<BigHash> contrivedHashes = new ArrayList();
            ArrayList<BigHash> realHashes = new ArrayList();
            long totalEntries = DataBlock.getHeadersPerFile() * 10;
            for (int i = 0; i < totalEntries; i++) {
                byte[] randomData = new byte[(int) (Math.random() * 10 * 1024)];
                random.nextBytes(randomData);
                BigHash hash = new BigHash(randomData);
                byte[] hashBytes = hash.toByteArray();
                byte[] copy = new byte[hashBytes.length];
                System.arraycopy(hashBytes, 0, copy, 0, copy.length);
                copy[0] = 0;
                copy[1] = (byte) (i % 2);
                BigHash fakeHash = BigHash.createFromBytes(copy);
                IOUtil.setData(ffts, uzf.getCertificate(), uzf.getPrivateKey(), fakeHash, randomData);
                contrivedHashes.add(fakeHash);
                realHashes.add(hash);
            }
            {
                System.out.println("B-tree structure.");
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
                Object o = IOUtil.getData(ffts, contrivedHashes.get(i), false).getReturnValueObject();
                byte[] data = null;
                if (o instanceof byte[]) {
                    data = (byte[]) o;
                } else if (o instanceof byte[][]) {
                    data = ((byte[][]) o)[0];
                } else {
                    fail("Expected return object to be type byte[] or byte[][], but wasn't.");
                }
                BigHash check = new BigHash(data);
                assertTrue("Expected the same hash.", check.equals(realHashes.get(i)));
            }
            ffts.close();
            FlatFileTrancheServer ffts2 = new FlatFileTrancheServer(dir);
            ffts2.waitToLoadExistingDataBlocks();
            for (int i = 0; i < contrivedHashes.size(); i++) {
                byte[] data = (byte[]) IOUtil.getData(ffts2, contrivedHashes.get(i), false).getReturnValueObject();
                BigHash check = new BigHash(data);
                assertTrue("Expected the same hash.", check.equals(realHashes.get(i)));
            }
            ffts2.close();
            FlatFileTrancheServer ffts3 = new FlatFileTrancheServer(dir);
            for (int i = 0; i < contrivedHashes.size(); i++) {
                try {
                    byte[] data = (byte[]) IOUtil.getData(ffts3, contrivedHashes.get(i), false).getReturnValueObject();
                    BigHash check = new BigHash(data);
                    assertTrue("Expected the same hash.", check.equals(realHashes.get(i)));
                } catch (FileNotFoundException e) {
                    byte[] data = (byte[]) IOUtil.getData(ffts3, contrivedHashes.get(i), false).getReturnValueObject();
                }
            }
        } finally {
            IOUtil.recursiveDelete(dir);
        }
    }
