            private synchronized void init() {
                if (!inited) {
                    try {
                        outStream = new FileOutputStream(file);
                        inStream = new FileInputStream(file);
                        outChannel = outStream.getChannel();
                        inChannel = inStream.getChannel();
                    } catch (FileNotFoundException foe) {
                        Logging.errorln("IOCacheArray constuctor error: Could not open file " + file + ".  Exception " + foe);
                        Logging.errorln("outStream " + outStream + "  inStream " + inStream + "  outchan " + outChannel + "  inchannel " + inChannel);
                    }
                }
                inited = true;
            }
