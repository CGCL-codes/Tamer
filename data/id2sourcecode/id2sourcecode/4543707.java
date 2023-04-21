    private int getBuddyListPosition(Buddy object, int min, int max) {
        int pos = (min + max) / 2;
        int comp;
        if (max < min) {
            return 0;
        } else if (max == min) {
            comp = object.compareToByName((Buddy) buddys.elementAt(max));
            if (comp < 0) {
                return max;
            } else if (comp >= 0) {
                return max + 1;
            }
        } else {
            comp = object.compareToByName((Buddy) buddys.elementAt(pos));
            if (comp == 0) {
                pos++;
            } else if (comp < 0) {
                pos = this.getBuddyListPosition(object, min, pos - 1);
            } else {
                pos = this.getBuddyListPosition(object, pos + 1, max);
            }
        }
        return pos;
    }
