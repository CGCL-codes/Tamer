    private static Object locateBbrowser() {
        if (browser != null) {
            return browser;
        }
        switch(jvm) {
            case MRJ_2_0:
                try {
                    Integer finderCreatorCode = (Integer) makeOSType.invoke(null, new Object[] { FINDER_CREATOR });
                    Object aeTarget = aeTargetConstructor.newInstance(new Object[] { finderCreatorCode });
                    Integer gurlType = (Integer) makeOSType.invoke(null, new Object[] { GURL_EVENT });
                    Object appleEvent = appleEventConstructor.newInstance(new Object[] { gurlType, gurlType, aeTarget, kAutoGenerateReturnID, kAnyTransactionID });
                    return appleEvent;
                } catch (IllegalAccessException iae) {
                    browser = null;
                    errorMessage = iae.getMessage();
                    return browser;
                } catch (InstantiationException ie) {
                    browser = null;
                    errorMessage = ie.getMessage();
                    return browser;
                } catch (InvocationTargetException ite) {
                    browser = null;
                    errorMessage = ite.getMessage();
                    return browser;
                }
            case MRJ_2_1:
                File systemFolder;
                try {
                    systemFolder = (File) findFolder.invoke(null, new Object[] { kSystemFolderType });
                } catch (IllegalArgumentException iare) {
                    browser = null;
                    errorMessage = iare.getMessage();
                    return browser;
                } catch (IllegalAccessException iae) {
                    browser = null;
                    errorMessage = iae.getMessage();
                    return browser;
                } catch (InvocationTargetException ite) {
                    browser = null;
                    errorMessage = ite.getTargetException().getClass() + ": " + ite.getTargetException().getMessage();
                    return browser;
                }
                String[] systemFolderFiles = systemFolder.list();
                for (int i = 0; i < systemFolderFiles.length; i++) {
                    try {
                        File file = new File(systemFolder, systemFolderFiles[i]);
                        if (!file.isFile()) {
                            continue;
                        }
                        Object fileType = getFileType.invoke(null, new Object[] { file });
                        if (FINDER_TYPE.equals(fileType.toString())) {
                            Object fileCreator = getFileCreator.invoke(null, new Object[] { file });
                            if (FINDER_CREATOR.equals(fileCreator.toString())) {
                                browser = file.toString();
                                return browser;
                            }
                        }
                    } catch (IllegalArgumentException iare) {
                        browser = browser;
                        errorMessage = iare.getMessage();
                        return null;
                    } catch (IllegalAccessException iae) {
                        browser = null;
                        errorMessage = iae.getMessage();
                        return browser;
                    } catch (InvocationTargetException ite) {
                        browser = null;
                        errorMessage = ite.getTargetException().getClass() + ": " + ite.getTargetException().getMessage();
                        return browser;
                    }
                }
                browser = null;
                break;
            case MRJ_3_0:
            case MRJ_3_1:
                browser = "";
                break;
            case WINDOWS_NT:
                browser = "cmd.exe";
                break;
            case WINDOWS_9x:
                browser = "command.com";
                break;
            case OTHER:
            default:
                browser = "netscape";
                break;
        }
        return browser;
    }
