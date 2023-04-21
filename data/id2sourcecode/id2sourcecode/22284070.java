    protected boolean handleChunk(int key, byte[] buf, int st, int len) throws IOException {
        switch(key) {
            case bKGDChunk:
                Color c = null;
                switch(colorType) {
                    case COLOR:
                    case COLOR | ALPHA:
                        verify(len == 6);
                        c = new Color(buf[st] & 0xff, buf[st + 2] & 0xff, buf[st + 4] & 0xff);
                        break;
                    case COLOR | PALETTE:
                    case COLOR | PALETTE | ALPHA:
                        verify(len == 1);
                        int ix = buf[st] & 0xFF;
                        verify(red_map != null && ix < red_map.length);
                        c = new Color(red_map[ix] & 0xff, green_map[ix] & 0xff, blue_map[ix] & 0xff);
                        break;
                    case GRAY:
                    case GRAY | ALPHA:
                        verify(len == 2);
                        int t = buf[st] & 0xFF;
                        c = new Color(t, t, t);
                        break;
                }
                if (c != null) property("background", c);
                break;
            case cHRMChunk:
                property("chromaticities", new Chromaticities(getInt(st), getInt(st + 4), getInt(st + 8), getInt(st + 12), getInt(st + 16), getInt(st + 20), getInt(st + 24), getInt(st + 28)));
                break;
            case gAMAChunk:
                if (len != 4) throw new PNGException("bogus gAMA");
                gamma = getInt(st);
                if (gamma != 100000) property("gamma", gamma / 100000.0f);
                break;
            case hISTChunk:
                break;
            case IDATChunk:
                return false;
            case IENDChunk:
                break;
            case IHDRChunk:
                if (len != 13 || (width = getInt(st)) == 0 || (height = getInt(st + 4)) == 0) throw new PNGException("bogus IHDR");
                bitDepth = getByte(st + 8);
                colorType = getByte(st + 9);
                compressionMethod = getByte(st + 10);
                filterMethod = getByte(st + 11);
                interlaceMethod = getByte(st + 12);
                break;
            case PLTEChunk:
                {
                    int tsize = len / 3;
                    red_map = new byte[tsize];
                    green_map = new byte[tsize];
                    blue_map = new byte[tsize];
                    for (int i = 0, j = st; i < tsize; i++, j += 3) {
                        red_map[i] = buf[j];
                        green_map[i] = buf[j + 1];
                        blue_map[i] = buf[j + 2];
                    }
                }
                break;
            case pHYsChunk:
                break;
            case sBITChunk:
                break;
            case tEXtChunk:
                int klen = 0;
                while (klen < len && buf[st + klen] != 0) klen++;
                if (klen < len) {
                    String tkey = new String(buf, st, klen);
                    String tvalue = new String(buf, st + klen + 1, len - klen - 1);
                    property(tkey, tvalue);
                }
                break;
            case tIMEChunk:
                property("modtime", new GregorianCalendar(getShort(st + 0), getByte(st + 2) - 1, getByte(st + 3), getByte(st + 4), getByte(st + 5), getByte(st + 6)).getTime());
                break;
            case tRNSChunk:
                switch(colorType) {
                    case PALETTE | COLOR:
                    case PALETTE | COLOR | ALPHA:
                        int alen = len;
                        if (red_map != null) alen = red_map.length;
                        alpha_map = new byte[alen];
                        System.arraycopy(buf, st, alpha_map, 0, len < alen ? len : alen);
                        while (--alen >= len) alpha_map[alen] = (byte) 0xFF;
                        break;
                    case COLOR:
                    case COLOR | ALPHA:
                        verify(len == 6);
                        transparentPixel = ((buf[st + 0] & 0xFF) << 16) | ((buf[st + 2] & 0xFF) << 8) | ((buf[st + 4] & 0xFF));
                        break;
                    case GRAY:
                    case GRAY | ALPHA:
                        verify(len == 2);
                        int t = buf[st] & 0xFF;
                        transparentPixel = (t << 16) | (t << 8) | t;
                        break;
                }
                break;
            case zTXtChunk:
                break;
        }
        return true;
    }
