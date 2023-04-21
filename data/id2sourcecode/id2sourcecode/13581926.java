    void removeInactiveEdges(int y) {
        int i = 0;
        while (i < activeEdges) {
            int index = aedge[i];
            if (y < ey1[index] || y >= ey2[index]) {
                for (int j = i; j < activeEdges - 1; j++) aedge[j] = aedge[j + 1];
                activeEdges--;
            } else i++;
        }
    }
