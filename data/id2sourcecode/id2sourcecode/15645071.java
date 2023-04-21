    synchronized void recycle(int validationErrorHRecycledCount, int validationErrorHEffectivellyUsed, ValidationErrorHandler[] validationErrorHRecycled, int conflictErrorHRecycledCount, int conflictErrorHEffectivellyUsed, ExternalConflictErrorHandler[] conflictErrorHRecycled, int commonErrorHRecycledCount, int commonErrorHEffectivellyUsed, CommonErrorHandler[] commonErrorHRecycled, int defaultErrorHRecycledCount, int defaultErrorHEffectivellyUsed, DefaultErrorHandler[] defaultErrorHRecycled, int startErrorHRecycledCount, int startErrorHEffectivellyUsed, StartErrorHandler[] startErrorHRecycled) {
        int neededLength = validationErrorHFree + validationErrorHRecycledCount;
        if (neededLength > validationErrorH.length) {
            if (neededLength > validationErrorHMaxSize) {
                neededLength = validationErrorHMaxSize;
                ValidationErrorHandler[] increased = new ValidationErrorHandler[neededLength];
                System.arraycopy(validationErrorH, 0, increased, 0, validationErrorH.length);
                validationErrorH = increased;
                System.arraycopy(validationErrorHRecycled, 0, validationErrorH, validationErrorHFree, validationErrorHMaxSize - validationErrorHFree);
                validationErrorHFree = validationErrorHMaxSize;
            } else {
                ValidationErrorHandler[] increased = new ValidationErrorHandler[neededLength];
                System.arraycopy(validationErrorH, 0, increased, 0, validationErrorH.length);
                validationErrorH = increased;
                System.arraycopy(validationErrorHRecycled, 0, validationErrorH, validationErrorHFree, validationErrorHRecycledCount);
                validationErrorHFree += validationErrorHRecycledCount;
            }
        } else {
            System.arraycopy(validationErrorHRecycled, 0, validationErrorH, validationErrorHFree, validationErrorHRecycledCount);
            validationErrorHFree += validationErrorHRecycledCount;
        }
        if (validationErrorHAverageUse != 0) validationErrorHAverageUse = (validationErrorHAverageUse + validationErrorHEffectivellyUsed) / 2; else validationErrorHAverageUse = validationErrorHEffectivellyUsed;
        for (int i = 0; i < validationErrorHRecycled.length; i++) {
            validationErrorHRecycled[i] = null;
        }
        neededLength = commonErrorHFree + commonErrorHRecycledCount;
        if (neededLength > commonErrorH.length) {
            if (neededLength > commonErrorHMaxSize) {
                neededLength = commonErrorHMaxSize;
                CommonErrorHandler[] increased = new CommonErrorHandler[neededLength];
                System.arraycopy(commonErrorH, 0, increased, 0, commonErrorH.length);
                commonErrorH = increased;
                System.arraycopy(commonErrorHRecycled, 0, commonErrorH, commonErrorHFree, commonErrorHMaxSize - commonErrorHFree);
                commonErrorHFree = commonErrorHMaxSize;
            } else {
                CommonErrorHandler[] increased = new CommonErrorHandler[neededLength];
                System.arraycopy(commonErrorH, 0, increased, 0, commonErrorH.length);
                commonErrorH = increased;
                System.arraycopy(commonErrorHRecycled, 0, commonErrorH, commonErrorHFree, commonErrorHRecycledCount);
                commonErrorHFree += commonErrorHRecycledCount;
            }
        } else {
            System.arraycopy(commonErrorHRecycled, 0, commonErrorH, commonErrorHFree, commonErrorHRecycledCount);
            commonErrorHFree += commonErrorHRecycledCount;
        }
        if (commonErrorHAverageUse != 0) commonErrorHAverageUse = (commonErrorHAverageUse + commonErrorHEffectivellyUsed) / 2; else commonErrorHAverageUse = commonErrorHEffectivellyUsed;
        for (int i = 0; i < commonErrorHRecycled.length; i++) {
            commonErrorHRecycled[i] = null;
        }
        neededLength = conflictErrorHFree + conflictErrorHRecycledCount;
        if (neededLength > conflictErrorH.length) {
            if (neededLength > conflictErrorHMaxSize) {
                neededLength = conflictErrorHMaxSize;
                ExternalConflictErrorHandler[] increased = new ExternalConflictErrorHandler[neededLength];
                System.arraycopy(conflictErrorH, 0, increased, 0, conflictErrorH.length);
                conflictErrorH = increased;
                System.arraycopy(conflictErrorHRecycled, 0, conflictErrorH, conflictErrorHFree, conflictErrorHMaxSize - conflictErrorHFree);
                conflictErrorHFree = conflictErrorHMaxSize;
            } else {
                ExternalConflictErrorHandler[] increased = new ExternalConflictErrorHandler[neededLength];
                System.arraycopy(conflictErrorH, 0, increased, 0, conflictErrorH.length);
                conflictErrorH = increased;
                System.arraycopy(conflictErrorHRecycled, 0, conflictErrorH, conflictErrorHFree, conflictErrorHRecycledCount);
                conflictErrorHFree += conflictErrorHRecycledCount;
            }
        } else {
            System.arraycopy(conflictErrorHRecycled, 0, conflictErrorH, conflictErrorHFree, conflictErrorHRecycledCount);
            conflictErrorHFree += conflictErrorHRecycledCount;
        }
        if (conflictErrorHAverageUse != 0) conflictErrorHAverageUse = (conflictErrorHAverageUse + conflictErrorHEffectivellyUsed) / 2; else conflictErrorHAverageUse = conflictErrorHEffectivellyUsed;
        for (int i = 0; i < conflictErrorHRecycled.length; i++) {
            conflictErrorHRecycled[i] = null;
        }
        neededLength = defaultErrorHFree + defaultErrorHRecycledCount;
        if (neededLength > defaultErrorH.length) {
            if (neededLength > defaultErrorHMaxSize) {
                neededLength = defaultErrorHMaxSize;
                DefaultErrorHandler[] increased = new DefaultErrorHandler[neededLength];
                System.arraycopy(defaultErrorH, 0, increased, 0, defaultErrorH.length);
                defaultErrorH = increased;
                System.arraycopy(defaultErrorHRecycled, 0, defaultErrorH, defaultErrorHFree, defaultErrorHMaxSize - defaultErrorHFree);
                defaultErrorHFree = defaultErrorHMaxSize;
            } else {
                DefaultErrorHandler[] increased = new DefaultErrorHandler[neededLength];
                System.arraycopy(defaultErrorH, 0, increased, 0, defaultErrorH.length);
                defaultErrorH = increased;
                System.arraycopy(defaultErrorHRecycled, 0, defaultErrorH, defaultErrorHFree, defaultErrorHRecycledCount);
                defaultErrorHFree += defaultErrorHRecycledCount;
            }
        } else {
            System.arraycopy(defaultErrorHRecycled, 0, defaultErrorH, defaultErrorHFree, defaultErrorHRecycledCount);
            defaultErrorHFree += defaultErrorHRecycledCount;
        }
        if (defaultErrorHAverageUse != 0) defaultErrorHAverageUse = (defaultErrorHAverageUse + defaultErrorHEffectivellyUsed) / 2; else defaultErrorHAverageUse = defaultErrorHEffectivellyUsed;
        for (int i = 0; i < defaultErrorHRecycled.length; i++) {
            defaultErrorHRecycled[i] = null;
        }
        neededLength = startErrorHFree + startErrorHRecycledCount;
        if (neededLength > startErrorH.length) {
            if (neededLength > startErrorHMaxSize) {
                neededLength = startErrorHMaxSize;
                StartErrorHandler[] increased = new StartErrorHandler[neededLength];
                System.arraycopy(startErrorH, 0, increased, 0, startErrorH.length);
                startErrorH = increased;
                System.arraycopy(startErrorHRecycled, 0, startErrorH, startErrorHFree, startErrorHMaxSize - startErrorHFree);
                startErrorHFree = startErrorHMaxSize;
            } else {
                StartErrorHandler[] increased = new StartErrorHandler[neededLength];
                System.arraycopy(startErrorH, 0, increased, 0, startErrorH.length);
                startErrorH = increased;
                System.arraycopy(startErrorHRecycled, 0, startErrorH, startErrorHFree, startErrorHRecycledCount);
                startErrorHFree += startErrorHRecycledCount;
            }
        } else {
            System.arraycopy(startErrorHRecycled, 0, startErrorH, startErrorHFree, startErrorHRecycledCount);
            startErrorHFree += startErrorHRecycledCount;
        }
        if (startErrorHAverageUse != 0) startErrorHAverageUse = (startErrorHAverageUse + startErrorHEffectivellyUsed) / 2; else startErrorHAverageUse = startErrorHEffectivellyUsed;
        for (int i = 0; i < startErrorHRecycled.length; i++) {
            startErrorHRecycled[i] = null;
        }
    }
