    @Override
    public void init(Element config) throws IOException {
        this.name = Main.awsBucket;
        try {
            pool = new S3ServicePool(S3ChunkStore.awsCredentials, Main.writeThreads * 2);
            RestS3Service s3Service = pool.borrowObject();
            S3Bucket s3Bucket = s3Service.getBucket(this.name);
            if (s3Bucket == null) {
                s3Bucket = s3Service.createBucket(this.name);
                if (Main.awsCompress) {
                    s3Bucket.addMetadata("compress", "true");
                } else {
                    s3Bucket.addMetadata("compress", "false");
                }
                if (Main.chunkStoreEncryptionEnabled) {
                    s3Bucket.addMetadata("encrypt", "true");
                } else {
                    s3Bucket.addMetadata("encrypt", "false");
                }
                SDFSLogger.getLog().info("created new store " + name);
            }
            if (s3Bucket.containsMetadata("compress")) this.compress = Boolean.parseBoolean((String) s3Bucket.getMetadata("compress")); else this.compress = Main.awsCompress;
            if (s3Bucket.containsMetadata("encrypt")) this.encrypt = Boolean.parseBoolean((String) s3Bucket.getMetadata("encrypt")); else this.encrypt = Main.chunkStoreEncryptionEnabled;
            this.openPosFile();
            pool.returnObject(s3Service);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
