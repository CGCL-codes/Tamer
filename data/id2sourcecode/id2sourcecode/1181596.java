    private boolean generateSubElements(File root, int depth) {
        int subElementsNumber = random.nextInt(maxSubElements + 1);
        if (depth > 0) {
            for (int i = 1; i <= subElementsNumber; i++) {
                if (random.nextInt(100) >= 50) {
                    if (!generateFile(root)) {
                        return false;
                    }
                    if (--elementsNumber <= 0) {
                        return true;
                    }
                } else {
                    if (!generateDir(root, generateName(), depth - 1)) {
                        return false;
                    }
                }
            }
        } else {
            for (int i = 1; i <= subElementsNumber; i++) {
                if (!generateFile(root)) {
                    return false;
                }
                if (--elementsNumber <= 0) {
                    return true;
                }
            }
        }
        return true;
    }
