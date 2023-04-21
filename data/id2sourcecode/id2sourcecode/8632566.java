    public static synchronized void addAlphabeticallyOrdered(Vector<Object> v, Object obj) {
        int size = v.size();
        int currentIteration = 0;
        int index, aux_index;
        int lowIndex = 0;
        int highIndex = size - 1;
        int maxNumberOfIterations = (int) MathExtension.log2(size);
        if (size == 0) {
            v.add(obj);
            return;
        }
        while ((lowIndex <= highIndex) && (currentIteration <= maxNumberOfIterations)) {
            index = (lowIndex + highIndex) / 2;
            if (v.get(index).toString().compareTo(obj.toString()) == 0) {
                v.add(index, obj);
                return;
            }
            if (v.get(index).toString().compareTo(obj.toString()) < 0) {
                aux_index = index + 1;
                if ((aux_index) >= size) {
                    v.add(v.size(), obj);
                    return;
                }
                if (v.get(aux_index).toString().compareTo(obj.toString()) > 0) {
                    v.add(aux_index, obj);
                    return;
                }
                lowIndex = aux_index;
            } else {
                if (v.get(index).toString().compareTo(obj.toString()) > 0) {
                    aux_index = index - 1;
                    if ((aux_index) < 0) {
                        v.add(0, obj);
                        return;
                    }
                    if (v.get(aux_index).toString().compareTo(obj.toString()) < 0) {
                        v.add(index, obj);
                        return;
                    }
                    highIndex = aux_index;
                }
            }
            currentIteration++;
        }
    }
