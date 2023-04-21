    private void fixLinkages() {
        int i, j;
        for (i = 0; i < MAX_LINK && pano_id_link[i] != null; i++) {
            if (pano_id_link[i].equals(mPanoIdPrev)) {
                for (j = i; j < MAX_LINK; j++) {
                    pano_id_link[j] = pano_id_link[j + 1];
                }
                break;
            }
        }
        if (pano_id_link[0] == null) {
            pano_id_link[0] = mPanoIdPrev;
        }
        if (pano_id_link[1] == null) {
            pano_id_link[1] = pano_id_link[0];
        }
    }
