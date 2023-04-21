    @Override
    public boolean addd(Comparable o) {
        int low = 0;
        int high = size() - 1;
        int mid, comparison;
        if (size() == 0) {
            return super.add(o);
        }
        if (convert(get(size() - 1)).compareTo(convert(o)) == 0) {
            mid = size() - 1;
            String s = convert(o);
            double value = Double.parseDouble(((String) o).substring(((String) o).lastIndexOf("?") + 1)) + Double.parseDouble(((String) get(mid)).substring(((String) (get(mid))).lastIndexOf("?") + 1));
            set(mid, convert(o) + "?" + value);
            return true;
        }
        while (low <= high) {
            mid = (low + high) / 2;
            comparison = convert(get(mid)).compareTo(convert(o));
            if (comparison > 0) {
                high = mid - 1;
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                String s = convert(o);
                double value = Double.parseDouble(((String) o).substring(((String) o).lastIndexOf("?") + 1)) + Double.parseDouble(((String) get(mid)).substring(((String) (get(mid))).lastIndexOf("?") + 1));
                set(mid, convert(o) + "?" + value);
                return true;
            }
        }
        if (low == high) {
            return insert(low, o);
        } else {
            return insert(high + 1, o);
        }
    }
