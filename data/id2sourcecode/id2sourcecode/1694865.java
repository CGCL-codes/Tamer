    public static List sequenceMenus(List menus) {
        List result = new ArrayList();
        for (Iterator it = menus.iterator(); it.hasNext(); ) {
            MenuModel temp = (MenuModel) it.next();
            int index = temp.getIndex();
            int comparePosition;
            for (int minPosition = 0, maxPosition = result.size(); minPosition <= maxPosition; ) {
                if (result.size() == 0) {
                    result.add(0, temp);
                    break;
                }
                comparePosition = (minPosition + maxPosition) / 2;
                if (comparePosition == minPosition) {
                    if (((MenuModel) result.get(comparePosition)).getIndex() > index) result.add(comparePosition, temp); else result.add(comparePosition + 1, temp);
                    break;
                } else {
                    if (((MenuModel) result.get(comparePosition)).getIndex() > index) {
                        maxPosition = comparePosition;
                    } else {
                        minPosition = comparePosition;
                    }
                }
            }
        }
        return result;
    }
