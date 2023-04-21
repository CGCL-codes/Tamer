    public int transferFrom(ByteQueue buffer) {
        ensureBacked();
        final int toPut = Math.min(availableForPut(), buffer.availableForGet());
        if (toPut > 0) {
            if (writePosition + toPut < data.length) {
                buffer.get(data, writePosition, toPut);
            } else if (writePosition >= data.length) {
                buffer.get(data, writePosition - data.length, toPut);
            } else {
                final int half = data.length - writePosition;
                buffer.get(data, writePosition, half);
                buffer.get(data, 0, toPut - half);
            }
            writePosition += toPut;
        }
        return toPut;
    }
