        private long guessNextTime(long ourNow, boolean success, long dateField, long lastModField, long expiresField) {
            long updateDelta;
            if (success) {
                long thisDelta;
                failCount = 0;
                if (++successCount == successCountToMin) {
                    successCount = 0;
                    thisDelta = delta = getMinimumInterval();
                } else if (prevLocalDate != 0) thisDelta = ourNow - prevLocalDate; else thisDelta = getMinimumInterval();
                if (lastModField != 0 && prevLastMod != 0) {
                    long serverDelta = lastModField - prevLastMod;
                    thisDelta = (thisDelta + serverDelta) / 2;
                }
                prevLastMod = lastModField;
                thisDelta = boundInterval(thisDelta);
                if (delta == 0) delta = thisDelta; else if (thisDelta > delta) delta = (3 * delta + thisDelta) / 4; else delta = (delta + 3 * thisDelta) / 4;
                updateDelta = delta * deltaFraction / 100;
            } else {
                successCount = 0;
                if (failCount + 1 == failCountToMax) {
                    updateDelta = getMaximumInterval();
                } else {
                    ++failCount;
                    updateDelta = getMinimumInterval();
                }
            }
            if (dateField != 0 && expiresField > dateField) {
                updateDelta = expiresField - dateField;
            }
            if (success) prevLocalDate = ourNow;
            updateDelta = boundInterval(updateDelta);
            return ourNow + updateDelta;
        }
