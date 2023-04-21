    public Transition(State start, State end, String read, String write, String move) {
        this.start = start;
        this.end = end;
        this.read = read;
        this.write = write;
        if (move.equals(">")) this.move = 1; else if (move.equals("<")) this.move = -1; else if (move.equals("-")) this.move = 0;
    }
