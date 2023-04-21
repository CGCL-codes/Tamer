    private void addBackEdge(int from, int to) {
        int[] oldEdges = backEdges[from];
        if (oldEdges == null) {
            backEdges[from] = new int[] { to };
        } else if (oldEdges[oldEdges.length - 1] < 0) {
            int left = 1;
            int right = oldEdges.length - 1;
            while (true) {
                if (right - left < 2) {
                    if (oldEdges[left] < 0) {
                        break;
                    } else {
                        if (oldEdges[right] >= 0) throw new Error("Failed binary search");
                        left = right;
                        break;
                    }
                } else {
                    int mid = (left + right) / 2;
                    if (oldEdges[mid] < 0) {
                        right = mid;
                    } else {
                        left = mid + 1;
                    }
                }
            }
            oldEdges[left] = to;
        } else {
            int[] newEdges = new int[oldEdges.length * 2];
            System.arraycopy(oldEdges, 0, newEdges, 0, oldEdges.length);
            newEdges[oldEdges.length] = to;
            for (int i = oldEdges.length + 1; i < newEdges.length; i++) {
                newEdges[i] = -1;
            }
            backEdges[from] = newEdges;
        }
    }
