    protected void blockUpdate() {
        tiger.reset();
        tiger.update((byte) 0);
        tiger.update(buffer, 0, bufferOffset);
        if ((bufferOffset == 0) && (nodes.size() > 0)) return;
        Object dig = (Object) tiger.digest();
        nodes.addElement(dig);
        count++;
        if (levelsLeft == -1) {
            byte[] digst = (byte[]) dig;
            System.arraycopy(digst, 0, serialization, serializationLeavesOffset, digst.length);
            serializationLeavesOffset += digst.length;
        }
    }
