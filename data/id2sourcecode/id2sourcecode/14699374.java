    private int initialID(int[] start_p, int[] lineage_ct_p) {
        println("initialID called: " + start_p[0] + CS + lineage_ct_p[0]);
        int rtn = 0;
        int lin_ct = lineage_ct_p[0];
        int first_four = -1, last_four = -1, four_cells;
        Vector nuclei = (Vector) nuclei_record.elementAt(0);
        int nuc_ct = nuclei.size();
        int cell_ct = countCells(nuclei);
        if (cell_ct <= 6) {
            polarBodies();
            cell_ct = countCells(nuclei);
        }
        if (cell_ct > 4) {
            Nucleus nucleij = null;
            for (int j = 0; j < nuc_ct; j++) {
                nucleij = (Nucleus) nuclei.elementAt(j);
                if (nucleij.status == -1) continue;
                if (nucleij.identity.indexOf(POLAR) > -1) continue;
                nucleij.identity = NUC + iNucCount++;
            }
            iParameters.axis = 0;
            start_p[0] = 0;
            lineage_ct_p[0] = lin_ct;
            System.out.println("Starting with more than 4 cells.  No canonical ID assigned.");
            return 0;
        } else {
            iParameters.axis = 1;
            if (cell_ct == 4) first_four = 0;
            for (int i = 0; i < iEndingIndex - 1; i++) {
                nuclei = (Vector) nuclei_record.elementAt(i);
                nuc_ct = nuclei.size();
                cell_ct = countCells(nuclei);
                if (cell_ct > 4) break;
                if (cell_ct == 4) {
                    if (first_four < 0) first_four = i;
                    last_four = i;
                }
            }
            if (first_four == -1) {
                nuclei = (Vector) nuclei_record.elementAt(0);
                nuc_ct = nuclei.size();
                Nucleus nucleij = null;
                for (int j = 0; j < nuc_ct; j++) {
                    nucleij = (Nucleus) nuclei.elementAt(j);
                    if (nucleij.status == -1) continue;
                    if (nucleij.identity.indexOf(POLAR) > -1) continue;
                    lin_ct++;
                    nucleij.identity = NUC + iNucCount++;
                }
                iParameters.axis = 0;
                start_p[0] = 0;
                lineage_ct_p[0] = lin_ct;
                System.out.println("Movie too short to see four cells");
                return 0;
            }
        }
        four_cells = (first_four + last_four) / 2;
        start_p[0] = four_cells + 1;
        rtn = fourCellID(four_cells, lineage_ct_p);
        if (rtn != 0) rtn = backAssignment(four_cells, lineage_ct_p);
        if (rtn == 0) {
            iParameters.axis = 0;
            return 1;
        }
        return 0;
    }
