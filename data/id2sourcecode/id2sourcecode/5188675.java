    public static double calculateShipping(Map items) throws PortalException, SystemException {
        double shipping = 0.0;
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
            if (item.isRequiresShipping()) {
                ShoppingItemPrice itemPrice = _getItemPrice(item, count.intValue());
                if (itemPrice.isUseShippingFormula()) {
                    subtotal += calculateActualPrice(itemPrice) * count.intValue();
                } else {
                    shipping += itemPrice.getShipping() * count.intValue();
                }
            }
        }
        if ((prefs != null) && (subtotal > 0)) {
            double shippingRate = 0.0;
            double[] range = ShoppingPreferences.SHIPPING_RANGE;
            for (int i = 0; i < range.length - 1; i++) {
                if (subtotal > range[i] && subtotal <= range[i + 1]) {
                    int rangeId = i / 2;
                    if (MathUtil.isOdd(i)) {
                        rangeId = (i + 1) / 2;
                    }
                    shippingRate = GetterUtil.getDouble(prefs.getShipping()[rangeId]);
                }
            }
            String formula = prefs.getShippingFormula();
            if (formula.equals("flat")) {
                shipping += shippingRate;
            } else if (formula.equals("percentage")) {
                shipping += subtotal * shippingRate;
            }
        }
        return shipping;
    }
