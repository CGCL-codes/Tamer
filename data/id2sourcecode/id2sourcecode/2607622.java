    private void write(RandomAccessFile file) throws java.io.IOException {
        FileChannel fc = file.getChannel();
        ByteBuffer bb = ByteBuffer.allocate(1024);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        long structStart = fc.position() + 8;
        put(bb, magic);
        if (structName.length() <= 4) {
            bb.putShort((short) 0x1);
            bb.putShort((short) structName.length());
            bb.put(structName.getBytes());
            bb.position(bb.position() + 4 - structName.length());
        } else {
            bb.putInt(0x1);
            bb.putInt(structName.length());
            bb.put(structName.getBytes());
            int rem = bb.position() % 8;
            bb.position(bb.position() + 8 - rem);
        }
        put(bb, magic2);
        bb.putInt(0x20 * sFields.size());
        bb.flip();
        fc.write(bb);
        long fieldstart = fc.position();
        for (int ii = 0; ii < sFields.size(); ++ii) {
            field_t field = (field_t) sFields.get(ii);
            file.writeBytes(field.fieldName);
            fc.position(fieldstart += 0x20);
        }
        for (int ii = 0; ii < sFields.size(); ++ii) {
            field_t field = (field_t) sFields.get(ii);
            if (field.child != null) field.child.write(file); else writeField(file, bb, field);
        }
        long end = fc.position();
        int temp = (int) (fc.position() - structStart);
        fc.position(structStart - 4);
        bb.clear();
        bb.putInt(temp);
        bb.flip();
        fc.write(bb);
        fc.position(end);
    }
