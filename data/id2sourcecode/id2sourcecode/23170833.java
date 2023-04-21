    public GEMFFile(final String pLocation, final List<File> pSourceFolders) throws FileNotFoundException, IOException {
        this.mLocation = pLocation;
        HashMap<String, HashMap<Integer, HashMap<Integer, HashMap<Integer, File>>>> dirIndex = new HashMap<String, HashMap<Integer, HashMap<Integer, HashMap<Integer, File>>>>();
        for (File sourceDir : pSourceFolders) {
            HashMap<Integer, HashMap<Integer, HashMap<Integer, File>>> zList = new HashMap<Integer, HashMap<Integer, HashMap<Integer, File>>>();
            for (File zDir : sourceDir.listFiles()) {
                try {
                    Integer.parseInt(zDir.getName());
                } catch (NumberFormatException e) {
                    continue;
                }
                HashMap<Integer, HashMap<Integer, File>> xList = new HashMap<Integer, HashMap<Integer, File>>();
                for (File xDir : zDir.listFiles()) {
                    try {
                        Integer.parseInt(xDir.getName());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    HashMap<Integer, File> yList = new HashMap<Integer, File>();
                    for (File yFile : xDir.listFiles()) {
                        try {
                            Integer.parseInt(yFile.getName().substring(0, yFile.getName().indexOf('.')));
                        } catch (NumberFormatException e) {
                            continue;
                        }
                        yList.put(Integer.parseInt(yFile.getName().substring(0, yFile.getName().indexOf('.'))), yFile);
                    }
                    xList.put(new Integer(xDir.getName()), yList);
                }
                zList.put(Integer.parseInt(zDir.getName()), xList);
            }
            dirIndex.put(sourceDir.getName(), zList);
        }
        HashMap<String, Integer> sourceIndex = new HashMap<String, Integer>();
        HashMap<Integer, String> indexSource = new HashMap<Integer, String>();
        int si = 0;
        for (String source : dirIndex.keySet()) {
            sourceIndex.put(source, new Integer(si));
            indexSource.put(new Integer(si), source);
            ++si;
        }
        List<GEMFRange> ranges = new ArrayList<GEMFRange>();
        for (String source : dirIndex.keySet()) {
            for (Integer zoom : dirIndex.get(source).keySet()) {
                HashMap<List<Integer>, List<Integer>> ySets = new HashMap<List<Integer>, List<Integer>>();
                for (Integer x : dirIndex.get(source).get(zoom).keySet()) {
                    List<Integer> ySet = new ArrayList<Integer>();
                    for (Integer y : dirIndex.get(source).get(zoom).get(x).keySet()) {
                        ySet.add(y);
                    }
                    if (!ySets.containsKey(ySet)) {
                        ySets.put(ySet, new ArrayList<Integer>());
                    }
                    ySets.get(ySet).add(x);
                }
                HashMap<List<Integer>, List<Integer>> xSets = new HashMap<List<Integer>, List<Integer>>();
                for (List<Integer> ySet : ySets.keySet()) {
                    TreeSet<Integer> xList = new TreeSet<Integer>(ySets.get(ySet));
                    List<Integer> xSet = new ArrayList<Integer>();
                    for (int i = xList.first(); i < xList.last() + 1; ++i) {
                        if (xList.contains(new Integer(i))) {
                            xSet.add(new Integer(i));
                        } else {
                            if (xSet.size() > 0) {
                                xSets.put(ySet, xSet);
                                xSet = new ArrayList<Integer>();
                            }
                        }
                    }
                    xSets.put(ySet, xSet);
                }
                for (List<Integer> xSet : xSets.keySet()) {
                    TreeSet<Integer> yList = new TreeSet<Integer>(xSet);
                    TreeSet<Integer> xList = new TreeSet<Integer>(ySets.get(xSet));
                    GEMFRange range = new GEMFFile.GEMFRange();
                    range.zoom = zoom;
                    range.sourceIndex = sourceIndex.get(source);
                    range.xMin = xList.first();
                    range.xMax = xList.last();
                    for (int i = yList.first(); i < yList.last() + 1; ++i) {
                        if (yList.contains(new Integer(i))) {
                            if (range.yMin == null) range.yMin = i;
                            range.yMax = i;
                        } else {
                            ranges.add(range);
                            range = new GEMFFile.GEMFRange();
                            range.zoom = zoom;
                            range.sourceIndex = sourceIndex.get(source);
                            range.xMin = xList.first();
                            range.xMax = xList.last();
                        }
                    }
                    ranges.add(range);
                }
            }
        }
        int source_list_size = 0;
        for (String source : sourceIndex.keySet()) source_list_size += (U32_SIZE + U32_SIZE + source.length());
        long offset = U32_SIZE + U32_SIZE + U32_SIZE + source_list_size + ranges.size() * ((U32_SIZE * 6) + U64_SIZE) + U32_SIZE;
        for (GEMFRange range : ranges) {
            range.offset = offset;
            for (int x = range.xMin; x < range.xMax + 1; ++x) {
                for (int y = range.yMin; y < range.yMax + 1; ++y) {
                    offset += (U32_SIZE + U64_SIZE);
                }
            }
        }
        long headerSize = offset;
        RandomAccessFile gemfFile = new RandomAccessFile(pLocation, "rw");
        gemfFile.writeInt(VERSION);
        gemfFile.writeInt(TILE_SIZE);
        gemfFile.writeInt(sourceIndex.size());
        for (String source : sourceIndex.keySet()) {
            gemfFile.writeInt(sourceIndex.get(source));
            gemfFile.writeInt(source.length());
            gemfFile.write(source.getBytes());
        }
        gemfFile.writeInt(ranges.size());
        for (GEMFRange range : ranges) {
            gemfFile.writeInt(range.zoom);
            gemfFile.writeInt(range.xMin);
            gemfFile.writeInt(range.xMax);
            gemfFile.writeInt(range.yMin);
            gemfFile.writeInt(range.yMax);
            gemfFile.writeInt(range.sourceIndex);
            gemfFile.writeLong(range.offset);
        }
        for (GEMFRange range : ranges) {
            for (int x = range.xMin; x < range.xMax + 1; ++x) {
                for (int y = range.yMin; y < range.yMax + 1; ++y) {
                    gemfFile.writeLong(offset);
                    long fileSize = dirIndex.get(indexSource.get(range.sourceIndex)).get(range.zoom).get(x).get(y).length();
                    gemfFile.writeInt((int) fileSize);
                    offset += fileSize;
                }
            }
        }
        byte[] buf = new byte[FILE_COPY_BUFFER_SIZE];
        long currentOffset = headerSize;
        int fileIndex = 0;
        for (GEMFRange range : ranges) {
            for (int x = range.xMin; x < range.xMax + 1; ++x) {
                for (int y = range.yMin; y < range.yMax + 1; ++y) {
                    long fileSize = dirIndex.get(indexSource.get(range.sourceIndex)).get(range.zoom).get(x).get(y).length();
                    if (currentOffset + fileSize > FILE_SIZE_LIMIT) {
                        gemfFile.close();
                        ++fileIndex;
                        gemfFile = new RandomAccessFile(pLocation + "-" + fileIndex, "rw");
                        currentOffset = 0;
                    } else {
                        currentOffset += fileSize;
                    }
                    FileInputStream tile = new FileInputStream(dirIndex.get(indexSource.get(range.sourceIndex)).get(range.zoom).get(x).get(y));
                    int read = tile.read(buf, 0, FILE_COPY_BUFFER_SIZE);
                    while (read != -1) {
                        gemfFile.write(buf, 0, read);
                        read = tile.read(buf, 0, FILE_COPY_BUFFER_SIZE);
                    }
                    tile.close();
                }
            }
        }
        gemfFile.close();
        openFiles();
        readHeader();
    }
