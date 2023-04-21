    private static int[] overlap_search(Gene[] exons, Aberation ap, int ext_width) {
        int[] overlaps_found = null;
        ArrayList<Integer> o = new ArrayList<Integer>(0);
        int low = 0;
        int high = exons.length - 1;
        int lastMid = -1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Location el = exons[mid].getLocation();
            if (overlaps(el, ap.get_start(), ap.get_end(), ext_width)) {
                o.add(mid);
                int n = 1;
                while (mid - n >= 0 && exons[mid - n].getLocation().getStart() + max_size_backwards < ap.get_start()) {
                    if (overlaps(exons[mid - n].getLocation(), ap.get_start(), ap.get_end(), ext_width)) {
                        o.add(mid - n);
                    }
                    n++;
                }
                n = 1;
                while (mid + n <= high && exons[mid + n].getLocation().getStart() > ap.get_end()) {
                    if (overlaps(exons[mid + n].getLocation(), ap.get_start(), ap.get_end(), ext_width)) {
                        o.add(mid + n);
                    }
                    n++;
                }
                Collections.sort(o);
                overlaps_found = new int[o.size()];
                int z = 0;
                for (Integer x : o) {
                    overlaps_found[z] = x;
                    z++;
                }
                return overlaps_found;
            } else {
                if (el.getStart() < ap.get_start()) {
                    low = mid;
                } else {
                    high = mid;
                }
            }
            if (lastMid == mid) {
                break;
            }
            lastMid = mid;
        }
        return null;
    }
