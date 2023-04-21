    public int compareTo(ICCGame G) {
        if (!this.getClass().equals(G.getClass())) return -1;
        switch(sortcode) {
            case SORT_BY_RATING:
                float rating1 = (G.wrating + G.brating) / 2;
                float rating2 = (wrating + brating) / 2;
                if (rating1 > rating2) return 1; else if (rating1 < rating2) return -1; else return 0;
            case SORT_BY_OBS:
                if (G.maxobs > maxobs) return 1; else if (G.maxobs < maxobs) return -1; else return 0;
            case SORT_BY_KIB:
                if (G.kiblist.size() > kiblist.size()) return 1; else if (G.kiblist.size() < kiblist.size()) return -1; else return 0;
            case SORT_BY_FLUX:
                if (G.flux > flux) return 1; else if (G.flux < flux) return -1; else return 0;
            default:
                return 0;
        }
    }
