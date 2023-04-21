    private ShadowMap(String filename) throws IOException {
        try {
            if (currentDirectory != null) filename = currentDirectory + File.separator + filename;
            FileInputStream fis = new FileInputStream(filename);
            DataInputStream dis = new DataInputStream(fis);
            Projection projection = Projection.getProjection(dis.readByte());
            if (projection == Projection.PERSPECTIVE) worldToRaster = PerspectiveTransform.readFromFile(dis); else worldToRaster = AffineTransform.readFromFile(dis);
            width = dis.readInt();
            height = dis.readInt();
            int offset = 1 + 16 * 4 + 4 + 4;
            if (projection == Projection.PERSPECTIVE) offset += 4 + 4;
            int size = width * height * 4;
            FileChannel fc = fis.getChannel();
            ByteBuffer bf = fc.map(FileChannel.MapMode.READ_ONLY, offset, size);
            data = bf.asFloatBuffer();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
