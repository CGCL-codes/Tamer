    public String movePortletIdDown(String[] portletIds, String portletId) {
        for (int i = 0; i < portletIds.length && portletIds.length > 1; i++) {
            if (portletIds[i].equals(portletId)) {
                if (i != portletIds.length - 1) {
                    portletIds[i] = portletIds[i + 1];
                    portletIds[i + 1] = portletId;
                } else {
                    portletIds[i] = portletIds[0];
                    portletIds[0] = portletId;
                }
                break;
            }
        }
        return StringUtil.merge(portletIds);
    }
