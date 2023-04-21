    protected void blockConsume() {
        while (firstStack.size() > 1) {
            byte[] right = (byte[]) firstStack.pop();
            byte[] left = (byte[]) firstStack.pop();
            tiger.reset();
            tiger.update((byte) 1);
            tiger.update(left);
            tiger.update(right);
            secondStack.push(tiger.digest());
        }
    }
