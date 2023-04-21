    public JxTrace[] findTracesByZLevel(double z) {
        int zp0 = 0;
        int zp;
        int zp1 = this._zvals.length;
        double zv;
        int nz = this._zvals.length;
        do {
            zp = (zp1 + zp0) / 2;
            zv = this._zvals[zp].doubleValue();
            if (zv > z) zp1 = zp; else if (zv < z) zp0 = zp; else break;
        } while ((nz >>= 1) > 0);
        JxTrace[] traces;
        if (Math.abs(zv - z) < this._dzmin) {
            ArrayList<JxTrace> tl = this._trace_table.get(this._zvals[zp]);
            traces = tl.toArray(new JxTrace[tl.size()]);
        } else traces = new JxTrace[0];
        return traces;
    }
