    private void manageDiskCheckpoint(String task) {
        try {
            boolean compress = false;
            if (compressionMode == 2) {
                compress = true;
            } else if (compressionMode == 1) {
                CompressionStats prevStats = compression.get(task);
                if (prevStats == null || (prevStats != null && prevStats.compress)) {
                    compress = true;
                }
            }
            long time = System.currentTimeMillis();
            if (compress) {
                log.info("Compressing disks to upload.");
                new File(checkPath + "/" + task + "/" + task + "-home.img").delete();
                Compressor.compressFileGz(poolPath + "/" + task + "/home.img", checkPath + "/" + task + "/" + task + "-home.img.gz");
                long srcSize = new File(poolPath + "/" + task + "/home.img").length();
                long dstSize = new File(checkPath + "/" + task + "/" + task + "-home.img.gz").length();
                CompressionStats stats = new CompressionStats();
                stats.compressTime = System.currentTimeMillis() - time;
                stats.srcSize = srcSize;
                stats.dstSize = dstSize;
                stats.compress = true;
                compression.put(task, stats);
            } else {
                log.info("Copying home disks to upload.");
                new File(checkPath + "/" + task + "/" + task + "-home.img.gz").delete();
                FileUtils.copyFile(new File(poolPath + "/" + task + "/home.img"), new File(checkPath + "/" + task + "/" + task + "-home.img"));
            }
            if (wholeCheckpoint) {
                log.info("Copying the other disks");
                if (new File(checkPath + "/" + task + "/" + task + "-debianbase.img").exists()) new File(checkPath + "/" + task + "/" + task + "-debianbase.img").delete();
                FileUtils.copyFile(new File(poolPath + "/" + task + "/debianbase.img"), new File(checkPath + "/" + task + "/" + task + "-debianbase.img"));
                if (new File(poolPath + "/" + task + "/breinAplic.img").exists()) {
                    if (new File(checkPath + "/" + task + "/" + task + "-breinAplic.img").exists()) new File(checkPath + "/" + task + "/" + task + "-breinAplic.img").delete();
                    FileUtils.copyFile(new File(poolPath + "/" + task + "/breinAplic.img"), new File(checkPath + "/" + task + "/" + task + "-breinAplic.img"));
                }
            }
            time = System.currentTimeMillis() - time;
            log.info("Checkpoint file manage: " + time + "ms");
        } catch (IOException e) {
            log.error("Managing checkpoint disks of task " + task + ": " + e.getMessage());
        }
    }
