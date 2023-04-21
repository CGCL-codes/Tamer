    private void removeIndex(final int position) {
        final int[] tmp = new int[indices.length - 1];
        System.arraycopy(indices, 0, tmp, 0, position);
        final int offset = indices[position] - indices[position + 1];
        for (int i = position; i < tmp.length; i++) {
            tmp[i] = indices[i + 1] + offset;
        }
        indices = tmp;
    }
