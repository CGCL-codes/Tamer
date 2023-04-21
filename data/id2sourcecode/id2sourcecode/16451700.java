    public static String getBestMove(String movesList) {
        if (movesList.length() > MAX_VARIANTS_LENGTH) {
            return "";
        }
        int i = 0;
        while (i < size && (!variantes[i].startsWith(movesList) || variantes[i].equals(movesList))) {
            i++;
        }
        if (i >= size) {
            return "";
        }
        int j = i;
        while (j < size && variantes[j].startsWith(movesList)) j++;
        int choice = i + rdm.nextInt(j - i);
        int lengthMoves = movesList.length();
        return variantes[choice].substring(lengthMoves, lengthMoves + 4);
    }
