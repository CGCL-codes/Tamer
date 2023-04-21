    protected void blockUpdate() {
        tiger.reset();
        tiger.update((byte) 0);
        tiger.update(buffer, 0, bufferOffset);
        if (bufferOffset == 0 & firstStack.size() == 0) {
            return;
        }
        firstStack.push(tiger.digest());
        blockConsume();
    }
