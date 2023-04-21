    public void run() {
        try {
            while (s.isConnected()) try {
                lock.lock();
                short command = stream.readOpCode();
                if (command == 0) break;
                if (authenticated) {
                    String hash = getHash();
                    switch(command) {
                        case OPCodes.SET_TB_WIDTH:
                            int width = stream.readInt();
                            stream.writeBoolean(h.setThumbnailWidth(width));
                            break;
                        case OPCodes.COMMIT:
                            stream.writeString(hash);
                            String path = stream.readString();
                            int count = stream.readInt();
                            log("Read " + count + " images (" + hash + ")");
                            for (int i = 0; i < count; i++) {
                                if (!stream.readBoolean()) break;
                                String name = stream.readString();
                                h.storeImage(path, name, stream.readDataFully());
                            }
                            lock.unlock();
                            break;
                        case OPCodes.GET_INFO:
                            path = stream.readString();
                            log("Get image infos for " + path);
                            Map<String, String> infos = h.getImageInfos(path);
                            stream.writeInt(infos.size());
                            for (String key : infos.keySet()) {
                                stream.writeString(key);
                                stream.writeString(infos.get(key));
                            }
                            break;
                        case OPCodes.GET_COMMENT:
                            path = stream.readString();
                            log("Get comment for " + path);
                            stream.writeString(h.getComment(path));
                            break;
                        case OPCodes.SET_COMMENT:
                            path = stream.readString();
                            String comment = stream.readString();
                            log("Set comment for " + path + " to " + comment);
                            h.setComment(path, comment);
                            break;
                        case OPCodes.LIST_DIRECTORIES:
                            path = stream.readString();
                            log("List subdirectories of " + path);
                            List<String> entries = h.listEntries(path);
                            stream.writeInt(entries.size());
                            for (int i = 0; i < entries.size(); i++) {
                                stream.writeString(entries.get(i));
                            }
                            lock.unlock();
                            break;
                        case OPCodes.LIST_IMAGES:
                            path = stream.readString();
                            log("List images in " + path);
                            entries = h.listImages(path);
                            stream.writeInt(entries.size());
                            for (int i = 0; i < entries.size(); i++) {
                                stream.writeString(entries.get(i));
                            }
                            lock.unlock();
                            break;
                        case OPCodes.GET_IMAGE:
                            path = stream.readString();
                            width = stream.readInt();
                            log("Get image for " + path);
                            Pair<JImage, byte[]> p = h.getImage(path, width, false);
                            long time = System.currentTimeMillis();
                            stream.writeDataFully(p.t);
                            lock.unlock();
                            log("Time to transfer " + path + ": " + (System.currentTimeMillis() - time) + "ms");
                            break;
                        case OPCodes.GET_IMAGES:
                            stream.writeString(hash);
                            count = stream.readInt();
                            log("Read " + count + " images (" + hash + ")");
                            for (int i = 0; i < count; i++) {
                                path = stream.readString();
                                p = h.getImage(path, -1, true);
                                if (!stream.readBoolean()) break;
                                if (p == null) stream.writeDataFully(new byte[] {}); else stream.writeDataFully(p.t);
                            }
                            lock.unlock();
                            break;
                        case OPCodes.GET_THUMBNAIL:
                            path = stream.readString();
                            log("Get thumbnail for " + path);
                            p = h.getThumbnail(path);
                            stream.writeDataFully(p.t);
                            lock.unlock();
                            break;
                        case OPCodes.GET_THUMBNAILS:
                            count = stream.readInt();
                            String[] images = new String[count];
                            for (int i = 0; i < count; i++) {
                                images[i] = stream.readString();
                            }
                            stream.writeString(hash);
                            log("Read " + images.length + " thumbnails (" + hash + ")");
                            ThumbnailLoader tl = new ThumbnailLoader(hash, images);
                            readers.put(hash, tl);
                            tl.run();
                            lock.unlock();
                            break;
                        case OPCodes.STOP_READER:
                            hash = stream.readString();
                            lock.unlock();
                            if (readers.containsKey(hash)) {
                                readers.get(hash).running = false;
                            }
                            break;
                        case OPCodes.CREATE_DIRECTORY:
                            path = stream.readString();
                            lock.unlock();
                            log("Create new directory " + path);
                            stream.writeBoolean(h.createDirectory(path));
                            break;
                        case OPCodes.RENAME:
                            String pathOld = stream.readString();
                            String pathNew = stream.readString();
                            log("Rename " + pathOld + " to " + pathNew);
                            stream.writeBoolean(h.rename(pathOld, pathNew));
                            lock.unlock();
                            break;
                        case OPCodes.ROTATE:
                            path = stream.readString();
                            boolean clockwise = stream.readBoolean();
                            log("Rotate " + path + (clockwise ? " clockwise" : " counter clockwise"));
                            byte[] data = h.rotateOrFlip(path, true, clockwise);
                            stream.writeDataFully(data);
                            lock.unlock();
                            break;
                        case OPCodes.FLIP:
                            path = stream.readString();
                            boolean horizontal = stream.readBoolean();
                            log("Flip " + path + (horizontal ? " horizontal" : " vertical"));
                            data = h.rotateOrFlip(path, false, horizontal);
                            stream.writeDataFully(data);
                            lock.unlock();
                            break;
                        case OPCodes.GET_READ_ONLY:
                            stream.writeBoolean(c.getValidator().getUserCanWrite(user));
                            break;
                    }
                } else if (command == OPCodes.LOGIN) {
                    user = stream.readString();
                    log("User " + user + " tries checkin!");
                    boolean userValid = c.isValidUsername(user);
                    stream.writeBoolean(userValid);
                    if (userValid) {
                        byte[] random = createRString(512).getBytes();
                        stream.writeDataFully(random);
                        byte[] encrypted = stream.readDataFully();
                        authenticated = c.getValidator().handleChallenge(user, random, encrypted);
                        if (authenticated) {
                            h.setAllowedDirectories(c.getValidator().getAllowedRegex(user));
                            boolean ro = c.getValidator().getUserCanWrite(user);
                            h.setReadOnly(ro);
                            log("User " + user + " has " + (ro ? "read-only permissions." : "write permissions."));
                        }
                    }
                    log("User " + user + (authenticated ? " authenticated successfully." : " failed to authenticate."));
                    stream.writeBoolean(authenticated);
                    lock.unlock();
                }
            } catch (WrapperException e) {
                e.printStackTrace();
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                log(s.getInetAddress() + " disconnected");
                stream.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (ChalkListener cl : c.listeners) {
                cl.clientLeft();
            }
        }
    }
