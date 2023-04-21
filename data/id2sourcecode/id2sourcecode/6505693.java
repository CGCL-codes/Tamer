    private void fast_pool_reseed() {
        byte[] v0 = fast_pool.digest();
        byte[] vi = v0;
        for (byte i = 0; i < Pt; i++) {
            reseed_ctx.update(vi, 0, vi.length);
            reseed_ctx.update(v0, 0, v0.length);
            reseed_ctx.update(i);
            vi = reseed_ctx.digest();
        }
        Util.makeKey(vi, tmp, 0, tmp.length);
        rekey(tmp);
        Util.wipe(v0);
        fast_entropy = 0;
        write_seed(seedfile);
    }
