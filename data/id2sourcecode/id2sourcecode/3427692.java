    public void copyStateTo(RrdUpdater other) throws IOException, RrdException {
        if (!(other instanceof Robin)) {
            throw new RrdException("Cannot copy Robin object to " + other.getClass().getName());
        }
        Robin robin = (Robin) other;
        int rowsDiff = rows - robin.rows;
        if (rowsDiff == 0) {
            robin.pointer.set(pointer.get());
            robin.values.writeBytes(values.readBytes());
        } else {
            for (int i = 0; i < robin.rows; i++) {
                int j = i + rowsDiff;
                robin.store(j >= 0 ? getValue(j) : Double.NaN);
            }
        }
    }
