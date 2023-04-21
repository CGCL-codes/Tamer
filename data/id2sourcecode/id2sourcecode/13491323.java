    public boolean writeDeviceToCompactFile(String fileName) {
        for (Tile[] tiles : getTiles()) {
            for (Tile tile : tiles) {
                tile.setDevice(this);
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            Hessian2Output h2os = new Hessian2Output(fos);
            Deflation deflate = new Deflation();
            Hessian2Output hos = deflate.wrap(h2os);
            Long[] locations = new Long[2];
            locations[1] = fos.getChannel().position();
            System.out.println("\n");
            MessageGenerator.printHeader("File Usage Statistics");
            hos.writeString(deviceFileVersion);
            debugWritingSize(hos, fos, "deviceFileVersion", locations);
            hos.writeInt(rows);
            debugWritingSize(hos, fos, "tileRows", locations);
            hos.writeInt(columns);
            debugWritingSize(hos, fos, "tileColumns", locations);
            hos.writeInt(wirePool.getEnumerations().size());
            for (WireConnection w : wirePool.getEnumerations()) {
                int mask = w.isPIP() ? 0x80000000 : 0x0;
                hos.writeInt(mask | (w.getWire()));
                hos.writeInt((w.getRowOffset() << 16) | (w.getColumnOffset() & 0xFFFF));
            }
            debugWritingSize(hos, fos, "wirePool", locations);
            hos.writeInt(wireArrayPool.getEnumerations().size());
            for (WireArray wireArray : wireArrayPool.getEnumerations()) {
                hos.writeInt(wireArray.array.length);
                for (WireConnection w : wireArray.array) {
                    hos.writeInt(wirePool.getEnumerationValue(w));
                }
            }
            debugWritingSize(hos, fos, "wireArrayPool", locations);
            hos.writeInt(wireConnectionPool.getEnumerations().size());
            for (WireArrayConnection wc : wireConnectionPool.getEnumerations()) {
                hos.writeInt(wc.wire);
                hos.writeInt(wc.wireArrayEnum);
            }
            debugWritingSize(hos, fos, "wireConnectionPool", locations);
            hos.writeInt(tileSinksPool.getEnumerations().size());
            for (TileSinks s : tileSinksPool.getEnumerations()) {
                hos.writeInt(s.sinks.size());
                for (Integer key : s.sinks.keySet()) {
                    SinkPin sp = s.sinks.get(key);
                    hos.writeInt(key);
                    hos.writeInt(sp.switchMatrixSinkWire);
                    hos.writeInt(sp.switchMatrixTileOffset);
                }
            }
            debugWritingSize(hos, fos, "tileSinksPool", locations);
            hos.writeInt(tileSourcesPool.getEnumerations().size());
            for (TileSources s : tileSourcesPool.getEnumerations()) {
                FileTools.writeIntArray(hos, s.sources);
            }
            debugWritingSize(hos, fos, "tileSourcesPool", locations);
            hos.writeInt(tileWiresPool.getEnumerations().size());
            for (TileWires tw : tileWiresPool.getEnumerations()) {
                FileTools.writeWireHashMap(hos, tw.wires, wireArrayPool, wireConnectionPool);
            }
            debugWritingSize(hos, fos, "tileWiresPool", locations);
            int index = 0;
            String[] tileNames = new String[rows * columns];
            int[] tileTypes = new int[rows * columns];
            int[] tileSinks = new int[rows * columns];
            int[] tileSources = new int[rows * columns];
            int[] tileWires = new int[rows * columns];
            int[] primitiveSitesCount = new int[rows * columns];
            for (Tile[] tileArray : tiles) {
                for (Tile t : tileArray) {
                    tileNames[index] = t.getName();
                    tileTypes[index] = t.getType().ordinal();
                    tileSinks[index] = tileSinksPool.getEnumerationValue(new TileSinks(t.getSinks()));
                    tileSources[index] = tileSourcesPool.getEnumerationValue(new TileSources(t.getSources()));
                    tileWires[index] = tileWiresPool.getEnumerationValue(new TileWires(t.getWireHashMap()));
                    primitiveSitesCount[index] = t.getPrimitiveSites() == null ? 0 : t.getPrimitiveSites().length;
                    index++;
                }
            }
            FileTools.writeStringArray(hos, tileNames);
            FileTools.writeIntArray(hos, tileTypes);
            FileTools.writeIntArray(hos, tileSinks);
            FileTools.writeIntArray(hos, tileSources);
            FileTools.writeIntArray(hos, tileWires);
            FileTools.writeIntArray(hos, primitiveSitesCount);
            debugWritingSize(hos, fos, "tiles[][]", locations);
            hos.writeString(partName);
            debugWritingSize(hos, fos, "partName", locations);
            hos.writeInt(primitivePinPool.getEnumerations().size());
            for (PrimitivePinMap map : primitivePinPool.getEnumerations()) {
                FileTools.writeHashMap(hos, map.pins);
            }
            debugWritingSize(hos, fos, "primitivePinPool", locations);
            hos.writeInt(primitiveSites.values().size());
            for (Tile[] tileArray : tiles) {
                for (Tile t : tileArray) {
                    if (t.getPrimitiveSites() != null) {
                        for (PrimitiveSite p : t.getPrimitiveSites()) {
                            FileTools.writePrimitiveSite(hos, p, this, primitivePinPool);
                        }
                    }
                }
            }
            debugWritingSize(hos, fos, "primitives", locations);
            hos.writeInt(routeThroughMap.size());
            for (WireConnection w : routeThroughMap.keySet()) {
                PIPRouteThrough p = routeThroughMap.get(w);
                hos.writeInt(p.getType().ordinal());
                hos.writeInt(p.getInWire());
                hos.writeInt(p.getOutWire());
                hos.writeInt(wirePool.getEnumerationValue(w));
            }
            debugWritingSize(hos, fos, "routeThroughMap", locations);
            System.out.println("------------------------------------------");
            System.out.printf("%10d bytes : %s\n\n", (fos.getChannel().position()), "Total");
            hos.close();
            fos.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
