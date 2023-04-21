    private Map<Method, List<Method>> computeRelatedMethodUseTfIdf(Map<Method, ReadWriteFields> methodAndReadWrites) {
        Map<Method, List<Method>> relatedMethodMap = new LinkedHashMap<Method, List<Method>>();
        Map<String, Integer> globalFrequence = new LinkedHashMap<String, Integer>();
        for (Method method : methodAndReadWrites.keySet()) {
            ReadWriteFields rwf = methodAndReadWrites.get(method);
            Map<String, Integer> readFields = rwf.readFields;
            Map<String, Integer> writeFields = rwf.writeFields;
            for (String read : readFields.keySet()) {
                if (!globalFrequence.containsKey(read)) {
                    globalFrequence.put(read, readFields.get(read));
                } else {
                    globalFrequence.put(read, readFields.get(read) + globalFrequence.get(read));
                }
            }
            for (String write : writeFields.keySet()) {
                if (!globalFrequence.containsKey(write)) {
                    globalFrequence.put(write, writeFields.get(write));
                } else {
                    globalFrequence.put(write, writeFields.get(write) + globalFrequence.get(write));
                }
            }
        }
        for (Method method : methodAndReadWrites.keySet()) {
            Set<Method> otherMethods = methodAndReadWrites.keySet();
            Map<Method, Float> relevanceMap = new LinkedHashMap<Method, Float>();
            List<Method> dependentMethods = new LinkedList<Method>();
            for (Method otherMethod : otherMethods) {
                if (otherMethod == method) {
                    continue;
                }
                Set<String> readThisMethod = methodAndReadWrites.get(method).readFields.keySet();
                Set<String> writeThisMethod = methodAndReadWrites.get(method).writeFields.keySet();
                Set<String> readOtherMethod = methodAndReadWrites.get(otherMethod).readFields.keySet();
                Set<String> readWriteFields = new HashSet<String>();
                Set<String> readReadFields = new HashSet<String>();
                for (String readField : readOtherMethod) {
                    if (writeThisMethod.contains(readField)) {
                        readWriteFields.add(readField);
                    }
                    if (readThisMethod.contains(readField)) {
                        readReadFields.add(readField);
                    }
                }
                float readWriteRelevance = 0.0f;
                for (String readWriteField : readWriteFields) {
                    readWriteRelevance += methodAndReadWrites.get(otherMethod).readFields.get(readWriteField) / globalFrequence.get(readWriteField);
                }
                float readReadRelevance = 0.0f;
                for (String readReadField : readReadFields) {
                    readReadRelevance += methodAndReadWrites.get(otherMethod).readFields.get(readReadField) / globalFrequence.get(readReadField);
                }
                float relevance = readWriteRelevance + readReadRelevance;
                relevanceMap.put(otherMethod, relevance);
                if (relevance > 0.0f) {
                    int insertIndex = 0;
                    for (int i = 0; i < dependentMethods.size(); i++) {
                        Method existedMethod = dependentMethods.get(i);
                        if (relevanceMap.get(existedMethod) <= relevance) {
                            insertIndex = i;
                            break;
                        }
                    }
                    if (insertIndex < 0) {
                        insertIndex = 0;
                    }
                    if (insertIndex > dependentMethods.size() - 1) {
                        insertIndex = dependentMethods.size() - 1;
                    }
                    dependentMethods.add(insertIndex, otherMethod);
                }
            }
            dependentMethods.addAll(methodAndReadWrites.keySet());
            relatedMethodMap.put(method, dependentMethods);
        }
        return relatedMethodMap;
    }
