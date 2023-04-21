    public static synchronized List<Object> doSearchConsideringCaseSensitive(String text, Vector<Object> sortOrderedItems) {
        int currentIteration = 0;
        int size = sortOrderedItems.size();
        int maxNumberOfIterations = (int) MathExtension.log2(size);
        int lowIndex = 0;
        int highIndex = sortOrderedItems.size() - 1;
        int mIndx;
        while ((lowIndex <= highIndex) && (currentIteration <= maxNumberOfIterations)) {
            mIndx = (lowIndex + highIndex) / 2;
            if (sortOrderedItems.get(mIndx).toString().startsWith(text)) {
                lowIndex = highIndex = mIndx;
                highIndex++;
                while ((highIndex < size) && (sortOrderedItems.get(highIndex).toString().startsWith(text))) {
                    highIndex++;
                }
                while (((lowIndex - 1) > -1) && (sortOrderedItems.get((lowIndex - 1)).toString().startsWith(text))) {
                    lowIndex--;
                }
                List<Object> list = new Vector<Object>(sortOrderedItems.subList(lowIndex, highIndex));
                lowIndex--;
                while ((lowIndex > -1) && (sortOrderedItems.get((lowIndex)).toString().toLowerCase().startsWith(text.toLowerCase()))) {
                    if (sortOrderedItems.get(lowIndex).toString().startsWith(text)) {
                        list.add(0, sortOrderedItems.get(lowIndex));
                    }
                    lowIndex--;
                }
                while ((highIndex < size) && (sortOrderedItems.get(highIndex).toString().toLowerCase().startsWith(text.toLowerCase()))) {
                    if (sortOrderedItems.get(highIndex).toString().startsWith(text)) {
                        list.add(list.size(), sortOrderedItems.get(highIndex));
                    }
                    highIndex++;
                }
                return list;
            } else {
                if (sortOrderedItems.get(mIndx).toString().compareTo(text) > 0) {
                    highIndex = mIndx - 1;
                } else {
                    lowIndex = mIndx + 1;
                }
            }
            currentIteration++;
        }
        return null;
    }
