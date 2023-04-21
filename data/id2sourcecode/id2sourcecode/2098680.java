    public Replica(Mapping map) {
        super();
        this.machineID = map.getMachineID();
        this.replicaID = map.getReplicaID();
        this.virtualChunkID = map.getVirtualChunkID();
        this.physicalChunkID = map.getPhyscialChunkID();
        this.volumeID = map.getVolumeID();
        datFile = new File(this.volumeID + File.separator + this.virtualChunkID + File.separator + this.machineID + "-" + this.physicalChunkID + ".dat");
        if (Config.STORAGEMACHINE_CHUNK_SIZE % ISCSI.DEFAULT_DISK_BLOCK_SIZE != 0) {
            logger.error("Chunk size must be a multiple of disk block size!");
            return;
        }
        this.maskSize = (long) (Config.STORAGEMACHINE_CHUNK_SIZE / ISCSI.DEFAULT_DISK_BLOCK_SIZE);
        try {
            logger.debug("File name is " + datFile.getName());
            if (datFile.exists() == false) {
                new File(this.volumeID + "").mkdir();
                new File(this.volumeID + File.separator + this.virtualChunkID).mkdir();
                datFile.createNewFile();
                logger.info("Create a new file");
            }
            if (datFile.length() != this.maskSize) {
                RandomAccessFile f = new RandomAccessFile(this.datFile, "rw");
                f.setLength(this.maskSize);
                f.close();
                logger.info("Set the file to size " + this.maskSize);
            }
            this.raf = new RandomAccessFile(this.datFile, "rwd");
            this.data = this.raf.getChannel();
            logger.info("Mask for [" + datFile.getName() + "] inits");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
