    private Object generateValue(int parentLevel) {
        int maxValueType = V_ARRAY;
        if (parentLevel >= maxLevel && nodeCount >= minNodeCount) maxValueType = V_NULL;
        int valueType = random.nextInt(maxValueType + 1);
        nodeCount++;
        switch(valueType) {
            case V_STRING:
                return generateString();
            case V_INT:
                return generateInt();
            case V_DOUBLE:
                return generateDouble();
            case V_BOOLEAN:
                return generateBoolean();
            case V_NULL:
                return null;
            case V_OBJECT:
                return generateObjectMeta(parentLevel + 1);
            case V_ARRAY:
                return generateArrayMeta(parentLevel + 1);
        }
        return null;
    }
