    private void push(byte[] data) {
        if (!nodes.isEmpty()) {
            for (int i = 0; i < nodes.size(); i++) {
                byte[] node = nodes.get(i);
                if (node == MARKER) {
                    nodes.set(i, data);
                    return;
                }
                tiger.reset();
                tiger.update((byte) 1);
                tiger.update(node);
                tiger.update(data);
                data = tiger.digest();
                nodes.set(i, MARKER);
            }
        }
        nodes.add(data);
    }
