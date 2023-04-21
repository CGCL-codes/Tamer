    public GEMFFileCreator(final String pLocation, final List<File> pSourceFolders) throws FileNotFoundException, IOException {
        final LinkedHashMap<String, LinkedHashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, File>>>> dirIndex = new LinkedHashMap<String, LinkedHashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, File>>>>();
        for (final File sourceDir : pSourceFolders) {
            final LinkedHashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, File>>> zList = new LinkedHashMap<Integer, LinkedHashMap<Integer, LinkedHashMap<Integer, File>>>();
            for (final File zDir : sourceDir.listFiles()) {
                try {
                    Integer.parseInt(zDir.getName());
                } catch (final NumberFormatException e) {
                    continue;
                }
                final LinkedHashMap<Integer, LinkedHashMap<Integer, File>> xList = new LinkedHashMap<Integer, LinkedHashMap<Integer, File>>();
                for (final File xDir : zDir.listFiles()) {
                    try {
                        Integer.parseInt(xDir.getName());
                    } catch (final NumberFormatException e) {
                        continue;
                    }
                    final LinkedHashMap<Integer, File> yList = new LinkedHashMap<Integer, File>();
                    for (final File yFile : xDir.listFiles()) {
                        try {
                            Integer.parseInt(yFile.getName().substring(0, yFile.getName().indexOf('.')));
                        } catch (final NumberFormatException e) {
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
        final LinkedHashMap<String, Integer> sourceIndex = new LinkedHashMap<String, Integer>();
        final LinkedHashMap<Integer, String> indexSource = new LinkedHashMap<Integer, String>();
        int si = 0;
        for (final String source : dirIndex.keySet()) {
            sourceIndex.put(source, new Integer(si));
            indexSource.put(new Integer(si), source);
            ++si;
        }
        final List<GEMFRange> ranges = new ArrayList<GEMFRange>();
        for (final String source : dirIndex.keySet()) {
            for (final Integer zoom : dirIndex.get(source).keySet()) {
                final LinkedHashMap<List<Integer>, List<Integer>> ySets = new LinkedHashMap<List<Integer>, List<Integer>>();
                for (final Integer x : new TreeSet<Integer>(dirIndex.get(source).get(zoom).keySet())) {
                    final List<Integer> ySet = new ArrayList<Integer>();
                    for (final Integer y : dirIndex.get(source).get(zoom).get(x).keySet()) {
                        ySet.add(y);
                    }
                    if (ySet.size() == 0) {
                        continue;
                    }
                    Collections.sort(ySet);
                    if (!ySets.containsKey(ySet)) {
                        ySets.put(ySet, new ArrayList<Integer>());
                    }
                    ySets.get(ySet).add(x);
                }
                final LinkedHashMap<List<Integer>, List<Integer>> xSets = new LinkedHashMap<List<Integer>, List<Integer>>();
                for (final List<Integer> ySet : ySets.keySet()) {
                    final TreeSet<Integer> xList = new TreeSet<Integer>(ySets.get(ySet));
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
                    if (xSet.size() > 0) {
                        xSets.put(ySet, xSet);
                    }
                }
                for (final List<Integer> xSet : xSets.keySet()) {
                    final TreeSet<Integer> yList = new TreeSet<Integer>(xSet);
                    final TreeSet<Integer> xList = new TreeSet<Integer>(ySets.get(xSet));
                    GEMFRange range = new GEMFRange();
                    range.zoom = zoom;
                    range.sourceIndex = sourceIndex.get(source);
                    range.xMin = xList.first();
                    range.xMax = xList.last();
                    for (int i = yList.first(); i < yList.last() + 1; ++i) {
                        if (yList.contains(new Integer(i))) {
                            if (range.yMin == null) {
                                range.yMin = i;
                            }
                            range.yMax = i;
                        } else {
                            if (range.yMin != null) {
                                ranges.add(range);
                                range = new GEMFRange();
                                range.zoom = zoom;
                                range.sourceIndex = sourceIndex.get(source);
                                range.xMin = xList.first();
                                range.xMax = xList.last();
                            }
                        }
                    }
                    if (range.yMin != null) {
                        ranges.add(range);
                    }
                }
            }
        }
        int source_list_size = 0;
        for (final String source : sourceIndex.keySet()) {
            source_list_size += (U32_SIZE + U32_SIZE + source.length());
        }
        long offset = U32_SIZE + U32_SIZE + U32_SIZE + source_list_size + ranges.size() * ((U32_SIZE * 6) + U64_SIZE) + U32_SIZE;
        for (final GEMFRange range : ranges) {
            range.offset = offset;
            for (int x = range.xMin; x < range.xMax + 1; ++x) {
                for (int y = range.yMin; y < range.yMax + 1; ++y) {
                    offset += (U32_SIZE + U64_SIZE);
                }
            }
        }
        final long headerSize = offset;
        RandomAccessFile gemfFile = new RandomAccessFile(pLocation, "rw");
        gemfFile.writeInt(VERSION);
        gemfFile.writeInt(TILE_SIZE);
        gemfFile.writeInt(sourceIndex.size());
        for (final String source : sourceIndex.keySet()) {
            gemfFile.writeInt(sourceIndex.get(source));
            gemfFile.writeInt(source.length());
            gemfFile.write(source.getBytes());
        }
        gemfFile.writeInt(ranges.size());
        for (final GEMFRange range : ranges) {
            gemfFile.writeInt(range.zoom);
            gemfFile.writeInt(range.xMin);
            gemfFile.writeInt(range.xMax);
            gemfFile.writeInt(range.yMin);
            gemfFile.writeInt(range.yMax);
            gemfFile.writeInt(range.sourceIndex);
            gemfFile.writeLong(range.offset);
        }
        for (final GEMFRange range : ranges) {
            for (int x = range.xMin; x < range.xMax + 1; ++x) {
                for (int y = range.yMin; y < range.yMax + 1; ++y) {
                    gemfFile.writeLong(offset);
                    final long fileSize = dirIndex.get(indexSource.get(range.sourceIndex)).get(range.zoom).get(x).get(y).length();
                    gemfFile.writeInt((int) fileSize);
                    offset += fileSize;
                }
            }
        }
        final byte[] buf = new byte[FILE_COPY_BUFFER_SIZE];
        long currentOffset = headerSize;
        int fileIndex = 0;
        for (final GEMFRange range : ranges) {
            for (int x = range.xMin; x < range.xMax + 1; ++x) {
                for (int y = range.yMin; y < range.yMax + 1; ++y) {
                    final long fileSize = dirIndex.get(indexSource.get(range.sourceIndex)).get(range.zoom).get(x).get(y).length();
                    if (currentOffset + fileSize > FILE_SIZE_LIMIT) {
                        gemfFile.close();
                        ++fileIndex;
                        gemfFile = new RandomAccessFile(pLocation + "-" + fileIndex, "rw");
                        currentOffset = 0;
                    } else {
                        currentOffset += fileSize;
                    }
                    final FileInputStream tile = new FileInputStream(dirIndex.get(indexSource.get(range.sourceIndex)).get(range.zoom).get(x).get(y));
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
    }
