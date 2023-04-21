    public TreadmillDriver(ServerInterpreter server, String spaceName, LargeObjectSpace lospace, int blockSize, int threshold, boolean mainSpace) {
        super(server, spaceName, lospace, blockSize, mainSpace);
        if (DEBUG) {
            Log.write("TreadmillDriver for ");
            Log.write(spaceName);
            Log.write(", blocksize=");
            Log.write(blockSize);
            Log.write(", start=");
            Log.write(lospace.getStart());
            Log.write(", extent=");
            Log.write(lospace.getExtent());
            Log.write(", maxTileNum=");
            Log.writeln(maxTileNum);
        }
        this.threshold = threshold;
        subspace = createSubspace(lospace);
        allTileNum = 0;
        maxAddr = lospace.getStart();
        usedSpaceStream = createUsedSpaceStream();
        objectsStream = createObjectsStream();
        rootsStream = createRootsStream();
        refFromImmortalStream = createRefFromImmortalStream();
        serverSpace.resize(0);
        resetData();
    }
