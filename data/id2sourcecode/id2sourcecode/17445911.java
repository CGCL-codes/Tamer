    public void run() {
        lastEvent = new SAWGraphicsEvent();
        session.getSession().getClipboardTransferTask().setInputStream(connection.getGraphicsClipboardDataInputStream());
        session.getSession().getClipboardTransferTask().setOutputStream(connection.getGraphicsClipboardDataOutputStream());
        while (!stopped) {
            try {
                switch(connection.getGraphicsControlDataInputStream().read()) {
                    case SAW.SAW_GRAPHICS_MODE_MOUSE_INPUT_MOVE:
                        {
                            lastEvent.x = connection.getGraphicsControlDataInputStream().readInt();
                            lastEvent.y = connection.getGraphicsControlDataInputStream().readInt();
                            controlProvider.mouseMove(lastEvent.x, lastEvent.y);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_MOUSE_INPUT_KEY_DOWN:
                        {
                            lastEvent.button = connection.getGraphicsControlDataInputStream().readInt();
                            controlProvider.mousePress(lastEvent.button);
                            pressedMouseKeys.add(lastEvent.button);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_MOUSE_INPUT_KEY_UP:
                        {
                            lastEvent.button = connection.getGraphicsControlDataInputStream().readInt();
                            controlProvider.mouseRelease(lastEvent.button);
                            pressedMouseKeys.remove(lastEvent.button);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_MOUSE_INPUT_WHEEL:
                        {
                            lastEvent.wheel = connection.getGraphicsControlDataInputStream().readInt();
                            controlProvider.mouseWheel(lastEvent.wheel);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_KEYBOARD_INPUT_KEY_DOWN:
                        {
                            lastEvent.keyCode = connection.getGraphicsControlDataInputStream().readInt();
                            controlProvider.keyPress(lastEvent.keyCode);
                            pressedKeyboardKeys.add(lastEvent.keyCode);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_KEYBOARD_INPUT_KEY_UP:
                        {
                            lastEvent.keyCode = connection.getGraphicsControlDataInputStream().readInt();
                            controlProvider.keyRelease(lastEvent.keyCode);
                            pressedKeyboardKeys.remove(lastEvent.keyCode);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_KEYBOARD_LOCK_KEY_STATE_ON:
                        {
                            lastEvent.keyCode = connection.getGraphicsControlDataInputStream().readInt();
                            controlProvider.setLockingKeyState(lastEvent.keyCode, true);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_KEYBOARD_LOCK_KEY_STATE_OFF:
                        {
                            lastEvent.keyCode = connection.getGraphicsControlDataInputStream().readInt();
                            controlProvider.setLockingKeyState(lastEvent.keyCode, false);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_ALL_INPUT_RELEASE_ALL_PRESSED_KEYS:
                        {
                            releaseAllPressedKeys();
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_REFRESH_REQUEST:
                        {
                            writer.requestRefresh();
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_CLEAR_REQUEST:
                        {
                            writer.requestClear();
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_COLOR_QUALITY_LOW:
                        {
                            writer.setColorQuality(SAWAWTScreenCaptureProvider.SAW_COLOR_QUALITY_LOW);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_COLOR_QUALITY_MEDIUM:
                        {
                            writer.setColorQuality(SAWAWTScreenCaptureProvider.SAW_COLOR_QUALITY_MEDIUM);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_COLOR_QUALITY_HIGH:
                        {
                            writer.setColorQuality(SAWAWTScreenCaptureProvider.SAW_COLOR_QUALITY_HIGH);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_REFRESH_MODE_PARTIAL:
                        {
                            writer.setCompleteRefresh(false);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_REFRESH_MODE_COMPLETE:
                        {
                            writer.setCompleteRefresh(true);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_DRAW_POINTER_ON:
                        {
                            writer.setDrawPointer(true);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_DRAW_POINTER_OFF:
                        {
                            writer.setDrawPointer(false);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_CAPTURE_INTERVAL_CHANGE:
                        {
                            writer.setScreenCaptureInterval(connection.getGraphicsControlDataInputStream().readInt());
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_REFRESH_MODE_INTERRUPTED:
                        {
                            writer.setRefreshInterrupted(true);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_CLIPBOARD_TRANSFER_SEND_REQUEST:
                        {
                            session.getSession().getClipboardTransferThread().join();
                            session.getSession().getClipboardTransferTask().setSending(false);
                            session.getSession().setClipboardTransferThread(new Thread(session.getSession().getClipboardTransferTask()));
                            session.getSession().getClipboardTransferThread().start();
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_CLIPBOARD_TRANSFER_RECEIVE_REQUEST:
                        {
                            session.getSession().getClipboardTransferThread().join();
                            session.getSession().getClipboardTransferTask().setSending(true);
                            session.getSession().setClipboardTransferThread(new Thread(session.getSession().getClipboardTransferTask()));
                            session.getSession().getClipboardTransferThread().start();
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_CLIPBOARD_TRANSFER_CANCEL_REQUEST:
                        {
                            if (session.getSession().getClipboardTransferTask().isSending()) {
                                connection.getGraphicsClipboardOutputStream().close();
                            }
                            session.getSession().getClipboardTransferThread().interrupt();
                            session.getSession().getClipboardTransferThread().join();
                            session.getSession().getConnection().resetClipboardStreams();
                            session.getSession().getClipboardTransferTask().setInputStream(connection.getGraphicsClipboardDataInputStream());
                            session.getSession().getClipboardTransferTask().setOutputStream(connection.getGraphicsClipboardDataOutputStream());
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_CLIPBOARD_CLEAR_REQUEST:
                        {
                            if (clipboard != null) {
                                try {
                                    clipboard.setContents(new SAWEmptyTransferable(), null);
                                } catch (Exception e) {
                                    return;
                                }
                            }
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_CAPTURE_MODE_PARTIAL:
                        {
                            writer.setScreenCaptureModeComplete(false);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_CAPTURE_MODE_COMPLETE:
                        {
                            writer.setScreenCaptureModeComplete(true);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_CAPTURE_AREA_CHANGE:
                        {
                            int x = connection.getGraphicsControlDataInputStream().readInt();
                            int y = connection.getGraphicsControlDataInputStream().readInt();
                            int width = connection.getGraphicsControlDataInputStream().readInt();
                            int height = connection.getGraphicsControlDataInputStream().readInt();
                            Rectangle area = new Rectangle();
                            area.x = x;
                            area.y = y;
                            area.width = width;
                            area.height = height;
                            writer.setCaptureArea(new Rectangle(area));
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_DIRECT_CODING:
                        {
                            writer.setDynamicCoding(false);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_DYNAMIC_CODING:
                        {
                            writer.setDynamicCoding(true);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_DEFAULT_DEVICE:
                        {
                            deviceNumber = 0;
                            GraphicsDevice defaultDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                            controlProvider.setGraphicsDevice(defaultDevice);
                            writer.setNextDevice(defaultDevice);
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_GRAPHICS_CHANGE_DEVICE:
                        {
                            GraphicsDevice[] devices = SAWUsableGraphicalDeviceResolver.getRasterDevices();
                            GraphicsDevice currentDevice = controlProvider.getGraphicsDevice();
                            GraphicsDevice nextDevice = null;
                            while (devices.length > 1 && (nextDevice == null || nextDevice.getIDstring().equals(currentDevice.getIDstring()))) {
                                deviceNumber++;
                                deviceNumber = deviceNumber % devices.length;
                                nextDevice = devices[deviceNumber];
                            }
                            if (nextDevice != null) {
                                controlProvider.setGraphicsDevice(nextDevice);
                                writer.setNextDevice(nextDevice);
                            }
                            break;
                        }
                    case SAW.SAW_GRAPHICS_MODE_SESSION_ENDING:
                        {
                            stopped = true;
                            break;
                        }
                    default:
                        {
                            stopped = true;
                            break;
                        }
                }
            } catch (IOException e) {
                stopped = true;
                break;
            } catch (Exception e) {
            }
        }
        releaseAllPressedKeys();
        synchronized (session) {
            session.notify();
        }
    }
