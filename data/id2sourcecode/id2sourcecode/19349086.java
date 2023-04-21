    private void sortTable(Vector table, Vector tempMapping, int[] mapping, int start, int end) {
        System.out.print("Sorting (" + start + ", " + end + ")...\r");
        if (start + 1 >= end) return; else if (start + 10 >= end) {
            for (int i = start + 1; i < end; i++) {
                int[] row = (int[]) table.elementAt(i);
                Integer tempMap = (Integer) tempMapping.elementAt(i);
                int j;
                for (j = i - 1; j >= start; j--) {
                    if (compareRows((int[]) table.elementAt(j), row, mapping) > 0) {
                        table.setElementAt((int[]) table.elementAt(j), j + 1);
                        tempMapping.setElementAt((Integer) tempMapping.elementAt(j), j + 1);
                    } else {
                        table.setElementAt(row, j + 1);
                        tempMapping.setElementAt(tempMap, j + 1);
                        break;
                    }
                }
                if (j < start) {
                    table.setElementAt(row, start);
                    tempMapping.setElementAt(tempMap, start);
                }
            }
        } else {
            int boundaryPos = (start + end) / 2;
            int i;
            boolean allTheSame = true;
            int firstDifferent = 0;
            do {
                int[] boundary = (int[]) table.elementAt(boundaryPos);
                i = start;
                int j = end - 1;
                int[] row = null;
                byte compResult;
                while (i < j) {
                    row = (int[]) table.elementAt(i);
                    while (i <= j && compareRows(row, boundary, mapping) < 0) {
                        i++;
                        row = (int[]) table.elementAt(i);
                    }
                    row = (int[]) table.elementAt(j);
                    compResult = compareRows(row, boundary, mapping);
                    while (i <= j && (compResult >= 0)) {
                        if (compResult != 0) {
                            allTheSame = false;
                            firstDifferent = j;
                        }
                        j--;
                        row = (int[]) table.elementAt(j);
                        compResult = compareRows(row, boundary, mapping);
                    }
                    if (i <= j) {
                        row = (int[]) table.elementAt(j);
                        table.setElementAt(table.elementAt(i), j);
                        table.setElementAt(row, i);
                        Object temp = tempMapping.elementAt(j);
                        tempMapping.setElementAt(tempMapping.elementAt(i), j);
                        tempMapping.setElementAt(temp, i);
                    }
                }
                if (i <= start) {
                    if (allTheSame) return; else boundaryPos = firstDifferent;
                }
            } while (i <= start);
            sortTable(table, tempMapping, mapping, start, i);
            sortTable(table, tempMapping, mapping, i, end);
        }
    }
