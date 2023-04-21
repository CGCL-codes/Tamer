    private void mergeSort(int[] indices, int[] workspace, final int start, final int end) {
        final int numElem = end - start;
        if (numElem > 1) {
            final int mid = (start + end) / 2;
            mergeSort(indices, workspace, start, mid);
            mergeSort(indices, workspace, mid, end);
            int i = start;
            int j = start;
            int k = mid;
            while ((j < mid) && (k < end)) {
                if (compare(j, k) <= 0) {
                    workspace[i++] = indices[j++];
                } else {
                    workspace[i++] = indices[k++];
                }
            }
            if (j < mid) {
                final int numLeft = mid - j;
                System.arraycopy(indices, j, indices, end - numLeft, numLeft);
                System.arraycopy(workspace, start, indices, start, numElem - numLeft);
            } else {
                final int numLeft = end - k;
                System.arraycopy(workspace, start, indices, start, numElem - numLeft);
            }
        }
    }
