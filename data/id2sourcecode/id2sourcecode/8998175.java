    @Override
    public int getColumnCount() {
        int col = 2;
        if (readReference != null && readReference.getData_timed() != null && writeReference != null && writeReference.getData_timed() != null) {
            if (readReference.getData_timed().length > 0 && writeReference.getData_timed().length > 0) {
                if (readReference.getData_timed()[0].value != null && readReference.getData_timed()[0].value.length > 0 && writeReference.getData_timed()[0].value != null && writeReference.getData_timed()[0].value.length > 0) {
                    col = 3;
                }
            }
        }
        return col;
    }
