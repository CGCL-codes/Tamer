    public static boolean synchronize(File left, File right) {
        if (left.isDirectory() || right.isDirectory()) {
            String[] leftContents = left.list();
            Set<String> contents = leftContents == null ? new LinkedHashSet<String>() : new LinkedHashSet<String>(Arrays.asList(leftContents));
            String[] rightContents = right.list();
            if (rightContents != null) {
                contents.addAll(Arrays.asList(rightContents));
            }
            for (String name : contents) {
                if (!synchronize(left, right, name)) return false;
            }
        } else {
            long leftTime = left.lastModified();
            long rightTime = right.lastModified();
            if (left.exists() && (!right.exists() || leftTime < rightTime)) {
                return copy(left, right);
            } else if (right.exists() && (!left.exists() || leftTime > rightTime)) {
                return copy(right, left);
            }
        }
        return true;
    }
