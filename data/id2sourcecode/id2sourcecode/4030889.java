    public LiferayPortletURLImpl(HttpServletRequest request, String portletId, WindowState windowState, PortletMode portletMode, long plid, String lifecycle) {
        super(request, portletId, plid, lifecycle);
        setLifecycle(getLifecyclePhase(lifecycle));
        setURLType(getChannelURlType(lifecycle));
        try {
            setWindowState(windowState);
        } catch (WindowStateException wse1) {
            if (_log.isWarnEnabled()) {
                _log.warn("Exception while setting window state for " + portletId, wse1);
            }
            try {
                setWindowState(WindowState.NORMAL);
            } catch (WindowStateException wse2) {
            }
        }
        try {
            setPortletMode(portletMode);
        } catch (PortletModeException pme1) {
            if (_log.isWarnEnabled()) {
                _log.warn("Exception while setting portlet mode for " + portletId, pme1);
            }
            try {
                setPortletMode(PortletMode.VIEW);
            } catch (PortletModeException pme2) {
            }
        }
    }
