    private void updateCredits() {
        if (getQuantity() > 0) {
            transferBt.setLabel(mode == MODE_BUY ? "Acheter" : "Vendre");
            transferBt.getElement().getStyle().setProperty("visibility", "");
            long payload = fleetData.getPayload();
            long totalWeight = (long) fleetData.getTotalWeight();
            long quantity = getQuantity();
            if (mode == MODE_SELL && quantity > selectedResourceCount) {
                quantity = selectedResourceCount;
                quantityField.setText(String.valueOf(quantity));
            }
            if (mode == MODE_BUY && totalWeight + quantity > payload) {
                quantity = payload - totalWeight;
                quantityField.setText(String.valueOf(quantity));
            }
            double rateBefore = baseRates[selectedResourceIndex];
            double rateAfter = baseRates[selectedResourceIndex] + (mode == MODE_SELL ? -1 : 1) * quantity * variation;
            double rate;
            double min = .005;
            double max = 9.52;
            if (rateAfter < min) {
                double coef = (rateBefore - min) / (rateBefore - rateAfter);
                rate = coef * (rateBefore - min) / 2 + (1 - coef) * min;
                rateAfter = min;
            } else if (rateAfter > max) {
                double coef = (max - rateBefore) / (rateAfter - rateBefore);
                rate = coef * (max - rateBefore) / 2 + (1 - coef) * max;
                rateAfter = max;
            } else {
                rate = (rateAfter + rateBefore) / 2;
            }
            rate *= (1 + (mode == MODE_SELL ? -fees : fees));
            long credits = (long) (mode == MODE_SELL ? Math.floor(rate * quantity) : Math.ceil(rate * quantity));
            long creditsTaxFree = (long) (mode == MODE_SELL ? Math.floor(rateBefore * quantity) : Math.ceil(rateBefore * quantity));
            double tax = mode == MODE_SELL ? 1 - (credits / (double) creditsTaxFree) : Math.abs(creditsTaxFree - credits) / (double) credits;
            playerCreditsLabel.setText(Formatter.formatNumber(playerCredits + (mode == MODE_SELL ? credits : -credits)) + "&nbsp;<img src=\"" + Config.getMediaUrl() + "images/misc/blank.gif\" class=\"resource credits\"/> (" + Formatter.formatNumber((mode == MODE_SELL ? credits : -credits)) + ", dont " + (int) Math.ceil(100 * tax) + "% taxe)");
            payloadValueLabel.setText(totalWeight + (mode == MODE_SELL ? -quantity : quantity) + " / " + payload);
        } else {
            transferBt.getElement().getStyle().setProperty("visibility", "hidden");
            playerCreditsLabel.setText(Formatter.formatNumber(playerCredits) + "&nbsp;<img src=\"" + Config.getMediaUrl() + "images/misc/blank.gif\" class=\"resource credits\"/>");
            payloadValueLabel.setText(fleetData.getTotalWeight() + " / " + fleetData.getPayload());
        }
    }
