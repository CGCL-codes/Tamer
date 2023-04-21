        @SuppressWarnings("fallthrough")
        final int proc(final InfBlocks s, final Inflater z, int r) throws IOException {
            int p = z.next_in_index;
            int n = z.avail_in;
            int b = s.bitb;
            int k = s.bitk;
            int q = s.write;
            int m = (q < s.read) ? (s.read - q - 1) : (s.end - q);
            final byte[] in = z.next_in;
            LOOP: while (true) {
                switch(this.mode) {
                    case START:
                        if ((m >= 258) && (n >= 10)) {
                            s.bitb = b;
                            s.bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            s.write = q;
                            r = inflate_fast(this.lbits, this.dbits, this.ltree, this.ltree_index, this.dtree, this.dtree_index, s, z);
                            p = z.next_in_index;
                            n = z.avail_in;
                            b = s.bitb;
                            k = s.bitk;
                            q = s.write;
                            m = (q < s.read) ? (s.read - q - 1) : (s.end - q);
                            if (r != Z_OK) {
                                mode = (r == Z_STREAM_END) ? WASH : BADCODE;
                                continue LOOP;
                            }
                        }
                        this.need = lbits;
                        this.tree = ltree;
                        this.tree_index = ltree_index;
                        this.mode = LEN;
                    case LEN:
                        {
                            final int j = this.need;
                            while (k < j) {
                                if (n == 0) break LOOP;
                                r = Z_OK;
                                n--;
                                b |= (in[p++] & 0xff) << k;
                                k += 8;
                            }
                            final int[] tree = this.tree;
                            final int tindex = (this.tree_index + (b & INFLATE_MASK[j])) * 3;
                            b >>>= tree[tindex + 1];
                            k -= tree[tindex + 1];
                            final int e = tree[tindex];
                            if (e == 0) {
                                this.lit = tree[tindex + 2];
                                this.mode = LIT;
                                continue LOOP;
                            } else if ((e & 16) != 0) {
                                this.get = e & 15;
                                this.len = tree[tindex + 2];
                                this.mode = LENEXT;
                                continue LOOP;
                            } else if ((e & 64) == 0) {
                                this.need = e;
                                this.tree_index = (tindex / 3) + tree[tindex + 2];
                                continue LOOP;
                            } else if ((e & 32) != 0) {
                                this.mode = WASH;
                                continue LOOP;
                            } else {
                                this.mode = BADCODE;
                                throw new InflaterException("Invalid literal/length code");
                            }
                        }
                    case LENEXT:
                        {
                            final int j = this.get;
                            while (k < j) {
                                if (n == 0) break LOOP;
                                r = Z_OK;
                                n--;
                                b |= (in[p++] & 0xff) << k;
                                k += 8;
                            }
                            this.len += (b & INFLATE_MASK[j]);
                            b >>= j;
                            k -= j;
                            this.need = dbits;
                            this.tree = dtree;
                            this.tree_index = dtree_index;
                            this.mode = DIST;
                        }
                    case DIST:
                        {
                            final int j = this.need;
                            while (k < j) {
                                if (n == 0) break LOOP;
                                r = Z_OK;
                                n--;
                                b |= (in[p++] & 0xff) << k;
                                k += 8;
                            }
                            final int[] tree = this.tree;
                            final int tindex = (this.tree_index + (b & INFLATE_MASK[j])) * 3;
                            b >>= tree[tindex + 1];
                            k -= tree[tindex + 1];
                            final int e = tree[tindex];
                            if ((e & 16) != 0) {
                                this.get = e & 15;
                                this.dist = tree[tindex + 2];
                                this.mode = DISTEXT;
                                continue LOOP;
                            } else if ((e & 64) == 0) {
                                this.need = e;
                                this.tree_index = (tindex / 3) + tree[tindex + 2];
                                continue LOOP;
                            } else {
                                this.mode = BADCODE;
                                throw new InflaterException("Invalid distance code");
                            }
                        }
                    case DISTEXT:
                        {
                            final int j = this.get;
                            while (k < j) {
                                if (n == 0) break LOOP;
                                r = Z_OK;
                                n--;
                                b |= (in[p++] & 0xff) << k;
                                k += 8;
                            }
                            this.dist += (b & INFLATE_MASK[j]);
                            b >>= j;
                            k -= j;
                            this.mode = COPY;
                        }
                    case COPY:
                        {
                            int f = q - this.dist;
                            while (f < 0) f += s.end;
                            while (this.len != 0) {
                                if (m == 0) {
                                    if ((q == s.end) && (s.read != 0)) {
                                        q = 0;
                                        m = (s.read > 0) ? (s.read - 1) : s.end;
                                    }
                                    if (m == 0) {
                                        s.write = q;
                                        r = s.inflate_flush(z, r);
                                        q = s.write;
                                        m = (q < s.read) ? (s.read - q - 1) : (s.end - q);
                                        if ((q == s.end) && (s.read != 0)) {
                                            q = 0;
                                            m = (s.read > 0) ? (s.read - 1) : s.end;
                                        }
                                        if (m == 0) break LOOP;
                                    }
                                }
                                s.window[q++] = s.window[f++];
                                m--;
                                if (f == s.end) f = 0;
                                this.len--;
                            }
                            this.mode = START;
                        }
                        continue LOOP;
                    case LIT:
                        if (m == 0) {
                            if ((q == s.end) && (s.read != 0)) {
                                q = 0;
                                m = (s.read > 0) ? (s.read - 1) : s.end;
                            }
                            if (m == 0) {
                                s.write = q;
                                r = s.inflate_flush(z, r);
                                q = s.write;
                                m = (q < s.read) ? (s.read - q - 1) : (s.end - q);
                                if ((q == s.end) && (s.read != 0)) {
                                    q = 0;
                                    m = (s.read > 0) ? (s.read - 1) : s.end;
                                }
                                if (m == 0) break LOOP;
                            }
                        }
                        r = Z_OK;
                        s.window[q++] = (byte) this.lit;
                        m--;
                        this.mode = START;
                        continue LOOP;
                    case WASH:
                        if (k > 7) {
                            k -= 8;
                            n++;
                            p--;
                        }
                        s.write = q;
                        r = s.inflate_flush(z, r);
                        q = s.write;
                        m = (q < s.read) ? (s.read - q - 1) : (s.end - q);
                        if (s.read != s.write) break LOOP;
                        this.mode = END;
                    case END:
                        r = Z_STREAM_END;
                        break LOOP;
                    default:
                        s.bitb = b;
                        s.bitk = k;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        s.write = q;
                        if (this.mode == BADCODE) {
                            s.inflate_flush(z, Z_DATA_ERROR);
                            throw new InflaterException("Data error");
                        } else {
                            s.inflate_flush(z, Z_STREAM_ERROR);
                            throw new InflaterException("Malformed input");
                        }
                }
            }
            s.bitb = b;
            s.bitk = k;
            z.avail_in = n;
            z.total_in += p - z.next_in_index;
            z.next_in_index = p;
            s.write = q;
            return s.inflate_flush(z, r);
        }
