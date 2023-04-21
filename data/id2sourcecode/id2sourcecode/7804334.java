        private void hash() {
            hashing = true;
            while (!fileToBeHashed.isEmpty()) {
                FileInfo fileInfo;
                synchronized (fileToBeHashed) {
                    fileInfo = fileToBeHashed.pop();
                }
                try {
                    MessageDigest tt = new TTH();
                    logger.debug(tt.getAlgorithm());
                    FileInputStream fis;
                    fis = new FileInputStream(fileInfo.getAbsolutePath());
                    int read;
                    byte[] in = new byte[1024];
                    long total = 0;
                    HashInfo hashInfo = new HashInfo();
                    HashTree hashTree = new HashTree(hashInfo);
                    long leafSize = Math.max(hashTree.calculateBlockSize(fileInfo.getLength(), 10), MIN_BLOCK_SIZE);
                    hashInfo.setLeafSize(leafSize);
                    while ((read = fis.read(in)) > -1) {
                        tt.update(in, 0, read);
                        total = total + read;
                        if (total % hashInfo.getLeafSize() == 0) {
                            byte[] digest = tt.digest();
                            ByteBuffer buffer = ByteBuffer.wrap(digest);
                            hashTree.addLeaf(buffer);
                            tt.reset();
                        }
                        if (total % 102400 == 0) {
                            fireHashingFile(fileInfo.getName(), ((double) total / (double) fileInfo.getLength()));
                        }
                    }
                    fis.close();
                    if (total % hashInfo.getLeafSize() > 0) {
                        byte[] digest = tt.digest();
                        ByteBuffer buffer = ByteBuffer.wrap(digest);
                        hashTree.addLeaf(buffer);
                        tt.reset();
                    }
                    hashInfo.setLeafNumber(hashTree.getLeaves().size());
                    List tmpLeaves = Collections.unmodifiableList(hashTree.getLeaves());
                    hashTree.calculateRoot(tmpLeaves);
                    String TTH = hashTree.getBase32Root();
                    logger.debug("TTH:" + TTH);
                    hashInfo.setRoot(TTH);
                    HashStore.getInstance().addHashTree(hashTree);
                    fileInfo.setHash(hashInfo);
                    hashedFiles.add(fileInfo);
                    fireFileHashed(fileInfo.getName());
                    tt.reset();
                } catch (NoSuchAlgorithmException e) {
                    logger.error("Unable to Hash: No algorithm", e);
                } catch (FileNotFoundException e) {
                    logger.error("Unable to Hash: File not found", e);
                } catch (IOException e) {
                    logger.error("Unable to Hash: IO Exception", e);
                }
            }
            hashing = false;
            fireFileHashed("");
        }
