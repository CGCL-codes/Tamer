    @Override
    public TValue get(Object key) {
        PdfString keyString = (PdfString) key;
        PdfDictionary parent = getBaseDataObject();
        while (true) {
            PdfArray names = (PdfArray) parent.resolve(PdfName.Names);
            if (names == null) {
                PdfArray kids = (PdfArray) parent.resolve(PdfName.Kids);
                int low = 0, high = kids.size() - 1;
                while (true) {
                    if (low > high) return null;
                    int mid = (low + high) / 2;
                    PdfDictionary kid = (PdfDictionary) kids.resolve(mid);
                    PdfArray limits = (PdfArray) kid.resolve(PdfName.Limits);
                    int comparison = keyString.compareTo(limits.get(0));
                    if (comparison < 0) {
                        high = mid - 1;
                    } else {
                        comparison = keyString.compareTo(limits.get(1));
                        if (comparison > 0) {
                            low = mid + 1;
                        } else {
                            parent = kid;
                            break;
                        }
                    }
                }
            } else {
                int low = 0, high = names.size();
                while (true) {
                    if (low > high) return null;
                    int mid = (mid = ((low + high) / 2)) - (mid % 2);
                    int comparison = keyString.compareTo(names.get(mid));
                    if (comparison < 0) {
                        high = mid - 2;
                    } else if (comparison > 0) {
                        low = mid + 2;
                    } else {
                        return wrap(names.get(mid + 1), (PdfString) names.get(mid));
                    }
                }
            }
        }
    }
