    private void buildIcon() {
        if (iconPath != null) {
            if (iconPath.endsWith(".png") || iconPath.endsWith(".gif") || iconPath.endsWith(".jpg") || iconPath.endsWith(".jpeg")) {
                if (FileUtils.checkImgEncomDirectory(new File(iconPath))) {
                    icon = new ImageIcon(iconPath, name);
                }
                if (FileUtils.checkImgEncomDirectory(new File(iconPath))) icon = new ImageIcon(iconPath, name); else {
                    File source = new File(iconPath);
                    File dest = new File("/ENCOM/images" + source.getName());
                    try {
                        if (FileUtils.copyFile(source, dest)) {
                            icon = new ImageIcon(source.getPath(), name);
                        } else {
                            iconPath = this.DEFAULT_ICON_PATH;
                        }
                    } catch (IOException e) {
                        Logger.getLogger(Store.class.getPackage().getName()).log(Level.WARNING, "IOException occured during icon creation of " + name, e);
                    }
                }
            } else {
                iconPath = this.DEFAULT_ICON_PATH;
                buildIcon();
            }
        }
    }
