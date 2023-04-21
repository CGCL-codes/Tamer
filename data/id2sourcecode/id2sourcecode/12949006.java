    public ConstantString INPUT(MaverickString result, ConstantString length, boolean processLine, boolean newline, MaverickString status) throws MaverickException {
        result.clear();
        try {
            boolean icanon = (pty.getAttribute(Termios.ICANON) != 0);
            boolean echo = (pty.getAttribute(Termios.ECHO) != 0);
            boolean localEcho = false;
            int max = length.intValue();
            if (max < 0) {
                if (in.ready()) {
                    result.set(1);
                    return ConstantString.RETURN_SUCCESS;
                } else {
                    result.set(0);
                    return ConstantString.RETURN_ELSE;
                }
            } else if (max > 0 || !newline) {
                if (icanon) {
                    pty.setAttribute(Termios.ICANON, 0);
                }
                if (echo) {
                    pty.setAttribute(Termios.ECHO, 0);
                }
                localEcho = true;
            }
            PrintChannel out = session.getChannel(Session.SCREEN_CHANNEL);
            ConstantString prompt = session.getPrompt();
            boolean showPrompt = (prompt != null && prompt.length() > 0);
            if (showPrompt && !in.ready()) {
                out.PRINT(prompt.charAt(0), status);
                showPrompt = false;
            }
            int ch;
            int erase = pty.getAttribute(Termios.VERASE);
            while ((processLine || (max > 0 && result.length() < max)) && ((ch = in.read()) != -1 && ch != Termios.CR && ch != Termios.LF)) {
                if (ch == erase) {
                    if (result.length() > 0) {
                        result.setLength(result.length() - 1);
                        if (localEcho) {
                            out.PRINT(erase, status);
                            out.PRINT(' ', status);
                            out.PRINT(erase, status);
                        }
                    } else if (localEcho) {
                        out.PRINT(Termios.BELL, status);
                    }
                } else if (max > 0 && result.length() >= max) {
                    if (localEcho) {
                        out.PRINT(Termios.BELL, status);
                    }
                } else {
                    result.append((char) ch);
                    if (localEcho) {
                        out.PRINT(ch, status);
                    }
                }
                if (showPrompt && !in.ready()) {
                    out.PRINT(prompt.charAt(0), status);
                    showPrompt = false;
                }
            }
            if (localEcho && newline) {
                out.PRINT(ConstantString.EMPTY, true, status);
            }
            if (localEcho) {
                if (icanon) {
                    pty.setAttribute(Termios.ICANON, 1);
                }
                if (echo) {
                    pty.setAttribute(Termios.ECHO, 1);
                }
            }
            return ConstantString.RETURN_SUCCESS;
        } catch (IOException ioe) {
            throw new MaverickException(0, ioe);
        }
    }
