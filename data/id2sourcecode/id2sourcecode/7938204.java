    public void calculateRoot(List leaves) {
        MessageDigest tt = new Tiger();
        Stack firstStack = new Stack();
        Stack secondStack = new Stack();
        ByteBuffer buffer;
        for (int leaf = leaves.size() - 1; leaf >= 0; leaf--) {
            buffer = (ByteBuffer) leaves.get(leaf);
            buffer.position(0);
            byte[] data = new byte[HashTree.HASH_SIZE];
            for (int i = 0; i < data.length; i++) {
                data[i] = buffer.get();
            }
            firstStack.push(data);
        }
        while (true) {
            while (firstStack.size() > 1) {
                byte[] left = (byte[]) firstStack.pop();
                byte[] right = (byte[]) firstStack.pop();
                tt.reset();
                tt.update(INTERNAL_HASH_PREFIX);
                tt.update(left, 0, left.length);
                tt.update(right, 0, right.length);
                secondStack.push(tt.digest());
            }
            if (!firstStack.empty()) {
                secondStack.push(firstStack.pop());
            }
            if (secondStack.size() == 1) {
                break;
            }
            while (!secondStack.empty()) {
                firstStack.push(secondStack.pop());
            }
        }
        digest = (byte[]) secondStack.pop();
        setRoot(ByteBuffer.wrap(digest));
    }
