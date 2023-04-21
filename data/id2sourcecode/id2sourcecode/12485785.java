    private static ScanResult process_horizontal_scan_minutia_V2(MINUTIAE minutiae, final int cx, final int cy, final int x2, final int feature_id, boolean[] bdata, final int iw, final int ih, int[] pdirection_map, int[] plow_flow_map, int[] phigh_curve_map, final LFSPARMS lfsparms) {
        int x_loc = (cx + x2) / 2;
        int x_edge = x_loc;
        int y_loc, y_edge;
        if (Matchpat.feature_patterns[feature_id].appearing == MinutiaApp.APPEARING) {
            y_loc = cy + 1;
            y_edge = cy;
        } else {
            y_loc = cy;
            y_edge = cy + 1;
        }
        int dmapval = pdirection_map[y_loc * iw + x_loc];
        int fmapval = plow_flow_map[y_loc * iw + x_loc];
        int cmapval = phigh_curve_map[y_loc * iw + x_loc];
        if (dmapval == Lfs.INVALID_DIR) return (ScanResult.IGNORE);
        int idir;
        if (cmapval != 0) {
            AdjustmentResult adjustmentResult;
            adjustmentResult = adjust_high_curvature_minutia_V2(x_loc, y_loc, x_edge, y_edge, bdata, iw, ih, plow_flow_map, minutiae, lfsparms);
            if (adjustmentResult.status == AdjustmentResult.Status.IGNORE) {
                return ScanResult.IGNORE;
            }
            x_loc = adjustmentResult.ox_loc;
            y_loc = adjustmentResult.oy_loc;
            x_edge = adjustmentResult.ox_edge;
            y_edge = adjustmentResult.oy_edge;
            idir = adjustmentResult.oidir;
        } else {
            idir = get_low_curvature_direction(RectilinearScanDirection.SCAN_HORIZONTAL, Matchpat.feature_patterns[feature_id].appearing, dmapval, lfsparms.num_directions);
        }
        double reliability;
        if (fmapval != 0) {
            reliability = Lfs.MEDIUM_RELIABILITY;
        } else reliability = Lfs.HIGH_RELIABILITY;
        MINUTIA minutia = new MINUTIA(x_loc, y_loc, x_edge, y_edge, idir, reliability, Matchpat.feature_patterns[feature_id].type, Matchpat.feature_patterns[feature_id].appearing, feature_id);
        UpdateResult updateResult = update_minutiae_V2(minutiae, minutia, RectilinearScanDirection.SCAN_HORIZONTAL, dmapval, bdata, iw, ih, lfsparms);
        if (updateResult == UpdateResult.UPDATE_IGNORE) {
        }
        return ScanResult.SUCCESS;
    }
