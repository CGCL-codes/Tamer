        public void run() {
            try {
                dataFile = new File("/root/tirar/gvSIGtmp.gvs");
                FileOutputStream fs = new FileOutputStream(dataFile);
                channel = fs.getChannel();
                indexFile = new File("/root/tirar/gvSIGindex.gvi");
                FileOutputStream indexfs = new FileOutputStream(indexFile);
                indexChannel = indexfs.getChannel();
                IVectorialDatabaseDriver d = (IVectorialDatabaseDriver) VectorialDisconnectedDBAdapter.this.driver;
                connectedAdapter = new VectorialDBAdapter();
                connectedAdapter.setDriver(d);
                indexBuffer.clear();
                indexBuffer.putInt(connectedAdapter.getRecordset().getFieldCount());
                indexBuffer.flip();
                indexChannel.write(indexBuffer);
                indexChannel.position(indexChannel.position() + 4);
                indexChannel.position(indexChannel.position() + 4 * 8);
                connectedAdapter.start();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bytes);
                int geom = 0;
                for (int i = 0; i < connectedAdapter.getShapeCount(); i++) {
                    geom++;
                    IGeometry g = connectedAdapter.getShape(i);
                    extent.add(g.getBounds2D());
                    bytes = new ByteArrayOutputStream();
                    oos = new ObjectOutputStream(bytes);
                    oos.writeObject(g);
                    oos.close();
                    writeObject(bytes.toByteArray());
                    for (int j = 0; j < connectedAdapter.getRecordset().getFieldCount(); j++) {
                        Value v = connectedAdapter.getRecordset().getFieldValue(i, j);
                        bytes = new ByteArrayOutputStream();
                        oos = new ObjectOutputStream(bytes);
                        oos.writeObject(v);
                        oos.close();
                        writeObject(bytes.toByteArray());
                    }
                }
                indexBuffer.clear();
                indexBuffer.putInt(geom);
                indexBuffer.flip();
                indexChannel.position(4);
                indexChannel.write(indexBuffer);
                indexChannel.position(indexChannel.position() + 4);
                ByteBuffer extentBuffer = ByteBuffer.allocate(4 * 8);
                extentBuffer.putDouble(extent.getMinX());
                extentBuffer.putDouble(extent.getMinY());
                extentBuffer.putDouble(extent.getMaxX());
                extentBuffer.putDouble(extent.getMaxY());
                extentBuffer.flip();
                indexChannel.position(8);
                indexChannel.write(extentBuffer);
                channel.close();
                indexChannel.close();
                connectedAdapter.stop();
                status = LOCAL;
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ReadDriverException e) {
                throw new RuntimeException(e);
            }
        }
