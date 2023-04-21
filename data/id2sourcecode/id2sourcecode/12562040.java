    private Object applyBinary(ParseElements pe) throws com.daffodilwoods.database.resource.DException {
        if (comparableRules == null) return pe.parseException;
        int low = 0, high = comparableRules.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            ProductionRules object = (ProductionRules) comparableRules[mid];
            Object obj = object.parse(pe);
            if (obj instanceof ParseException) {
                ParseException parseException = (ParseException) obj;
                if (parseException.returnType == 2) {
                    Object o = specialHandling(mid - 1, mid + 1, pe, low, high);
                    if (!(o instanceof ParseException)) return o;
                    parseException = (ParseException) o;
                }
                if (parseException.returnType < 0) high = mid - 1; else if (parseException.returnType > 0) low = mid + 1; else return obj;
            } else {
                if (!object.recursiveflag) return obj; else {
                    pe.recursiveObject = obj;
                    pe.recursionState = nameOfRule;
                    return parseForRecusriveRules(obj, pe);
                }
            }
        }
        return pe.parseException;
    }
