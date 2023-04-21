    public final void VBR_encode_granule(final LameGlobalFlags gfp, GrInfo cod_info, final float[] l3_xmin, float xrpow[], final int ch, int min_bits, int max_bits) {
        final LameInternalFlags gfc = gfp.internal_flags;
        GrInfo bst_cod_info = new GrInfo();
        float bst_xrpow[] = new float[576];
        final int Max_bits = max_bits;
        int real_bits = max_bits + 1;
        int this_bits = (max_bits + min_bits) / 2;
        int dbits, over, found = 0;
        final boolean sfb21_extra = gfc.sfb21_extra;
        assert (Max_bits <= LameInternalFlags.MAX_BITS_PER_CHANNEL);
        Arrays.fill(bst_cod_info.l3_enc, 0);
        do {
            assert (this_bits >= min_bits);
            assert (this_bits <= max_bits);
            assert (min_bits <= max_bits);
            if (this_bits > Max_bits - 42) gfc.sfb21_extra = false; else gfc.sfb21_extra = sfb21_extra;
            over = outer_loop(gfp, cod_info, l3_xmin, xrpow, ch, this_bits);
            if (over <= 0) {
                found = 1;
                real_bits = cod_info.part2_3_length;
                bst_cod_info.assign(cod_info);
                System.arraycopy(xrpow, 0, bst_xrpow, 0, 576);
                max_bits = real_bits - 32;
                dbits = max_bits - min_bits;
                this_bits = (max_bits + min_bits) / 2;
            } else {
                min_bits = this_bits + 32;
                dbits = max_bits - min_bits;
                this_bits = (max_bits + min_bits) / 2;
                if (found != 0) {
                    found = 2;
                    cod_info.assign(bst_cod_info);
                    System.arraycopy(bst_xrpow, 0, xrpow, 0, 576);
                }
            }
        } while (dbits > 12);
        gfc.sfb21_extra = sfb21_extra;
        if (found == 2) {
            System.arraycopy(bst_cod_info.l3_enc, 0, cod_info.l3_enc, 0, 576);
        }
        assert (cod_info.part2_3_length <= Max_bits);
    }
