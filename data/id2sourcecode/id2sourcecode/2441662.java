    @Override
    public String toString() {
        return "DefaultAccessPolicy{webAdmin=" + webAdmin + ",userSettableName=" + userSettableName + ",checkOwner=" + checkOwner + ",useAuthorities=" + StringUtils.arrayToCommaDelimitedString(useAuthorities) + ",oaiAuthorities=" + StringUtils.arrayToCommaDelimitedString(oaiAuthorities) + ",readAuthorities=" + StringUtils.arrayToCommaDelimitedString(readAuthorities) + ",writeAuthorities=" + StringUtils.arrayToCommaDelimitedString(writeAuthorities) + ",adminAuthorities=" + StringUtils.arrayToCommaDelimitedString(adminAuthorities) + "}";
    }
