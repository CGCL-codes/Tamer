    public void init() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        colors[0] = new Color(255, 128, 64);
        colors[1] = new Color(255, 0, 0);
        colors[2] = new Color(0, 255, 0);
        colors[3] = new Color(0, 0, 255);
        colors[4] = new Color(153, 153, 153);
        colors[5] = new Color(170, 170, 68);
        colors[6] = new Color(187, 119, 68);
        colors[7] = new Color(153, 68, 68);
        colors[8] = new Color(68, 119, 68);
        colors[9] = new Color(0, 68, 119);
        colors[10] = new Color(255, 255, 255);
        colors[11] = new Color(255, 255, 0);
        colors[12] = new Color(255, 96, 32);
        colors[13] = new Color(208, 0, 0);
        colors[14] = new Color(0, 144, 0);
        colors[15] = new Color(32, 64, 208);
        colors[16] = new Color(176, 176, 176);
        colors[17] = new Color(80, 80, 80);
        colors[18] = new Color(255, 0, 255);
        colors[19] = new Color(0, 255, 255);
        colors[20] = new Color(255, 160, 192);
        colors[21] = new Color(32, 255, 16);
        colors[22] = new Color(0, 0, 0);
        colors[23] = new Color(128, 128, 128);
        animThread = new Thread(this, "Cube Animator");
        animThread.start();
        String param = getParameter("config");
        if (param != null) {
            try {
                URL url = new URL(getDocumentBase(), param);
                InputStream input = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String line = reader.readLine();
                while (line != null) {
                    int pos = line.indexOf('=');
                    if (pos > 0) {
                        String key = line.substring(0, pos).trim();
                        String value = line.substring(pos + 1).trim();
                        config.put(key, value);
                    }
                    line = reader.readLine();
                }
                reader.close();
            } catch (MalformedURLException ex) {
                System.err.println("Malformed URL: " + param + ": " + ex);
            } catch (IOException ex) {
                System.err.println("Input error: " + param + ": " + ex);
            }
        }
        param = getParameter("bgcolor");
        if (param != null && param.length() == 6) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 16; j++) {
                    if (Character.toLowerCase(param.charAt(i)) == "0123456789abcdef".charAt(j)) {
                        hex[i] = j;
                        break;
                    }
                }
            }
            bgColor = new Color(hex[0] * 16 + hex[1], hex[2] * 16 + hex[3], hex[4] * 16 + hex[5]);
        } else bgColor = Color.gray;
        param = getParameter("butbgcolor");
        if (param != null && param.length() == 6) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 16; j++) {
                    if (Character.toLowerCase(param.charAt(i)) == "0123456789abcdef".charAt(j)) {
                        hex[i] = j;
                        break;
                    }
                }
            }
            buttonBgColor = new Color(hex[0] * 16 + hex[1], hex[2] * 16 + hex[3], hex[4] * 16 + hex[5]);
        } else buttonBgColor = bgColor;
        param = getParameter("colors");
        if (param != null) {
            for (int k = 0; k < 10 && k < param.length() / 6; k++) {
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 16; j++) {
                        if (Character.toLowerCase(param.charAt(k * 6 + i)) == "0123456789abcdef".charAt(j)) {
                            hex[i] = j;
                            break;
                        }
                    }
                }
                colors[k] = new Color(hex[0] * 16 + hex[1], hex[2] * 16 + hex[3], hex[4] * 16 + hex[5]);
            }
        }
        for (int i = 0; i < 6; i++) for (int j = 0; j < 9; j++) cube[i][j] = i + 10;
        String initialPosition = "lluu";
        param = getParameter("colorscheme");
        if (param != null && param.length() == 6) {
            for (int i = 0; i < 6; i++) {
                int color = 23;
                for (int j = 0; j < 23; j++) {
                    if (Character.toLowerCase(param.charAt(i)) == "0123456789wyorgbldmcpnk".charAt(j)) {
                        color = j;
                        break;
                    }
                }
                for (int j = 0; j < 9; j++) cube[i][j] = color;
            }
        }
        param = getParameter("pos");
        if (param != null && param.length() == 54) {
            initialPosition = "uuuuff";
            if (bgColor == Color.gray) bgColor = Color.white;
            for (int i = 0; i < 6; i++) {
                int ti = posFaceTransform[i];
                for (int j = 0; j < 9; j++) {
                    int tj = posFaceletTransform[i][j];
                    cube[ti][tj] = 23;
                    for (int k = 0; k < 14; k++) {
                        if (param.charAt(i * 9 + j) == "DFECABdfecabgh".charAt(k)) {
                            cube[ti][tj] = k + 4;
                            break;
                        }
                    }
                }
            }
        }
        param = getParameter("facelets");
        if (param != null && param.length() == 54) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 9; j++) {
                    cube[i][j] = 23;
                    for (int k = 0; k < 23; k++) {
                        if (Character.toLowerCase(param.charAt(i * 9 + j)) == "0123456789wyorgbldmcpnk".charAt(k)) {
                            cube[i][j] = k;
                            break;
                        }
                    }
                }
            }
        }
        param = getParameter("move");
        move = (param == null ? new int[0][0] : getMove(param, true));
        movePos = 0;
        curInfoText = -1;
        param = getParameter("initmove");
        if (param != null) {
            int[][] initialMove = param.equals("#") ? move : getMove(param, false);
            if (initialMove.length > 0) doMove(cube, initialMove[0], 0, initialMove[0].length, false);
        }
        param = getParameter("initrevmove");
        if (param != null) {
            int[][] initialReversedMove = param.equals("#") ? move : getMove(param, false);
            if (initialReversedMove.length > 0) doMove(cube, initialReversedMove[0], 0, initialReversedMove[0].length, true);
        }
        param = getParameter("demo");
        if (param != null) {
            demoMove = param.equals("#") ? move : getMove(param, true);
            if (demoMove.length > 0 && demoMove[0].length > 0) demo = true;
        }
        param = getParameter("position");
        vNorm(vMul(eyeY, eye, eyeX));
        if (param == null) param = initialPosition;
        double pi12 = Math.PI / 12;
        for (int i = 0; i < param.length(); i++) {
            double angle = pi12;
            switch(Character.toLowerCase(param.charAt(i))) {
                case 'd':
                    angle = -angle;
                case 'u':
                    vRotY(eye, angle);
                    vRotY(eyeX, angle);
                    break;
                case 'f':
                    angle = -angle;
                case 'b':
                    vRotZ(eye, angle);
                    vRotZ(eyeX, angle);
                    break;
                case 'l':
                    angle = -angle;
                case 'r':
                    vRotX(eye, angle);
                    vRotX(eyeX, angle);
                    break;
            }
        }
        vNorm(vMul(eyeY, eye, eyeX));
        speed = 0;
        doubleSpeed = 0;
        param = getParameter("speed");
        if (param != null) for (int i = 0; i < param.length(); i++) if (param.charAt(i) >= '0' && param.charAt(i) <= '9') speed = speed * 10 + (int) param.charAt(i) - '0';
        param = getParameter("doublespeed");
        if (param != null) for (int i = 0; i < param.length(); i++) if (param.charAt(i) >= '0' && param.charAt(i) <= '9') doubleSpeed = doubleSpeed * 10 + (int) param.charAt(i) - '0';
        if (speed == 0) speed = 10;
        if (doubleSpeed == 0) doubleSpeed = speed * 3 / 2;
        persp = 0;
        param = getParameter("perspective");
        if (param == null) persp = 2; else for (int i = 0; i < param.length(); i++) if (param.charAt(i) >= '0' && param.charAt(i) <= '9') persp = persp * 10 + (int) param.charAt(i) - '0';
        int intscale = 0;
        param = getParameter("scale");
        if (param != null) for (int i = 0; i < param.length(); i++) if (param.charAt(i) >= '0' && param.charAt(i) <= '9') intscale = intscale * 10 + (int) param.charAt(i) - '0';
        scale = 1.0 / (1.0 + intscale / 10.0);
        hint = false;
        param = getParameter("hint");
        if (param != null) {
            hint = true;
            faceShift = 0.0;
            for (int i = 0; i < param.length(); i++) if (param.charAt(i) >= '0' && param.charAt(i) <= '9') faceShift = faceShift * 10 + (int) param.charAt(i) - '0';
            if (faceShift < 1.0) hint = false; else faceShift /= 10.0;
        }
        buttonBar = 1;
        buttonHeight = 13;
        progressHeight = move.length == 0 ? 0 : 6;
        param = getParameter("buttonbar");
        if ("0".equals(param)) {
            buttonBar = 0;
            buttonHeight = 0;
            progressHeight = 0;
        } else if ("1".equals(param)) buttonBar = 1; else if ("2".equals(param) || move.length == 0) {
            buttonBar = 2;
            progressHeight = 0;
        }
        param = getParameter("edit");
        if ("0".equals(param)) editable = false; else editable = true;
        param = getParameter("movetext");
        if ("1".equals(param)) moveText = 1; else if ("2".equals(param)) moveText = 2; else if ("3".equals(param)) moveText = 3; else if ("4".equals(param)) moveText = 4; else moveText = 0;
        param = getParameter("fonttype");
        if (param == null || "1".equals(param)) outlined = true; else outlined = false;
        metric = 0;
        param = getParameter("metric");
        if (param != null) {
            if ("1".equals(param)) metric = 1; else if ("2".equals(param)) metric = 2; else if ("3".equals(param)) metric = 3;
        }
        align = 1;
        param = getParameter("align");
        if (param != null) {
            if ("0".equals(param)) align = 0; else if ("1".equals(param)) align = 1; else if ("2".equals(param)) align = 2;
        }
        for (int i = 0; i < 6; i++) for (int j = 0; j < 9; j++) initialCube[i][j] = cube[i][j];
        for (int i = 0; i < 3; i++) {
            initialEye[i] = eye[i];
            initialEyeX[i] = eyeX[i];
            initialEyeY[i] = eyeY[i];
        }
        int red = bgColor.getRed();
        int green = bgColor.getGreen();
        int blue = bgColor.getBlue();
        int average = (red * 299 + green * 587 + blue * 114) / 1000;
        if (average < 128) {
            textColor = Color.white;
            hlColor = bgColor.brighter();
            hlColor = new Color(hlColor.getBlue(), hlColor.getRed(), hlColor.getGreen());
        } else {
            textColor = Color.black;
            hlColor = bgColor.darker();
            hlColor = new Color(hlColor.getBlue(), hlColor.getRed(), hlColor.getGreen());
        }
        bgColor2 = new Color(red / 2, green / 2, blue / 2);
        curInfoText = -1;
        if (demo) startAnimation(-1);
    }
