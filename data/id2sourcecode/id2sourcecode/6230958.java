    public AbstractBaseEntity start(final NetOperationParam param) {
        final int mNOCode = param.getmNOCode();
        ArrayList<String> params = param.getParams();
        AbstractBaseEntity tmp = null;
        switch(mNOCode) {
            case LOGIN:
                tmp = login(params.get(0), params.get(1));
                break;
            case START_TRANSFER:
                tmp = startTransfer(params.get(0), params.get(1), params.get(2));
                break;
            case GET_USER_FROM:
                tmp = getUserFrom();
                break;
            case GET_CHANNEL:
                tmp = getChannel(params.get(0));
                break;
            case GET_DOWN_ADDR:
                tmp = getDownAddr(params.get(0));
                break;
            case GET_UP_ADDR:
                tmp = getUpAddr(params.get(0));
                break;
            case SEND_PTZ_CONTROL:
                tmp = sendPTZControl(params.get(0), params.get(1), params.get(2));
                break;
            case GET_HISTORY_GROUP:
                tmp = getHistoryGroup();
                break;
            case GET_HISTORY_FILE:
                tmp = getHistoryFile(params.get(0));
                break;
            case GET_ALL_POINT:
                tmp = getAllPoint();
                break;
            case GET_USER_GROUP:
                tmp = getUserGroup();
                break;
            case GET_PERMISSION_ID:
                tmp = getPermissionId(params.get(0));
                break;
            case GET_SDK_VERSION:
                tmp = getSDKVersion();
                break;
            case LOGOUT:
                tmp = logout();
                break;
            case GET_AHSS:
                tmp = getAhss();
                break;
            case GET_RTSP:
                tmp = getRtsp();
                break;
            case GET_TCS:
                tmp = getTcs();
                break;
            case GET_PLAN_GROUP:
                tmp = getPlanGroup();
                break;
            case GET_PLAN_CHANNEL:
                tmp = getPlanChannel(params.get(0));
                break;
            case GET_SUDDEN_CHANNEL:
                tmp = getSuddenChannel();
                break;
            case GET_PLAN_ENABLE:
                tmp = getPlanEnable();
                break;
            case GO_PLAYER:
                tmp = goPlayer(params.get(0));
                break;
            case GET_PREVIEW:
                tmp = getPreview(params.get(0), params.get(1), params.get(2), params.get(3), params.get(4), params.get(5));
                break;
            default:
                break;
        }
        return tmp;
    }
