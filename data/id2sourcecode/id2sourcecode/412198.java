    int proc(InfBlocks s, ZStream z, int r) {
        int j;
        int[] t;
        int tindex;
        int e;
        int b = 0;
        int k = 0;
        int p = 0;
        int n;
        int q;
        int m;
        int f;
        p = z.next_in_index;
        n = z.avail_in;
        b = s.bitb;
        k = s.bitk;
        q = s.write;
        m = q < s.read ? s.read - q - 1 : s.end - q;
        while (true) {
            switch(mode) {
                case START:
                    if (m >= 258 && n >= 10) {
                        s.bitb = b;
                        s.bitk = k;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        s.write = q;
                        r = inflate_fast(lbits, dbits, ltree, ltree_index, dtree, dtree_index, s, z);
                        p = z.next_in_index;
                        n = z.avail_in;
                        b = s.bitb;
                        k = s.bitk;
                        q = s.write;
                        m = q < s.read ? s.read - q - 1 : s.end - q;
                        if (r != Z_OK) {
                            mode = r == Z_STREAM_END ? WASH : BADCODE;
                            break;
                        }
                    }
                    need = lbits;
                    tree = ltree;
                    tree_index = ltree_index;
                    mode = LEN;
                case LEN:
                    j = need;
                    while (k < (j)) {
                        if (n != 0) r = Z_OK; else {
                            s.bitb = b;
                            s.bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            s.write = q;
                            return s.inflate_flush(z, r);
                        }
                        n--;
                        b |= (z.next_in[p++] & 0xff) << k;
                        k += 8;
                    }
                    tindex = (tree_index + (b & inflate_mask[j])) * 3;
                    b >>>= (tree[tindex + 1]);
                    k -= (tree[tindex + 1]);
                    e = tree[tindex];
                    if (e == 0) {
                        lit = tree[tindex + 2];
                        mode = LIT;
                        break;
                    }
                    if ((e & 16) != 0) {
                        get = e & 15;
                        len = tree[tindex + 2];
                        mode = LENEXT;
                        break;
                    }
                    if ((e & 64) == 0) {
                        need = e;
                        tree_index = tindex / 3 + tree[tindex + 2];
                        break;
                    }
                    if ((e & 32) != 0) {
                        mode = WASH;
                        break;
                    }
                    mode = BADCODE;
                    z.msg = "invalid literal/length code";
                    r = Z_DATA_ERROR;
                    s.bitb = b;
                    s.bitk = k;
                    z.avail_in = n;
                    z.total_in += p - z.next_in_index;
                    z.next_in_index = p;
                    s.write = q;
                    return s.inflate_flush(z, r);
                case LENEXT:
                    j = get;
                    while (k < (j)) {
                        if (n != 0) r = Z_OK; else {
                            s.bitb = b;
                            s.bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            s.write = q;
                            return s.inflate_flush(z, r);
                        }
                        n--;
                        b |= (z.next_in[p++] & 0xff) << k;
                        k += 8;
                    }
                    len += (b & inflate_mask[j]);
                    b >>= j;
                    k -= j;
                    need = dbits;
                    tree = dtree;
                    tree_index = dtree_index;
                    mode = DIST;
                case DIST:
                    j = need;
                    while (k < (j)) {
                        if (n != 0) r = Z_OK; else {
                            s.bitb = b;
                            s.bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            s.write = q;
                            return s.inflate_flush(z, r);
                        }
                        n--;
                        b |= (z.next_in[p++] & 0xff) << k;
                        k += 8;
                    }
                    tindex = (tree_index + (b & inflate_mask[j])) * 3;
                    b >>= tree[tindex + 1];
                    k -= tree[tindex + 1];
                    e = (tree[tindex]);
                    if ((e & 16) != 0) {
                        get = e & 15;
                        dist = tree[tindex + 2];
                        mode = DISTEXT;
                        break;
                    }
                    if ((e & 64) == 0) {
                        need = e;
                        tree_index = tindex / 3 + tree[tindex + 2];
                        break;
                    }
                    mode = BADCODE;
                    z.msg = "invalid distance code";
                    r = Z_DATA_ERROR;
                    s.bitb = b;
                    s.bitk = k;
                    z.avail_in = n;
                    z.total_in += p - z.next_in_index;
                    z.next_in_index = p;
                    s.write = q;
                    return s.inflate_flush(z, r);
                case DISTEXT:
                    j = get;
                    while (k < (j)) {
                        if (n != 0) r = Z_OK; else {
                            s.bitb = b;
                            s.bitk = k;
                            z.avail_in = n;
                            z.total_in += p - z.next_in_index;
                            z.next_in_index = p;
                            s.write = q;
                            return s.inflate_flush(z, r);
                        }
                        n--;
                        b |= (z.next_in[p++] & 0xff) << k;
                        k += 8;
                    }
                    dist += (b & inflate_mask[j]);
                    b >>= j;
                    k -= j;
                    mode = COPY;
                case COPY:
                    f = q - dist;
                    while (f < 0) {
                        f += s.end;
                    }
                    while (len != 0) {
                        if (m == 0) {
                            if (q == s.end && s.read != 0) {
                                q = 0;
                                m = q < s.read ? s.read - q - 1 : s.end - q;
                            }
                            if (m == 0) {
                                s.write = q;
                                r = s.inflate_flush(z, r);
                                q = s.write;
                                m = q < s.read ? s.read - q - 1 : s.end - q;
                                if (q == s.end && s.read != 0) {
                                    q = 0;
                                    m = q < s.read ? s.read - q - 1 : s.end - q;
                                }
                                if (m == 0) {
                                    s.bitb = b;
                                    s.bitk = k;
                                    z.avail_in = n;
                                    z.total_in += p - z.next_in_index;
                                    z.next_in_index = p;
                                    s.write = q;
                                    return s.inflate_flush(z, r);
                                }
                            }
                        }
                        s.window[q++] = s.window[f++];
                        m--;
                        if (f == s.end) f = 0;
                        len--;
                    }
                    mode = START;
                    break;
                case LIT:
                    if (m == 0) {
                        if (q == s.end && s.read != 0) {
                            q = 0;
                            m = q < s.read ? s.read - q - 1 : s.end - q;
                        }
                        if (m == 0) {
                            s.write = q;
                            r = s.inflate_flush(z, r);
                            q = s.write;
                            m = q < s.read ? s.read - q - 1 : s.end - q;
                            if (q == s.end && s.read != 0) {
                                q = 0;
                                m = q < s.read ? s.read - q - 1 : s.end - q;
                            }
                            if (m == 0) {
                                s.bitb = b;
                                s.bitk = k;
                                z.avail_in = n;
                                z.total_in += p - z.next_in_index;
                                z.next_in_index = p;
                                s.write = q;
                                return s.inflate_flush(z, r);
                            }
                        }
                    }
                    r = Z_OK;
                    s.window[q++] = (byte) lit;
                    m--;
                    mode = START;
                    break;
                case WASH:
                    if (k > 7) {
                        k -= 8;
                        n++;
                        p--;
                    }
                    s.write = q;
                    r = s.inflate_flush(z, r);
                    q = s.write;
                    m = q < s.read ? s.read - q - 1 : s.end - q;
                    if (s.read != s.write) {
                        s.bitb = b;
                        s.bitk = k;
                        z.avail_in = n;
                        z.total_in += p - z.next_in_index;
                        z.next_in_index = p;
                        s.write = q;
                        return s.inflate_flush(z, r);
                    }
                    mode = END;
                case END:
                    r = Z_STREAM_END;
                    s.bitb = b;
                    s.bitk = k;
                    z.avail_in = n;
                    z.total_in += p - z.next_in_index;
                    z.next_in_index = p;
                    s.write = q;
                    return s.inflate_flush(z, r);
                case BADCODE:
                    r = Z_DATA_ERROR;
                    s.bitb = b;
                    s.bitk = k;
                    z.avail_in = n;
                    z.total_in += p - z.next_in_index;
                    z.next_in_index = p;
                    s.write = q;
                    return s.inflate_flush(z, r);
                default:
                    r = Z_STREAM_ERROR;
                    s.bitb = b;
                    s.bitk = k;
                    z.avail_in = n;
                    z.total_in += p - z.next_in_index;
                    z.next_in_index = p;
                    s.write = q;
                    return s.inflate_flush(z, r);
            }
        }
    }
