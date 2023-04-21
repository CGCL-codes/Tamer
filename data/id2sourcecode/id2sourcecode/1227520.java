    private void addOneMesh(double z00, double z01, double z10, double z11, double x0, double y0, double x1, double y1, double aThreshold) {
        int cellCase;
        cellCase = calcCellCase(z00, z01, z10, z11, aThreshold);
        switch(cellCase) {
            case 1:
            case 2:
            case 4:
            case 8:
            case 7:
            case 11:
            case 13:
            case 14:
                addMeshCornerLine(cellCase, z00, z01, z10, z11, x0, y0, x1, y1, aThreshold);
                break;
            case 3:
            case 5:
            case 10:
            case 12:
                addMeshCrossLine(cellCase, z00, z01, z10, z11, x0, y0, x1, y1, aThreshold);
                break;
            case 9:
            case 6:
                double z0m, zm0, zmm, zm1, z1m, xm, ym;
                z0m = (z00 + z01) / 2;
                zm0 = (z00 + z10) / 2;
                zmm = (z00 + z01 + z10 + z11) / 4;
                zm1 = (z01 + z11) / 2;
                z1m = (z10 + z11) / 2;
                xm = (x0 + x1) / 2;
                ym = (y0 + y1) / 2;
                addOneMesh(z00, z0m, zm0, zmm, x0, y0, xm, ym, aThreshold);
                addOneMesh(z0m, z01, zmm, zm1, xm, y0, x1, ym, aThreshold);
                addOneMesh(zm0, zmm, z10, z1m, x0, ym, xm, y1, aThreshold);
                addOneMesh(zmm, zm1, z1m, z11, xm, ym, x1, y1, aThreshold);
                break;
            case 0:
            case 15:
                break;
        }
    }
