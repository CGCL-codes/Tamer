    public E next() {
        if (cursor < 1) {
            cursor = size - 1;
            return (E) data[0];
        }
        int grab = gen.nextInt(cursor + 1);
        E temp = (E) data[grab];
        data[grab] = data[cursor];
        data[cursor] = temp;
        cursor--;
        return temp;
    }
