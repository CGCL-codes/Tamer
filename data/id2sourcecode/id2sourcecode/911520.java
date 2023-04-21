    protected void analyzePixels() {
        _prof.prof.cnt[495]++;
        _prof.prof.cnt[496]++;
        int len = pixels.length;
        _prof.prof.cnt[497]++;
        int nPix = len / 3;
        {
            _prof.prof.cnt[498]++;
            indexedPixels = new byte[nPix];
        }
        _prof.prof.cnt[499]++;
        NeuQuant nq = new NeuQuant(pixels, len, sample);
        {
            _prof.prof.cnt[500]++;
            colorTab = nq.process();
        }
        {
            _prof.prof.cnt[501]++;
            for (int i = 0; i < colorTab.length; i += 3) {
                _prof.prof.cnt[502]++;
                byte temp = colorTab[i];
                {
                    _prof.prof.cnt[503]++;
                    colorTab[i] = colorTab[i + 2];
                }
                {
                    _prof.prof.cnt[504]++;
                    colorTab[i + 2] = temp;
                }
                {
                    _prof.prof.cnt[505]++;
                    usedEntry[i / 3] = false;
                }
            }
        }
        _prof.prof.cnt[506]++;
        int k = 0;
        {
            _prof.prof.cnt[507]++;
            for (int i = 0; i < nPix; i++) {
                _prof.prof.cnt[508]++;
                int index = nq.map(pixels[k++] & 0xff, pixels[k++] & 0xff, pixels[k++] & 0xff);
                {
                    _prof.prof.cnt[509]++;
                    usedEntry[index] = true;
                }
                {
                    _prof.prof.cnt[510]++;
                    indexedPixels[i] = (byte) index;
                }
            }
        }
        {
            _prof.prof.cnt[511]++;
            pixels = null;
        }
        {
            _prof.prof.cnt[512]++;
            colorDepth = 8;
        }
        {
            _prof.prof.cnt[513]++;
            palSize = 7;
        }
        {
            _prof.prof.cnt[514]++;
            if (transparent != null) {
                {
                    _prof.prof.cnt[515]++;
                    transIndex = findClosest(transparent);
                }
            }
        }
    }
