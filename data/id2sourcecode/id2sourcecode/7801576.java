    void writeDCT(OutputStream os, int op) throws IOException {
        if (!valid) throw new IOException("Can't write DCT, because an error happened at reading (" + getLocationName() + ")");
        int[] last_dc = new int[components_in_scan];
        int off;
        HuffEncoder encoder = new HuffEncoder(os);
        int restarts_to_go = restart_interval;
        switch(op) {
            case TRANSPOSE:
                for (int ix = 0; ix < dct_coefs[0].length; ix++) {
                    for (int iy = 0; iy < dct_coefs.length; iy++) {
                        off = 0;
                        if (restart_interval != 0 && restarts_to_go == 0) {
                            restarts_to_go = restart_interval;
                            if (_Ss == 0) {
                                for (int k = 0; k < last_dc.length; k++) last_dc[k] = 0;
                            }
                            encoder.restart();
                        }
                        for (int c = 0; c < components_in_scan; c++) {
                            encoder.setTables(ac_table[c], dc_table[c]);
                            for (int mx = 0; mx < V[c]; mx++) {
                                for (int my = 0; my < H[c]; my++) {
                                    last_dc[c] = encoder.encode(transposeDCT(dct_coefs[iy][ix][off + my * V[c] + mx]), last_dc[c]);
                                }
                            }
                            off += V[c] * H[c];
                        }
                        restarts_to_go--;
                    }
                }
                break;
            case ROT_90:
                for (int ix = 0; ix < dct_coefs[0].length; ix++) {
                    for (int iy = dct_coefs.length - 1; iy >= 0; iy--) {
                        off = 0;
                        if (restart_interval != 0 && restarts_to_go == 0) {
                            restarts_to_go = restart_interval;
                            if (_Ss == 0) {
                                for (int k = 0; k < last_dc.length; k++) last_dc[k] = 0;
                            }
                            encoder.restart();
                        }
                        for (int c = 0; c < components_in_scan; c++) {
                            encoder.setTables(ac_table[c], dc_table[c]);
                            for (int mx = 0; mx < V[c]; mx++) {
                                for (int my = H[c] - 1; my >= 0; my--) {
                                    last_dc[c] = encoder.encode(rotate90DCT(dct_coefs[iy][ix][off + my * V[c] + mx]), last_dc[c]);
                                }
                            }
                            off += V[c] * H[c];
                        }
                        restarts_to_go--;
                    }
                }
                break;
            case ROT_270:
                for (int ix = dct_coefs[0].length - 1; ix >= 0; ix--) {
                    for (int iy = 0; iy < dct_coefs.length; iy++) {
                        off = 0;
                        if (restart_interval != 0 && restarts_to_go == 0) {
                            restarts_to_go = restart_interval;
                            if (_Ss == 0) {
                                for (int k = 0; k < last_dc.length; k++) last_dc[k] = 0;
                            }
                            encoder.restart();
                        }
                        for (int c = 0; c < components_in_scan; c++) {
                            encoder.setTables(ac_table[c], dc_table[c]);
                            for (int mx = V[c] - 1; mx >= 0; mx--) {
                                for (int my = 0; my < H[c]; my++) {
                                    last_dc[c] = encoder.encode(rotate270DCT(dct_coefs[iy][ix][off + my * V[c] + mx]), last_dc[c]);
                                }
                            }
                            off += V[c] * H[c];
                        }
                        restarts_to_go--;
                    }
                }
                break;
            case TRANSVERSE:
                for (int ix = dct_coefs[0].length - 1; ix >= 0; ix--) {
                    for (int iy = dct_coefs.length - 1; iy >= 0; iy--) {
                        off = 0;
                        if (restart_interval != 0 && restarts_to_go == 0) {
                            restarts_to_go = restart_interval;
                            if (_Ss == 0) {
                                for (int k = 0; k < last_dc.length; k++) last_dc[k] = 0;
                            }
                            encoder.restart();
                        }
                        for (int c = 0; c < components_in_scan; c++) {
                            encoder.setTables(ac_table[c], dc_table[c]);
                            for (int mx = V[c] - 1; mx >= 0; mx--) {
                                for (int my = H[c] - 1; my >= 0; my--) {
                                    last_dc[c] = encoder.encode(transverseDCT(dct_coefs[iy][ix][off + my * V[c] + mx]), last_dc[c]);
                                }
                            }
                            off += V[c] * H[c];
                        }
                        restarts_to_go--;
                    }
                }
                break;
            case FLIP_H:
                for (int iy = 0; iy < dct_coefs.length; iy++) {
                    for (int ix = dct_coefs[iy].length - 1; ix >= 0; ix--) {
                        off = 0;
                        if (restart_interval != 0 && restarts_to_go == 0) {
                            restarts_to_go = restart_interval;
                            if (_Ss == 0) {
                                for (int k = 0; k < last_dc.length; k++) last_dc[k] = 0;
                            }
                            encoder.restart();
                        }
                        for (int c = 0; c < components_in_scan; c++) {
                            encoder.setTables(ac_table[c], dc_table[c]);
                            for (int my = 0; my < V[c]; my++) {
                                for (int mx = H[c] - 1; mx >= 0; mx--) {
                                    last_dc[c] = encoder.encode(flipHDct(dct_coefs[iy][ix][off + my * H[c] + mx]), last_dc[c]);
                                }
                            }
                            off += V[c] * H[c];
                        }
                        restarts_to_go--;
                    }
                }
                break;
            case FLIP_V:
                for (int iy = dct_coefs.length - 1; iy >= 0; iy--) {
                    for (int ix = 0; ix < dct_coefs[iy].length; ix++) {
                        off = 0;
                        if (restart_interval != 0 && restarts_to_go == 0) {
                            restarts_to_go = restart_interval;
                            if (_Ss == 0) {
                                for (int k = 0; k < last_dc.length; k++) last_dc[k] = 0;
                            }
                            encoder.restart();
                        }
                        for (int c = 0; c < components_in_scan; c++) {
                            encoder.setTables(ac_table[c], dc_table[c]);
                            for (int my = V[c] - 1; my >= 0; my--) {
                                for (int mx = 0; mx < H[c]; mx++) {
                                    last_dc[c] = encoder.encode(flipVDct(dct_coefs[iy][ix][off + my * H[c] + mx]), last_dc[c]);
                                }
                            }
                            off += V[c] * H[c];
                        }
                        restarts_to_go--;
                    }
                }
                break;
            case ROT_180:
                for (int iy = dct_coefs.length - 1; iy >= 0; iy--) {
                    for (int ix = dct_coefs[iy].length - 1; ix >= 0; ix--) {
                        off = 0;
                        if (restart_interval != 0 && restarts_to_go == 0) {
                            restarts_to_go = restart_interval;
                            if (_Ss == 0) {
                                for (int k = 0; k < last_dc.length; k++) last_dc[k] = 0;
                            }
                            encoder.restart();
                        }
                        for (int c = 0; c < components_in_scan; c++) {
                            encoder.setTables(ac_table[c], dc_table[c]);
                            for (int my = V[c] - 1; my >= 0; my--) {
                                for (int mx = H[c] - 1; mx >= 0; mx--) {
                                    last_dc[c] = encoder.encode(rotate180Dct(dct_coefs[iy][ix][off + my * H[c] + mx]), last_dc[c]);
                                }
                            }
                            off += V[c] * H[c];
                        }
                        restarts_to_go--;
                    }
                }
                break;
            case NONE:
            default:
                for (int iy = 0; iy < dct_coefs.length; iy++) {
                    for (int ix = 0; ix < dct_coefs[iy].length; ix++) {
                        off = 0;
                        if (restart_interval != 0 && restarts_to_go == 0) {
                            restarts_to_go = restart_interval;
                            if (_Ss == 0) {
                                for (int k = 0; k < last_dc.length; k++) last_dc[k] = 0;
                            }
                            encoder.restart();
                        }
                        for (int c = 0; c < components_in_scan; c++) {
                            encoder.setTables(ac_table[c], dc_table[c]);
                            for (int b = 0; b < V[c] * H[c]; b++) {
                                last_dc[c] = encoder.encode(dct_coefs[iy][ix][off], last_dc[c]);
                                off++;
                            }
                        }
                        restarts_to_go--;
                    }
                }
        }
        encoder.flush();
    }
