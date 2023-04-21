    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int random = StdRandom.uniform(N);
        Node n = (Node) array[random];
        Item item = n.item;
        Node prev = n.previous;
        Node next = n.next;
        if (prev == null) {
            if (next == null) {
                first = null;
                last = null;
                N--;
            } else {
                first = next;
                first.previous = null;
                N--;
            }
        } else {
            if (next == null) {
                last = n.previous;
                last.next = null;
                N--;
            } else {
                prev.next = n.next;
                next.previous = prev;
                N--;
            }
        }
        for (int i = random; i < N; i++) {
            array[i] = array[i + 1];
        }
        return item;
    }
