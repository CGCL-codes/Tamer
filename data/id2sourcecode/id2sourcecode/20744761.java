    int findInterval(double x) {
        int ilow, imid, i, n;
        n = xControl.length;
        ilow = 0;
        i = n - 1;
        while ((i - ilow) > 1) {
            imid = (i + ilow) / 2;
            if (x < xControl[imid]) {
                i = imid;
            } else {
                ilow = imid;
            }
        }
        return i;
    }
