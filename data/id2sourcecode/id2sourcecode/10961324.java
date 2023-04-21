    public static synchronized List<Object> doSearchIgnoringCaseSensitive(String text, Vector<Object> sortOrderedItems) {
        int currentIteration = 0;
        int size = sortOrderedItems.size();
        int maxNumberOfIterations = (int) MathExtension.log2(size);
        int lowIndex = 0;
        int highIndex = sortOrderedItems.size() - 1;
        int mIndx;
        List<Object> list = null;
        List<Object> list2 = null;
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
                list = Arrays.asList((sortOrderedItems.subList(lowIndex, highIndex)).toArray());
                break;
            } else {
                if (sortOrderedItems.get(mIndx).toString().compareTo(text) > 0) {
                    highIndex = mIndx - 1;
                } else {
                    lowIndex = mIndx + 1;
                }
            }
            currentIteration++;
        }
        currentIteration = 0;
        lowIndex = 0;
        highIndex = sortOrderedItems.size() - 1;
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
                list2 = Arrays.asList((sortOrderedItems.subList(lowIndex, highIndex)).toArray());
                if (list == null) return list2; else {
                    if (list2 == null) return null;
                    Object obj;
                    int j;
                    list = new ArrayList<Object>(list.subList(0, list.size()));
                    for (int i = 0; i < list2.size(); i++) {
                        obj = list2.get(i);
                        if (!list.contains(obj)) {
                            for (j = 0; j < list.size(); j++) {
                                if (list.get(j).toString().compareTo(obj.toString()) > 0) break;
                            }
                            list.add(j, obj);
                        }
                    }
                    size = list.size();
                    if (size == 0) {
                        j = 0;
                    } else {
                        j = sortOrderedItems.indexOf(list.get(size - 1));
                    }
                    j++;
                    if (j < sortOrderedItems.size()) {
                        do {
                            obj = sortOrderedItems.get(j);
                            if (obj.toString().toLowerCase().startsWith(text.toLowerCase())) {
                                list.add(size, obj);
                            }
                            j++;
                        } while (j < sortOrderedItems.size());
                    }
                    return list;
                }
            } else {
                if (sortOrderedItems.get(mIndx).toString().toLowerCase().compareTo(text.toLowerCase()) > 0) {
                    highIndex = mIndx - 1;
                } else {
                    lowIndex = mIndx + 1;
                }
            }
            currentIteration++;
        }
        return null;
    }
