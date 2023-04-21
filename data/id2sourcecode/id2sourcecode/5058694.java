    public static void main(String[] args) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(20000000);
        DataOutputStream data = new DataOutputStream(out);
        ByteBuffer buf = ByteBuffer.allocate(16 + 4 * 600000);
        Object test = new Object();
        byte[] src = out.toByteArray();
        buf.put(src);
        buf.putDouble(1.0 / 3);
        buf.putInt(16000 * 16000);
        buf.putInt(-32000 * 32000);
        int pos = buf.position();
        Gbl.startMeasurement();
        for (float f = 0.0f; f < 600000; f += 1.) buf.putFloat(f);
        Gbl.printElapsedTime();
        buf.position(pos);
        Gbl.startMeasurement();
        for (float f = 0.0f; f < 600000; f += 1.) buf.getFloat();
        Gbl.printElapsedTime();
        System.out.println("old");
        Gbl.startMeasurement();
        for (float f = 0.0f; f < 600000; f += 1.) try {
            data.writeFloat(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gbl.printElapsedTime();
        byte[] bbyte = out.toByteArray();
        DataInputStream datain = new DataInputStream(new ByteArrayInputStream(bbyte, 0, bbyte.length));
        Gbl.startMeasurement();
        for (float f = 0.0f; f < 600000; f += 1.) try {
            datain.readFloat();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gbl.printElapsedTime();
        showMixedData(buf, "Display raw buffer contents");
        try {
            FileOutputStream fos = new FileOutputStream("junk.txt");
            FileChannel fChan = fos.getChannel();
            buf.position(0);
            System.out.println("Bytes written: " + fChan.write(buf));
            System.out.println("File size: " + fChan.size() + "\n");
            fos.close();
            fChan.close();
            fos = null;
            fChan = null;
            buf = null;
            FileChannel rwCh = new RandomAccessFile("junk.txt", "rw").getChannel();
            long fileSize = rwCh.size();
            ByteBuffer mapBuf = rwCh.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            rwCh.close();
            showMixedData(mapBuf, "Map contents");
            mapBuf.position(8);
            mapBuf.putInt(127);
            showMixedData(mapBuf, "Modified map contents");
            FileChannel newInCh = new RandomAccessFile("junk.txt", "r").getChannel();
            ByteBuffer newBuf = ByteBuffer.allocate((int) fileSize);
            System.out.println("Bytes read = " + newInCh.read(newBuf));
            newInCh.close();
            showMixedData(newBuf, "Modified file contents");
            mapBuf.position(0);
            FloatBuffer fBuf = mapBuf.asFloatBuffer();
            fBuf.position(0);
            fBuf.put((float) 1.0 / 6);
            fBuf.put((float) 2.0 / 6);
            fBuf.put((float) 3.0 / 6);
            fBuf.put((float) 4.0 / 6);
            showFloatData(fBuf, "Modified map contents");
            FileChannel floatInCh = new RandomAccessFile("junk.txt", "r").getChannel();
            ByteBuffer anotherBuf = ByteBuffer.allocate((int) fileSize);
            System.out.println("Bytes read = " + floatInCh.read(anotherBuf));
            floatInCh.close();
            anotherBuf.position(0);
            FloatBuffer fileBuf = anotherBuf.asFloatBuffer();
            showFloatData(fileBuf, "Modified file contents");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
