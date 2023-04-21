    public static void browseURL(URI uri) {
        if (isDesktopSupported()) {
            try {
                Class<?> desktopClass = Class.forName("java.awt.Desktop");
                Method method = desktopClass.getMethod("getDesktop", (Class[]) null);
                Object desktop = method.invoke(null, (Object[]) null);
                method = desktopClass.getMethod("browse", new Class[] { URI.class });
                method.invoke(desktop, new Object[] { uri });
            } catch (ClassNotFoundException e) {
                log.debug("ClassNotFoundException: " + e.getMessage());
            } catch (NoSuchMethodException e) {
                log.error("NoSuchMethodException: " + e.getMessage());
            } catch (InvocationTargetException e) {
                log.error("InvocationTargetException: " + e.getMessage());
            } catch (IllegalAccessException e) {
                log.error("IllegalAccessException: " + e.getMessage());
            } catch (ClassCastException e) {
                log.error("ClassCastException: " + e.getMessage());
            } catch (Throwable e) {
                log.error("Throwable: " + e.getClass().getName() + " - " + e.getMessage());
            }
        }
    }
