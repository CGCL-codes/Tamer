    @Override
    protected int engineDigest(byte[] buf, int offset, int len) throws DigestException {
        if (len < HASHSIZE) {
            throw new DigestException();
        }
        blockUpdate();
        if (!firstStack.isEmpty()) {
            secondStack.push(firstStack.pop());
        }
        if (!secondStack.isEmpty()) {
            while (secondStack.size() > 1) {
                Stack tmpStack = new Stack();
                while (!secondStack.isEmpty()) {
                    tmpStack.push(secondStack.pop());
                }
                while (tmpStack.size() > 1) {
                    byte[] left = (byte[]) tmpStack.pop();
                    byte[] right = (byte[]) tmpStack.pop();
                    tiger.reset();
                    tiger.update((byte) 1);
                    tiger.update(left);
                    tiger.update(right);
                    secondStack.push(tiger.digest());
                }
                if (!tmpStack.isEmpty()) {
                    secondStack.push(tmpStack.pop());
                }
            }
        }
        if (!secondStack.isEmpty()) {
            System.arraycopy(secondStack.pop(), 0, buf, offset, HASHSIZE);
        }
        engineReset();
        return HASHSIZE;
    }
