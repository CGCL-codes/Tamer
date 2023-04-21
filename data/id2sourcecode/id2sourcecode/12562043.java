    private Object applyBinary(ParseElements pe, Best best) throws com.daffodilwoods.database.resource.DException {
        if (comparableRules == null) return pe.parseException;
        int low = 0, high = comparableRules.length - 1;
        int position = pe.position;
        while (low <= high) {
            int mid = (low + high) / 2;
            ProductionRules object = (ProductionRules) comparableRules[mid];
            Object obj = object.parse(pe);
            if (obj instanceof ParseException) {
                ParseException parseException = (ParseException) obj;
                if (!pe.tokenFlag && parseException.returnType == 2) {
                    specialHandling(mid - 1, mid + 1, pe, best, position);
                    return best.sobject;
                }
                if (parseException.returnType < 0) high = mid - 1; else if (parseException.returnType > 0) low = mid + 1; else return obj;
            } else {
                if (position != pe.position) best.update(obj, pe.position);
                if (!object.recursiveflag) {
                    specialHandling(mid - 1, mid + 1, pe, best, position);
                    return best.sobject;
                }
                pe.recursiveObject = best.sobject;
                pe.recursionState = nameOfRule;
                return parseForRecusriveRules(best, pe);
            }
        }
        return pe.parseException;
    }
