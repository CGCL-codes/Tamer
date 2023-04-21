    private void initialize() {
        if (App.hasProjects()) {
            App.addProjectObserver(this, App.Source.ALL);
            setChannel();
            setReceiver();
        } else {
            channel_no = 0;
            try {
                channel = MidiSystem.getSynthesizer().getChannels()[channel_no];
            } catch (MidiUnavailableException e) {
            }
        }
        int width = 0, height = 0;
        if (orientation == ORIENTATION_HORIZONTAL) {
            int currentX = 0;
            Key key;
            for (int i = lowNote; i <= highNote; i++) {
                switch(i % 12) {
                    case 1:
                    case 3:
                    case 6:
                    case 8:
                    case 10:
                        key = new Key((currentX - BLACK_KEY_WIDTH / 2), 0, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT, i, KeyColor.BLACK);
                        blackKeys.add(key);
                        break;
                    default:
                        key = new Key(currentX, 0, WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT, i, KeyColor.WHITE);
                        whiteKeys.add(key);
                        currentX += WHITE_KEY_WIDTH;
                        break;
                }
                keys.add(key);
            }
            width = currentX;
            height = WHITE_KEY_HEIGHT;
        } else if (orientation == ORIENTATION_VERTICAL) {
            int currentY = 0;
            Key key;
            for (int i = highNote; i >= lowNote; i--) {
                switch(i % 12) {
                    case 1:
                    case 3:
                    case 6:
                    case 8:
                    case 10:
                        key = new Key(0, (currentY - BLACK_KEY_WIDTH / 2), BLACK_KEY_HEIGHT, BLACK_KEY_WIDTH, i, KeyColor.BLACK);
                        blackKeys.add(key);
                        break;
                    default:
                        key = new Key(0, currentY, WHITE_KEY_HEIGHT, WHITE_KEY_WIDTH, i, KeyColor.WHITE);
                        whiteKeys.add(key);
                        currentY += WHITE_KEY_WIDTH;
                        break;
                }
                keys.add(key);
            }
            width = WHITE_KEY_HEIGHT;
            height = currentY;
        }
        setPreferredSize(new Dimension(width, height));
    }
