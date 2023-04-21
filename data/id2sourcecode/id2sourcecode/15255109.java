    public final int removeAt(final int idx) {
        if (idx < 0 || idx > pos) {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        } else {
            final int result = data[idx];
            pos--;
            for (int n = idx; n <= pos; n++) data[n] = data[n + 1];
            return result;
        }
    }
