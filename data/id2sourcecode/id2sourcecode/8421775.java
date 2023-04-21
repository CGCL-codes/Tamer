    private static void initX() {
        if (desktop != null) {
            return;
        }
        boolean supported = false;
        Method browseM = null;
        Object desktopObj = null;
        try {
            Class<?> desktopCls = Class.forName("java.awt.Desktop", true, null);
            Method getDesktopM = desktopCls.getMethod("getDesktop");
            browseM = desktopCls.getMethod("browse", URI.class);
            Class<?> actionCls = Class.forName("java.awt.Desktop$Action", true, null);
            final Method isDesktopSupportedMethod = desktopCls.getMethod("isDesktopSupported");
            Method isSupportedMethod = desktopCls.getMethod("isSupported", actionCls);
            Field browseField = actionCls.getField("BROWSE");
            Thread xthread = new Thread() {

                public void run() {
                    try {
                        result = (Boolean) isDesktopSupportedMethod.invoke(null);
                    } catch (IllegalAccessException e) {
                        InternalError x = new InternalError("Desktop.getDesktop() method not found");
                        x.initCause(e);
                    } catch (InvocationTargetException e) {
                        if (Util.isVerbose()) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            xthread.setDaemon(true);
            xthread.start();
            try {
                xthread.join(5 * 1000);
            } catch (InterruptedException ie) {
            }
            if (result.booleanValue()) {
                desktopObj = getDesktopM.invoke(null);
                result = (Boolean) isSupportedMethod.invoke(desktopObj, browseField.get(null));
                supported = result.booleanValue();
            }
        } catch (ClassNotFoundException e) {
            if (Util.isVerbose()) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            if (Util.isVerbose()) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            if (Util.isVerbose()) {
                e.printStackTrace();
            }
        } catch (IllegalAccessException e) {
            InternalError x = new InternalError("Desktop.getDesktop() method not found");
            x.initCause(e);
            throw x;
        } catch (InvocationTargetException e) {
            if (Util.isVerbose()) {
                e.printStackTrace();
            }
        }
        isBrowseSupported = supported;
        browseMethod = browseM;
        desktop = desktopObj;
    }
