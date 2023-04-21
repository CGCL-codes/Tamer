    public Vector makeCorridor(int x, int y, byte direction, int max_length) throws Exception {
        Vector v = new Vector();
        int r;
        v.x = x;
        v.y = y;
        if (direction == Individual.EAST) {
            if (x + max_length > SIZE - 1) {
                max_length = SIZE - x - 1;
            }
        } else if (direction == Individual.SOUTH) {
            if (y + max_length > SIZE - 1) {
                max_length = SIZE - y - 1;
            }
        } else if (direction == Individual.WEST) {
            if (x - max_length < 0) {
                max_length = x - 1;
            }
        } else if (direction == Individual.NORTH) {
            if (y - max_length < 0) {
                max_length = y - 1;
            }
        }
        if (direction == Individual.EAST) {
            int count = 0;
            if (max_length > 2) r = 2 + rand.nextInt(max_length - 2); else r = max_length;
            do {
                v.x += 1;
                this.writeCase(EMPTY_FIELD, v.x, v.y);
                count++;
            } while (count < r);
        }
        if (direction == Individual.WEST) {
            int count = 0;
            if (max_length > 2) r = 2 + rand.nextInt(max_length - 2); else r = max_length;
            do {
                v.x -= 1;
                this.writeCase(EMPTY_FIELD, v.x, v.y);
                count++;
            } while (count < r);
        }
        if (direction == Individual.NORTH) {
            int count = 0;
            if (max_length > 2) r = 2 + rand.nextInt(max_length - 2); else r = max_length;
            do {
                v.y -= 1;
                this.writeCase(EMPTY_FIELD, v.x, v.y);
                count++;
            } while (count < r);
        }
        if (direction == Individual.SOUTH) {
            int count = 0;
            if (max_length > 2) r = 2 + rand.nextInt(max_length - 2); else r = max_length;
            do {
                v.y += 1;
                this.writeCase(EMPTY_FIELD, v.x, v.y);
                count++;
            } while (count < r);
        }
        if (v.y == SIZE - 2) {
            v.y += 1;
            this.writeCase(EMPTY_FIELD, v.x, v.y);
        } else if (v.x == SIZE - 2) {
            v.x += 1;
            this.writeCase(EMPTY_FIELD, v.x, v.y);
        } else if (v.y == 1) {
            v.y -= 1;
            this.writeCase(EMPTY_FIELD, v.x, v.y);
        } else if (v.x == 1) {
            v.x -= 1;
            this.writeCase(EMPTY_FIELD, v.x, v.y);
        }
        return v;
    }
