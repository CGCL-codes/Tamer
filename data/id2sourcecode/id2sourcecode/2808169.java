        public synchronized void addAlphabeticallyOrdered(Vector<Object> v, Object obj, Comparator<Object> comp) {
            int size = v.size();
            int currentIteration = 0;
            int index, aux_index;
            int lowIndex = 0;
            int highIndex = size - 1;
            int maxNumberOfIterations = (int) log2(size);
            if (size == 0) {
                v.add(obj);
                return;
            }
            while ((lowIndex <= highIndex) && (currentIteration <= maxNumberOfIterations)) {
                index = (lowIndex + highIndex) / 2;
                if (comp.compare(v.get(index), obj) == 0) {
                    v.add(index, obj);
                    return;
                }
                if (comp.compare(v.get(index), obj) < 0) {
                    aux_index = index + 1;
                    if ((aux_index) >= size) {
                        v.add(v.size(), obj);
                        return;
                    }
                    if (comp.compare(v.get(aux_index), obj) > 0) {
                        v.add(aux_index, obj);
                        return;
                    }
                    lowIndex = aux_index;
                } else {
                    if (comp.compare(v.get(index), obj) > 0) {
                        aux_index = index - 1;
                        if ((aux_index) < 0) {
                            v.add(0, obj);
                            return;
                        }
                        if (comp.compare(v.get(aux_index), obj) < 0) {
                            v.add(index, obj);
                            return;
                        }
                        highIndex = aux_index;
                    }
                }
                currentIteration++;
            }
        }
