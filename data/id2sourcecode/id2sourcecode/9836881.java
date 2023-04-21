    public void add(int lower, int upper, int content) {
        int index = (lower + upper) / 2;
        if (content > vec.get(index)) {
            if (index + 1 >= vec.size() || vec.get(index + 1) > content) {
                vec.add(index + 1, content);
            } else {
                add(index + 1, upper, content);
            }
        } else {
            if (index == 0 || (vec.get(index - 1) < content)) {
                vec.add(index, content);
            } else {
                add(lower, index - 1, content);
            }
        }
    }
