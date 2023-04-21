    public void removeFromCategory(String category) {
        synchronized (i2) {
            checkItemDeleted();
            boolean catexist = false;
            if (category == null) throw new NullPointerException();
            loadPIMStruct();
            int j;
            for (j = 0; j < Integer.parseInt(categories[0]); j++) {
                if (category.equals(categories[j + 1])) {
                    catexist = true;
                    break;
                }
            }
            if (catexist) {
                int numCat = Integer.parseInt(categories[0]);
                for (int k = j; k < categories.length - 1; k++) {
                    categories[k] = categories[k + 1];
                }
                categories[0] = Integer.toString(--numCat);
                modified = true;
                if (numCat <= categories.length / 2) categories = adjustStringArray(categories, numCat + 2);
            }
        }
    }
