    public void run() {
        cacheMapFile = new File(path + fileSeperator + "_CACHE_MAP_");
        cacheOneFile = new File(path + fileSeperator + "_CACHE_001_");
        cacheTwoFile = new File(path + fileSeperator + "_CACHE_002_");
        cacheThreeFile = new File(path + fileSeperator + "_CACHE_003_");
        try {
            FileInputStream fis = new FileInputStream(cacheMapFile);
            fileChannel = fis.getChannel();
            FileInputStream fisCacheOne = new FileInputStream(cacheOneFile);
            FileInputStream fisCacheTwo = new FileInputStream(cacheTwoFile);
            FileInputStream fisCacheThree = new FileInputStream(cacheThreeFile);
            cacheChannels[0] = fisCacheOne.getChannel();
            cacheChannels[1] = fisCacheTwo.getChannel();
            cacheChannels[2] = fisCacheThree.getChannel();
            ByteBuffer headerBuffer = ByteBuffer.allocate(MozCacheMapHeader.getHeaderSize());
            fileChannel.position(0);
            fileChannel.read(headerBuffer);
            MozCacheMapHeader cacheMapHeader = new MozCacheMapHeader(headerBuffer);
            System.out.println(cacheMapHeader);
            int[] buckets = cacheMapHeader.getBucketUsage();
            for (int i = 0; i < buckets.length; i++) {
                int entries = buckets[i];
                entries = 256;
                ByteBuffer bucketBuffer = ByteBuffer.allocate(MozCacheBucket.BYTE_SIZE);
                fileChannel.read(bucketBuffer);
                MozCacheBucket cacheBucket = new MozCacheBucket(entries, bucketBuffer);
                bucketList.add(cacheBucket);
                List<MozCacheRecord> records = cacheBucket.recordList;
                for (MozCacheRecord record : records) {
                    if (record.getHashNumber() != 0) {
                        CacheRecord cacheRecord = new CacheRecord();
                        cacheRecord.setHashNumber(record.getHashNumber());
                        int cacheNum = record.getMetaFileLocation();
                        int blockSize = record.getBlockSize(record.getMetaFileLocation());
                        int dataSize = 0;
                        if (cacheNum > 0) {
                            int recordSize = record.getMetaBlockCount() * blockSize;
                            int recordPosition = record.getMetaStartBlock() * blockSize;
                            {
                                FileChannel channel = cacheChannels[cacheNum - 1];
                                channel.position(MozCacheBlockFile.HEADER_SIZE + recordPosition);
                                ByteBuffer cacheBuffer = ByteBuffer.allocate(recordSize);
                                channel.read(cacheBuffer);
                                cacheBuffer.position(0);
                                int major = cacheBuffer.getShort();
                                int minor = cacheBuffer.getShort();
                                int metaLoc = cacheBuffer.getInt();
                                int fetchCount = cacheBuffer.getInt();
                                int lastFetched = cacheBuffer.getInt();
                                int lastModifited = cacheBuffer.getInt();
                                int expTime = cacheBuffer.getInt();
                                dataSize = cacheBuffer.getInt();
                                int keySize = cacheBuffer.getInt();
                                int metaDataSize = cacheBuffer.getInt();
                                byte[] keyBytes = new byte[keySize];
                                byte[] metaDataBytes = new byte[metaDataSize];
                                cacheBuffer.get(keyBytes);
                                cacheBuffer.get(metaDataBytes);
                                String key = new String(keyBytes);
                                int keyEnd = key.indexOf(":");
                                key = key.substring(keyEnd + 1);
                                cacheRecord.setUrl(key);
                                ByteBuffer metaData = ByteBuffer.wrap(metaDataBytes);
                                metaData.position(0);
                                while (metaData.position() < metaData.limit()) {
                                    String line = Util.readString(metaData, metaData.position());
                                    line = line.trim();
                                    if (line.equalsIgnoreCase("request-method")) {
                                        Util.readString(metaData, metaData.position());
                                    } else if (line.equalsIgnoreCase("response-head")) {
                                        String responseHeader = Util.readString(metaData, metaData.position());
                                        cacheRecord.parseHeaders(new String(responseHeader));
                                    }
                                }
                            }
                        } else if (record.getHashNumber() != 0x0) {
                            String filePath = path + fileSeperator + record.getMetaFileName();
                            record.isValid();
                            System.out.println("Meta cache in external file " + filePath);
                        }
                        cacheNum = record.getDataFileLocation();
                        blockSize = record.getBlockSize(record.getDataFileLocation());
                        if (cacheNum > 0) {
                            int recordSize = record.getDataBlockCount() * blockSize;
                            int recordPosition = record.getDataStartBlock() * blockSize;
                            {
                                FileChannel channel = cacheChannels[cacheNum - 1];
                                channel.position(MozCacheBlockFile.HEADER_SIZE + recordPosition);
                                ByteBuffer cacheBuffer = ByteBuffer.allocate(dataSize);
                                channel.read(cacheBuffer);
                                if (dataSize <= recordSize) {
                                    cacheRecord.setData(cacheBuffer.array());
                                } else {
                                    System.out.println("Warning: The size specified in the meta data is larger than the data record");
                                }
                            }
                        } else if (record.getHashNumber() != 0x0) {
                            String filePath = path + fileSeperator + record.getDataFileName();
                            cacheRecord.setFileName(filePath);
                        }
                        System.out.println();
                        fireUpdate(cacheRecord);
                    }
                }
            }
            fileChannel.close();
            fireComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
