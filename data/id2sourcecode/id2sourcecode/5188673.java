    public static double calculateInsurance(Map items) throws PortalException, SystemException {
        double insurance = 0.0;
        double subtotal = 0.0;
        ShoppingPreferences prefs = null;
        Iterator itr = items.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();
            ShoppingCartItem cartItem = (ShoppingCartItem) entry.getKey();
            Integer count = (Integer) entry.getValue();
            ShoppingItem item = cartItem.getItem();
            if (prefs == null) {
                ShoppingCategory category = item.getCategory();
                prefs = ShoppingPreferences.getInstance(category.getCompanyId(), category.getGroupId());
            }
            ShoppingItemPrice itemPrice = _getItemPrice(item, count.intValue());
            subtotal += calculateActualPrice(itemPrice) * count.intValue();
        }
        if ((prefs != null) && (subtotal > 0)) {
            double insuranceRate = 0.0;
            double[] range = ShoppingPreferences.INSURANCE_RANGE;
            for (int i = 0; i < range.length - 1; i++) {
                if (subtotal > range[i] && subtotal <= range[i + 1]) {
                    int rangeId = i / 2;
                    if (MathUtil.isOdd(i)) {
                        rangeId = (i + 1) / 2;
                    }
                    insuranceRate = GetterUtil.getDouble(prefs.getInsurance()[rangeId]);
                }
            }
            String formula = prefs.getInsuranceFormula();
            if (formula.equals("flat")) {
                insurance += insuranceRate;
            } else if (formula.equals("percentage")) {
                insurance += subtotal * insuranceRate;
            }
        }
        return insurance;
    }
