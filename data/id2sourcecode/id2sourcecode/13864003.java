    public DBFHeader(String filename) {
        isvalid = false;
        try {
            int i;
            FileInputStream fis = new FileInputStream(filename);
            FileChannel fc = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int) fc.size());
            fc.read(buffer);
            buffer.flip();
            buffer.order(ByteOrder.BIG_ENDIAN);
            filetype = (0xFF & buffer.get());
            lastupdate = new int[3];
            for (i = 0; i < 3; i++) {
                lastupdate[i] = (0xFF & buffer.get());
            }
            numberofrecords = (0xFF & buffer.get()) + 256 * ((0xFF & buffer.get()) + 256 * ((0xFF & buffer.get()) + 256 * (0xFF & buffer.get())));
            recordsoffset = (0xFF & buffer.get()) + 256 * (0xFF & buffer.get());
            recordlength = (0xFF & buffer.get()) + 256 * (0xFF & buffer.get());
            for (i = 0; i < 16; i++) {
                buffer.get();
            }
            tableflags = (0xFF & buffer.get());
            buffer.get();
            buffer.get();
            buffer.get();
            fields = new ArrayList();
            byte nextfsr;
            while ((nextfsr = buffer.get()) != 0x0D) {
                fields.add(new DBFField(nextfsr, buffer));
            }
            fis.close();
            isvalid = true;
        } catch (Exception e) {
            System.out.println("loading dbfheader error: " + filename + ": " + e.toString());
            e.printStackTrace();
        }
    }
