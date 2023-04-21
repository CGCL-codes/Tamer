    private static AbstractPVRFileSystem advancedDetection(final File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteBuffer buf = ByteBuffer.allocateDirect(AbstractPVRFileSystem.CLUSTER_SIZE);
        LOGGER.fine("Magic value not found! starting advanced detection");
        for (int i = 1; (i < MAX_SEARCH); i++) {
            buf.rewind();
            fc.read(buf, i * AbstractPVRFileSystem.CLUSTER_SIZE);
            reverse(buf);
            if (isS1TOC(buf)) {
                LOGGER.info("Digicorder S/C/T 1 identified by " + "TOC-heuristic");
                fis.close();
                return new PVRV1FileSystem(file, i);
            }
            for (int j = 0; j < SECTORS_PER_CLUST; ++j) {
                buf.position(BYTES_PER_SECTOR * j);
                if (isS2TOC(buf)) {
                    LOGGER.info("Digicorder S/C/T 2 identified by " + "TOC-heuristic " + i + "/" + j);
                    fis.close();
                    return new PVRV2FileSystem(file, i, j);
                }
            }
        }
        LOGGER.info(file.getName() + " doesn't seem to be a TSD volume");
        fis.close();
        return null;
    }
