    @Override
    public void commandAction(Command c, Displayable d) {
        if (c == CMD_ADD) {
            if (d == menuList) {
                Display.getDisplay(this).setCurrent(textBox);
            } else if (d == textBox) {
                System.out.println(textBox.getString());
                try {
                    URL url = new URL(textBox.getString());
                    File file = new File(MIDLET_FOLDER, URLEncoder.encode(url.toString(), "UTF-8").replace('%', '_'));
                    copyToFile(url.openStream(), file);
                    addJar(file);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                Display.getDisplay(this).setCurrent(menuList);
            }
        } else super.commandAction(c, d);
    }
