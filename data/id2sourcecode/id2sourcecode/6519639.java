    public String get(final String uri) throws RippleException {
        if (!upToDate) {
            update();
        }
        int fromIndex = 0, toIndex = fromUris.length - 1;
        int mid, cmp;
        int i = -1;
        while (fromIndex <= toIndex) {
            mid = (fromIndex + toIndex) / 2;
            cmp = uri.compareTo(fromUris[mid]);
            if (cmp > 0) {
                fromIndex = mid + 1;
                i = mid;
            } else if (cmp < 0) {
                toIndex = mid - 1;
            } else {
                return getPrivate(toUris[mid]);
            }
        }
        if (-1 < i && uri.startsWith(fromUris[i])) {
            return getPrivate(toUris[i] + uri.substring(fromUris[i].length()));
        } else {
            return getPrivate(uri);
        }
    }
