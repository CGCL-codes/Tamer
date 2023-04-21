    public void addMeth(final MethAST meth) {
        int low = 0;
        int high = methods.length - 1;
        while (low <= high) {
            final int mid = (low + high) / 2;
            final int compare = meth.compareTo(methods[mid]);
            if (compare < 0) {
                high = mid - 1;
            } else if (compare > 0) {
                low = mid + 1;
            } else {
                low = mid;
                high = mid - 1;
            }
        }
        final MethAST[] newMeths = new MethAST[methods.length + 1];
        System.arraycopy(methods, 0, newMeths, 0, low);
        newMeths[low] = meth;
        System.arraycopy(methods, low, newMeths, low + 1, methods.length - low);
        methods = newMeths;
    }
