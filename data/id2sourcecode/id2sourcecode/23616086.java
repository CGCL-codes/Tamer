    private void addCacheIfNecessary(PrintWriter out, AbstractClassModel classModel) {
        if (classModel.hasStereotype(Constants.CACHED_STEREOTYPE)) {
            out.append("<cache usage=\"read-write\"/>");
        }
    }
