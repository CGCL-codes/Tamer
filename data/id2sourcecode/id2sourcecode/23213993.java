    void _testUsingBinarySearch(SimpleTimeZone tz, long min, long max, long expectedBoundary) {
        boolean startsInDST = tz.inDaylightTime(new Date(min));
        if (tz.inDaylightTime(new Date(max)) == startsInDST) {
            logln("Error: inDaylightTime(" + new Date(max) + ") != " + (!startsInDST));
            return;
        }
        while ((max - min) > INTERVAL) {
            long mid = (min + max) / 2;
            if (tz.inDaylightTime(new Date(mid)) == startsInDST) {
                min = mid;
            } else {
                max = mid;
            }
        }
        logln("Binary Search Before: " + min + " = " + new Date(min));
        logln("Binary Search After:  " + max + " = " + new Date(max));
        long mindelta = expectedBoundary - min;
        if (mindelta >= 0 && mindelta <= INTERVAL && mindelta >= 0 && mindelta <= INTERVAL) logln("PASS: Expected bdry:  " + expectedBoundary + " = " + new Date(expectedBoundary)); else errln("FAIL: Expected bdry:  " + expectedBoundary + " = " + new Date(expectedBoundary));
    }
