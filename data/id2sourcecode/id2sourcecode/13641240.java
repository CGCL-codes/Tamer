        @SuppressWarnings("fallthrough")
        final int proc(final Inflater z, int r) throws IOException {
            int p = z.next_in_index;
            int n = z.avail_in;
            int b = this.bitb;
            int k = this.bitk;
            int q = this.write;
            int m = ((q < this.read) ? (this.read - q - 1) : (end - q));
            String error = null;
            final byte[] in = z.next_in;
            LOOP: while (true) {
                switch(this.mode) {
                    case TYPE:
                        {
                            while (k < 3) {
                                if (n == 0) break LOOP;
                                r = Z_OK;
                                n--;
                                b |= (in[p++] & 0xff) << k;
                                k += 8;
                            }
                            int t = b & 7;
                            last = t & 1;
                            switch(t >>> 1) {
                                case 0:
                                    b >>>= 3;
                                    k -= 3;
                                    t = k & 7;
                                    b >>>= t;
                                    k -= t;
                                    this.mode = LENS;
                                    continue LOOP;
                                case 1:
                                    codes.init(InfTree.fixed_bl, InfTree.fixed_bd, InfTree.fixed_tl, 0, InfTree.fixed_td, 0);
                                    b >>>= 3;
                                    k -= 3;
                                    this.mode = CODES;
                                    continue LOOP;
                                case 2:
                                    b >>>= 3;
                                    k -= 3;
                                    this.mode = TABLE;
                                    continue LOOP;
                                case 3:
                                    error = "Invalid block type";
                                    b >>>= 3;
                                    k -= 3;
                                    r = Z_DATA_ERROR;
                                    this.mode = BAD;
                                    break LOOP;
                            }
                        }
                        continue LOOP;
                    case LENS:
                        {
                            while (k < 32) {
                                if (n == 0) break LOOP;
                                r = Z_OK;
                                n--;
                                b |= (in[p++] & 0xff) << k;
                                k += 8;
                            }
                            if ((((~b) >>> 16) & 0xffff) != (b & 0xffff)) {
                                error = "Invalid stored block lengths";
                                r = Z_DATA_ERROR;
                                this.mode = BAD;
                                break LOOP;
                            }
                            this.left = (b & 0xffff);
                            b = 0;
                            k = 0;
                            this.mode = (this.left != 0) ? STORED : ((this.last != 0) ? DRY : TYPE);
                        }
                        continue LOOP;
                    case STORED:
                        {
                            if (n == 0) break LOOP;
                            if (m == 0) {
                                if ((q == this.end) && (this.read != 0)) {
                                    q = 0;
                                    m = (q < this.read ? (this.read - q - 1) : (this.end - q));
                                }
                                if (m == 0) {
                                    this.write = q;
                                    r = inflate_flush(z, r);
                                    q = this.write;
                                    m = ((q < this.read) ? (this.read - q - 1) : (this.end - q));
                                    if ((q == this.end) && (this.read != 0)) {
                                        q = 0;
                                        m = ((q < this.read) ? (this.read - q - 1) : (this.end - q));
                                    }
                                    if (m == 0) break LOOP;
                                }
                            }
                            r = Z_OK;
                            int t = this.left;
                            if (t > n) t = n;
                            if (t > m) t = m;
                            System.arraycopy(in, p, this.window, q, t);
                            p += t;
                            n -= t;
                            q += t;
                            m -= t;
                            if ((this.left -= t) == 0) this.mode = (this.last != 0) ? DRY : TYPE;
                        }
                        continue LOOP;
                    case TABLE:
                        {
                            while (k < 14) {
                                if (n == 0) break LOOP;
                                r = Z_OK;
                                n--;
                                b |= (in[p++] & 0xff) << k;
                                k += 8;
                            }
                            int t = b & 0x3fff;
                            this.table = t;
                            if ((t & 0x1f) > 29 || ((t >> 5) & 0x1f) > 29) {
                                error = "Too many length or distance symbols";
                                r = Z_DATA_ERROR;
                                this.mode = BAD;
                                break LOOP;
                            }
                            t = 258 + (t & 0x1f) + ((t >> 5) & 0x1f);
                            int[] blens = this.blens;
                            if ((blens != null) && (blens.length >= t)) Arrays.fill(blens, 0, t, 0); else {
                                blens = null;
                                this.blens = null;
                                this.blens = new int[t];
                            }
                            b >>>= 14;
                            k -= 14;
                            this.index = 0;
                            this.mode = BTREE;
                        }
                    case BTREE:
                        {
                            final int[] blens = this.blens;
                            int index = this.index;
                            while (index < 4 + (this.table >>> 10)) {
                                while (k < 3) {
                                    if (n == 0) {
                                        this.index = index;
                                        break LOOP;
                                    }
                                    r = Z_OK;
                                    n--;
                                    b |= (in[p++] & 0xff) << k;
                                    k += 8;
                                }
                                blens[BORDER[index++]] = b & 7;
                                b >>>= 3;
                                k -= 3;
                            }
                            while (index < 19) blens[BORDER[index++]] = 0;
                            this.bb[0] = 7;
                            final int t = inftree.inflate_trees_bits(blens, this.bb, this.tb, this.hufts);
                            if (t != Z_OK) {
                                r = t;
                                if (r != Z_DATA_ERROR) r = Z_DATA_ERROR; else {
                                    this.blens = null;
                                    this.mode = BAD;
                                }
                                this.index = index;
                                break LOOP;
                            }
                            this.index = 0;
                            this.mode = DTREE;
                        }
                    case DTREE:
                        {
                            final int[] blens = this.blens;
                            int index = this.index;
                            while (true) {
                                int t = this.table;
                                if (index >= 258 + (t & 0x1f) + ((t >> 5) & 0x1f)) break;
                                t = this.bb[0];
                                while (k < t) {
                                    if (n == 0) {
                                        this.index = index;
                                        break LOOP;
                                    }
                                    r = Z_OK;
                                    n--;
                                    b |= (in[p++] & 0xff) << k;
                                    k += 8;
                                }
                                final int tb = this.tb[0];
                                t = this.hufts[(tb + (b & INFLATE_MASK[t])) * 3 + 1];
                                int c = this.hufts[(tb + (b & INFLATE_MASK[t])) * 3 + 2];
                                if (c < 16) {
                                    b >>>= t;
                                    k -= t;
                                    blens[index++] = c;
                                } else {
                                    int i = (c == 18) ? 7 : (c - 14);
                                    int j = (c == 18) ? 11 : 3;
                                    while (k < (t + i)) {
                                        if (n == 0) {
                                            this.index = index;
                                            break LOOP;
                                        }
                                        r = Z_OK;
                                        n--;
                                        b |= (in[p++] & 0xff) << k;
                                        k += 8;
                                    }
                                    b >>>= t;
                                    k -= t;
                                    j += b & INFLATE_MASK[i];
                                    b >>>= i;
                                    k -= i;
                                    i = index;
                                    t = this.table;
                                    if ((i + j > 258 + (t & 0x1f) + ((t >> 5) & 0x1f)) || (c == 16 && i < 1)) {
                                        error = "Invalid bit length repeat";
                                        r = Z_DATA_ERROR;
                                        this.mode = BAD;
                                        this.index = index;
                                        break LOOP;
                                    }
                                    c = (c == 16) ? blens[i - 1] : 0;
                                    do blens[i++] = c; while (--j != 0);
                                    index = i;
                                }
                            }
                            this.index = index;
                            this.tb[0] = -1;
                            final int[] bl = new int[] { 9 };
                            final int[] bd = new int[] { 6 };
                            final int[] tl = new int[1];
                            final int[] td = new int[1];
                            int t = this.table;
                            t = this.inftree.inflate_trees_dynamic(257 + (t & 0x1f), 1 + ((t >> 5) & 0x1f), blens, bl, bd, tl, td, this.hufts);
                            if (t != Z_OK) {
                                if (t == Z_DATA_ERROR) {
                                    this.blens = null;
                                    this.mode = BAD;
                                }
                                r = t;
                                break LOOP;
                            }
                            codes.init(bl[0], bd[0], this.hufts, tl[0], this.hufts, td[0]);
                            this.mode = CODES;
                        }
                    case CODES:
                        this.bitb = b;
                        this.bitk = k;
                        this.write = q;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        if ((r = codes.proc(this, z, r)) != Z_STREAM_END) return inflate_flush(z, r);
                        r = Z_OK;
                        p = z.next_in_index;
                        n = z.avail_in;
                        b = this.bitb;
                        k = this.bitk;
                        q = this.write;
                        m = ((q < this.read) ? (this.read - q - 1) : (this.end - q));
                        if (this.last == 0) {
                            this.mode = TYPE;
                            continue LOOP;
                        }
                        mode = DRY;
                    case DRY:
                        this.write = q;
                        r = inflate_flush(z, r);
                        q = this.write;
                        m = ((q < this.read) ? (this.read - q - 1) : (this.end - q));
                        if (this.read != this.write) break LOOP;
                        this.mode = DONE;
                    case DONE:
                        r = Z_STREAM_END;
                        break LOOP;
                    case BAD:
                        r = Z_DATA_ERROR;
                        break LOOP;
                    default:
                        r = Z_STREAM_ERROR;
                        break LOOP;
                }
            }
            this.bitb = b;
            this.bitk = k;
            this.write = q;
            z.avail_in = n;
            z.total_in += p - z.next_in_index;
            z.next_in_index = p;
            r = inflate_flush(z, r);
            if (r <= Z_ERRNO) {
                if (r == Z_DATA_ERROR) throw new InflaterException((error == null) ? "data error" : error);
                if (r != Z_BUF_ERROR) throw new InflaterException("stream error (code " + r + ")");
            }
            return r;
        }
