                @Override
                public int read() throws IOException {
                    if (offset < length) {
                        bytesRead++;
                        return monobuf[offset++] & 0xff;
                    }
                    length = stereoIn.read(monobuf);
                    if (length <= 0) {
                        logger.fine("reached EOF on original input stream (read " + length + " bytes)");
                        return -1;
                    }
                    for (int i = 0; i < length; i += 4) {
                        int lh, ll, rh, rl;
                        if (desiredFormat.isBigEndian()) {
                            lh = monobuf[i + 0];
                            ll = monobuf[i + 1] & 0xff;
                            rh = monobuf[i + 2];
                            rl = monobuf[i + 3] & 0xff;
                        } else {
                            lh = monobuf[i + 1];
                            ll = monobuf[i + 0] & 0xff;
                            rh = monobuf[i + 3];
                            rl = monobuf[i + 2] & 0xff;
                        }
                        int left = (lh << 8 | ll);
                        int right = (rh << 8 | rl);
                        int mixed = (left + right) / 2;
                        if (desiredFormat.isBigEndian()) {
                            monobuf[(i / 2) + 1] = (byte) (mixed & 0xff);
                            monobuf[(i / 2) + 0] = (byte) ((mixed >> 8) & 0xff);
                        } else {
                            monobuf[(i / 2) + 0] = (byte) (mixed & 0xff);
                            monobuf[(i / 2) + 1] = (byte) ((mixed >> 8) & 0xff);
                        }
                    }
                    length /= 2;
                    offset = 0;
                    return monobuf[offset++] & 0xff;
                }
