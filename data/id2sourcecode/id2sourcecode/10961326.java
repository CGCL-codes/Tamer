    public static synchronized List<Object> doSearchIgnoringCaseSensitive(String text, Vector<Object> sortOrderedItems, Comparator<Object> comp) {
        int currentIteration = 0;
        int size = sortOrderedItems.size();
        int maxNumberOfIterations = (int) MathExtension.log2(size);
        int lowIndex = 0;
        int highIndex = sortOrderedItems.size() - 1;
        int mIndx;
        while ((lowIndex <= highIndex) && (currentIteration <= maxNumberOfIterations)) {
            mIndx = (lowIndex + highIndex) / 2;
            if (sortOrderedItems.get(mIndx).toString().toLowerCase().startsWith(text.toLowerCase())) {
                lowIndex = highIndex = mIndx;
                highIndex++;
                while ((highIndex < size) && (sortOrderedItems.get(highIndex).toString().toLowerCase().startsWith(text.toLowerCase()))) {
                    highIndex++;
                }
                while (((lowIndex - 1) > -1) && (sortOrderedItems.get((lowIndex - 1)).toString().toLowerCase().startsWith(text.toLowerCase()))) {
                    lowIndex--;
                }
                return Arrays.asList((sortOrderedItems.subList(lowIndex, highIndex)).toArray());
            } else {
                if (comp.compare(sortOrderedItems.get(mIndx).toString().toLowerCase(), text.toLowerCase()) > 0) {
                    highIndex = mIndx - 1;
                } else {
                    lowIndex = mIndx + 1;
                }
            }
            currentIteration++;
        }
        return null;
    }
