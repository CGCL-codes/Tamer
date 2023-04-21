    private void writeDoubleData(Number[] data) throws IOException {
        bb.clear();
        DoubleBuffer db = bb.asDoubleBuffer();
        for (Number n : data) {
            db.put(n.doubleValue());
        }
        bb.position(bb.position() + (8 * data.length));
        bb.flip();
        fileWriter.getChannel().write(bb);
        cols++;
    }
