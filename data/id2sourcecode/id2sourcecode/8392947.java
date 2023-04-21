    private static Shader createShader(String targetStr, String modeStr, String inputStr) {
        int mode = MODE.NONE;
        int target = IMAGE.OUTPUT;
        int targetChannel = CHANNEL.ALPHA;
        int input = IMAGE.ICON;
        int inputMode = INPUT.CHANNEL;
        int inputChannel = CHANNEL.ALPHA;
        float inputValue = 0;
        try {
            switch(modeStr.charAt(0)) {
                case 'W':
                    mode = MODE.WRITE;
                    break;
                case 'M':
                    mode = MODE.MULTIPLY;
                    break;
                case 'D':
                    mode = MODE.DIVIDE;
                    break;
                case 'A':
                    mode = MODE.ADD;
                    break;
                case 'S':
                    mode = MODE.SUBTRACT;
                    break;
                default:
                    throw (new Exception());
            }
            switch(targetStr.charAt(0)) {
                case 'B':
                    target = IMAGE.BUFFER;
                    break;
                case 'O':
                    target = IMAGE.OUTPUT;
                    break;
                default:
                    throw (new Exception());
            }
            switch(targetStr.charAt(1)) {
                case 'A':
                    targetChannel = CHANNEL.ALPHA;
                    break;
                case 'R':
                    targetChannel = CHANNEL.RED;
                    break;
                case 'G':
                    targetChannel = CHANNEL.GREEN;
                    break;
                case 'B':
                    targetChannel = CHANNEL.BLUE;
                    break;
                default:
                    throw (new Exception());
            }
            boolean isValue = false;
            switch(inputStr.charAt(0)) {
                case 'I':
                    input = IMAGE.ICON;
                    break;
                case 'B':
                    input = IMAGE.BUFFER;
                    break;
                case 'O':
                    input = IMAGE.OUTPUT;
                    break;
                default:
                    inputValue = Float.parseFloat(inputStr);
                    isValue = true;
                    inputMode = INPUT.VALUE;
                    ;
            }
            if (!isValue) switch(inputStr.charAt(1)) {
                case 'A':
                    inputChannel = CHANNEL.ALPHA;
                    break;
                case 'R':
                    inputChannel = CHANNEL.RED;
                    break;
                case 'G':
                    inputChannel = CHANNEL.GREEN;
                    break;
                case 'B':
                    inputChannel = CHANNEL.BLUE;
                    break;
                case 'I':
                    inputMode = INPUT.INTENSITY;
                    break;
                case 'H':
                    inputMode = INPUT.AVERAGE;
                    break;
                default:
                    throw (new Exception());
            }
        } catch (Exception e) {
        }
        return new Shader(mode, target, targetChannel, input, inputMode, inputChannel, inputValue);
    }
