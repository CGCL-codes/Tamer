    public void execute(View view, String command, Output output) {
        stop();
        ConsoleShellPluginPart.clearErrors();
        String osName = System.getProperty("os.name");
        boolean appendEXE = (osName.indexOf("Windows") != -1 || osName.indexOf("OS/2") != -1);
        if (appendEXE) {
            int spaceIndex = command.indexOf(' ');
            if (spaceIndex == -1) spaceIndex = command.length();
            int dotIndex = command.indexOf('.');
            if (dotIndex == -1 || dotIndex > spaceIndex) {
                command = command.substring(0, spaceIndex) + ".exe" + command.substring(spaceIndex);
            }
        }
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < command.length(); i++) {
            char c = command.charAt(i);
            switch(c) {
                case '$':
                    if (i == command.length() - 1) buf.append(c); else {
                        Buffer buffer = view.getBuffer();
                        switch(command.charAt(++i)) {
                            case 'd':
                                buf.append(buffer.getFile().getParent());
                                break;
                            case 'u':
                                String path = buffer.getPath();
                                if (!MiscUtilities.isURL(path)) path = "file:" + path;
                                buf.append(path);
                                break;
                            case 'f':
                                buf.append(buffer.getPath());
                                break;
                            case 'j':
                                buf.append(jEdit.getJEditHome());
                                break;
                            case 'n':
                                String name = buffer.getName();
                                int index = name.lastIndexOf('.');
                                if (index == -1) buf.append(name); else buf.append(name.substring(0, index));
                                break;
                            case '$':
                                buf.append('$');
                                break;
                        }
                    }
                    break;
                case '~':
                    if (i == command.length() - 1) {
                        buf.append(System.getProperty("user.home"));
                        break;
                    }
                    c = command.charAt(i + 1);
                    if (c == '/' || c == ' ' || c == File.separatorChar) {
                        buf.append(System.getProperty("user.home"));
                        break;
                    }
                    buf.append('~');
                    break;
                default:
                    buf.append(c);
            }
        }
        command = buf.toString();
        try {
            process = Runtime.getRuntime().exec(command);
            process.getOutputStream().close();
        } catch (IOException io) {
            String[] args = { io.getMessage() };
            output.printInfo(jEdit.getProperty("console.shell.ioerror", args));
            return;
        }
        this.command = command;
        this.output = output;
        stdout = new StdoutThread();
        stderr = new StderrThread();
    }
