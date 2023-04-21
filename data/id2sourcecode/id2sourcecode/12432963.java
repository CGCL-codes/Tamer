    public Map coloring() {
        Map result;
        int lower = 1, upper = vertices.size(), middle;
        while (lower < upper) {
            middle = (lower + upper) / 2;
            result = null;
            try {
                result = coloring(middle);
            } catch (NotEnoughColorsException e) {
            }
            if (result != null) if (lower == middle) return result; else upper = middle; else if (lower == middle) try {
                return coloring(lower + 1);
            } catch (NotEnoughColorsException e) {
                return null;
            } else lower = middle;
        }
        return null;
    }
