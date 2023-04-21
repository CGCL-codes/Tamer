    private void ricompatta() {
        for (int i = 0; i < cardsPresent; i++) {
            if (cards[i] == null && cards[i + 1] != null) {
                cards[i] = cards[i + 1];
                cards[i + 1] = null;
            }
        }
    }
