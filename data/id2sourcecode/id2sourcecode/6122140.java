    public void removeGift(Gift aGift) {
        for (int x = 0; x < gifts.length; x++) {
            if (gifts[x] != null) {
                if (gifts[x].getId() == aGift.getId()) {
                    gifts[x] = null;
                    for (int y = x; y < gifts.length - 1; y++) {
                        gifts[y] = gifts[y + 1];
                        gifts[y + 1] = null;
                    }
                    return;
                }
            }
        }
    }
