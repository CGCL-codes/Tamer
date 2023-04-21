    public String getLevelFullName() {
        String name = null;
        if (level == null) {
            name = ResourceUtil.getString("application.unknown");
        } else if (level.equals(SubversionConstants.SVN_ACCESS_LEVEL_DENY_ACCESS)) {
            name = ResourceUtil.getString("accesslevel.denyaccess");
        } else if (level.equalsIgnoreCase(SubversionConstants.SVN_ACCESS_LEVEL_READONLY)) {
            name = ResourceUtil.getString("accesslevel.readonly");
        } else if (level.equalsIgnoreCase(SubversionConstants.SVN_ACCESS_LEVEL_READWRITE)) {
            name = ResourceUtil.getString("accesslevel.readwrite");
        }
        return name;
    }
