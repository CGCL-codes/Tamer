    public synchronized void append(ArrayDataSet ds) {
        if (ds.rank() != this.rank) throw new IllegalArgumentException("rank mismatch");
        if (ds.len1 != this.len1) throw new IllegalArgumentException("len1 mismatch");
        if (ds.len2 != this.len2) throw new IllegalArgumentException("len2 mismatch");
        if (ds.len3 != this.len3) throw new IllegalArgumentException("len3 mismatch");
        if (this.getBack().getClass() != ds.getBack().getClass()) throw new IllegalArgumentException("backing type mismatch");
        int myLength = this.len0 * this.len1 * this.len2 * this.len3;
        int dsLength = ds.len0 * ds.len1 * ds.len2 * ds.len3;
        if (Array.getLength(this.getBack()) < myLength + dsLength) {
            throw new IllegalArgumentException("unable to append dataset, not enough room");
        }
        System.arraycopy(ds.getBack(), 0, this.getBack(), myLength, dsLength);
        Units u1 = SemanticOps.getUnits(this);
        Units u2 = SemanticOps.getUnits(ds);
        if (u1 != u2) {
            UnitsConverter uc = UnitsConverter.getConverter(u2, u1);
            Class backClass = this.getBack().getClass().getComponentType();
            for (int i = myLength; i < myLength + dsLength; i++) {
                Number nv = uc.convert(Array.getDouble(this.getBack(), i));
                if (backClass == double.class) {
                    Array.set(this.getBack(), i, nv.doubleValue());
                } else if (backClass == float.class) {
                    Array.set(this.getBack(), i, nv.floatValue());
                } else if (backClass == long.class) {
                    Array.set(this.getBack(), i, nv.longValue());
                } else if (backClass == int.class) {
                    Array.set(this.getBack(), i, nv.intValue());
                } else if (backClass == short.class) {
                    Array.set(this.getBack(), i, nv.shortValue());
                } else if (backClass == byte.class) {
                    Array.set(this.getBack(), i, nv.byteValue());
                } else {
                    throw new IllegalArgumentException("unsupported type: " + backClass);
                }
            }
        }
        this.len0 = this.len0 + ds.len0;
        properties.putAll(joinProperties(this, ds));
    }
