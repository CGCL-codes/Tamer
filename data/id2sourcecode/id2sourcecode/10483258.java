    private NFRule findNormalRule(long number) {
        if (isFractionRuleSet) {
            return findFractionRuleSetRule(number);
        }
        if (number < 0) {
            if (negativeNumberRule != null) {
                return negativeNumberRule;
            } else {
                number = -number;
            }
        }
        int lo = 0;
        int hi = rules.length;
        if (hi > 0) {
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                if (rules[mid].getBaseValue() == number) {
                    return rules[mid];
                } else if (rules[mid].getBaseValue() > number) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
            NFRule result = rules[hi - 1];
            if (result.shouldRollBack(number)) {
                result = rules[hi - 2];
            }
            return result;
        }
        return fractionRules[2];
    }
