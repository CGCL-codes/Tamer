    private void recvOrbresponse(Response rsp) throws OrbErrorException, IOException, EOFException {
        int i, size;
        if (rsp == null) return;
        sync(inBuf);
        rsp.what = inBuf.readInt();
        rsp.result = inBuf.readInt();
        switch(rsp.result) {
            case 0:
                switch(rsp.what) {
                    case ORBOPEN:
                        rsp.orbStart = inBuf.readDouble();
                        rsp.serverStart = inBuf.readDouble();
                        rsp.latestPktid = inBuf.readInt();
                        break;
                    case ORBCLOSE:
                    case ORBSETLOGGING:
                    case ORBHALT:
                    case ORBSETPRI:
                    case ORBKILL:
                        break;
                    case ORBSELECT:
                    case ORBREJECT:
                        rsp.nSelections = inBuf.readInt();
                        break;
                    case ORBSEEK:
                    case ORBAFTER:
                    case ORBBEFORE:
                    case ORBTELL:
                        rsp.pktid = inBuf.readInt();
                        break;
                    case ORBSTAT:
                        rsp.orbStat.maxdataBytes = inBuf.readInt();
                        rsp.orbStat.maxPkts = inBuf.readInt();
                        rsp.orbStat.maxSources = inBuf.readInt();
                        rsp.orbStat.nClients = inBuf.readInt();
                        rsp.orbStat.nSources = inBuf.readInt();
                        rsp.orbStat.orbstartedTime = inBuf.readDouble();
                        rsp.orbStat.whenTime = inBuf.readDouble();
                        rsp.orbStat.pid = inBuf.readInt();
                        rsp.orbStat.address[0] = inBuf.readByte();
                        rsp.orbStat.address[1] = inBuf.readByte();
                        rsp.orbStat.address[2] = inBuf.readByte();
                        rsp.orbStat.address[3] = inBuf.readByte();
                        rsp.orbStat.port = inBuf.readInt();
                        rsp.orbStat.opens = inBuf.readInt();
                        rsp.orbStat.closes = inBuf.readInt();
                        rsp.orbStat.rejected = inBuf.readInt();
                        rsp.orbStat.errors = inBuf.readInt();
                        rsp.orbStat.host = inbuf2str(ORBHOST_SIZE);
                        rsp.orbStat.who = inbuf2str(ORBWHO_SIZE);
                        rsp.orbStat.version = inbuf2str(ORBVERSION_SIZE);
                        rsp.orbStat.orbinitializedTime = inBuf.readDouble();
                        rsp.orbStat.messages = inBuf.readInt();
                        rsp.orbStat.connections = inBuf.readInt();
                        break;
                    case ORBSOURCES:
                        rsp.sourceWhen = inBuf.readDouble();
                        size = inBuf.readInt();
                        rsp.source = new OrbSource[size];
                        for (i = 0; i < size; i++) {
                            rsp.source[i] = new OrbSource();
                            rsp.source[i].whenTime = rsp.sourceWhen;
                            rsp.source[i].srcname = inbuf2str(ORBSRCNAME_SIZE);
                            rsp.source[i].active = inBuf.readInt();
                            rsp.source[i].oldestPktid = inBuf.readInt();
                            rsp.source[i].latestPktid = inBuf.readInt();
                            rsp.source[i].npktsOrb = inBuf.readInt();
                            rsp.source[i].nbytesOrb = inBuf.readInt();
                            rsp.source[i].oldestTime = inBuf.readDouble();
                            rsp.source[i].latestTime = inBuf.readDouble();
                        }
                        break;
                    case ORBCLIENTS:
                        rsp.clientWhen = inBuf.readDouble();
                        size = inBuf.readInt();
                        rsp.client = new OrbClient[size];
                        for (i = 0; i < size; i++) {
                            rsp.client[i] = new OrbClient();
                            rsp.client[i].whenTime = rsp.clientWhen;
                            rsp.client[i].started = inBuf.readDouble();
                            rsp.client[i].lastpktTime = inBuf.readDouble();
                            rsp.client[i].currentPktid = inBuf.readInt();
                            rsp.client[i].packetsTransferred = inBuf.readInt();
                            rsp.client[i].bytesTransferred = inBuf.readInt();
                            rsp.client[i].bytesRead = inBuf.readInt();
                            rsp.client[i].bytesWritten = inBuf.readInt();
                            rsp.client[i].nreads = inBuf.readInt();
                            rsp.client[i].nwrites = inBuf.readInt();
                            rsp.client[i].nrequests = inBuf.readInt();
                            rsp.client[i].messages = inBuf.readInt();
                            rsp.client[i].lastRequest = inBuf.readInt();
                            rsp.client[i].priority = inBuf.readInt();
                            rsp.client[i].errors = inBuf.readInt();
                            rsp.client[i].select = inbuf2strn(SELECT_MAX);
                            rsp.client[i].reject = inbuf2strn(SELECT_MAX);
                            rsp.client[i].fd = inBuf.readInt();
                            rsp.client[i].thread = inBuf.readInt();
                            rsp.client[i].address[0] = inBuf.readByte();
                            rsp.client[i].address[1] = inBuf.readByte();
                            rsp.client[i].address[2] = inBuf.readByte();
                            rsp.client[i].address[3] = inBuf.readByte();
                            rsp.client[i].port = inBuf.readInt();
                            rsp.client[i].who = inbuf2str(ORBWHO_SIZE);
                            rsp.client[i].host = inbuf2str(ORBHOST_SIZE);
                            rsp.client[i].what = inbuf2str(ORBWHAT_SIZE);
                            rsp.client[i].pid = inBuf.readInt();
                            rsp.client[i].permission = inbuf2str(1);
                        }
                        break;
                    case ORBREAP:
                        rsp.pkg.pktid = inBuf.readInt();
                        rsp.pkg.pktsize = inBuf.readUnsignedShort();
                        int srcsize = inBuf.readUnsignedShort();
                        if (srcsize > ORBSRCNAME_SIZE) srcsize = ORBSRCNAME_SIZE;
                        rsp.pkg.time = inBuf.readDouble();
                        rsp.pkg.srcname = new SourceName(inbuf2str(srcsize));
                        if (rsp.pkg.pktsize > 0) {
                            if (rsp.pkg.packet == null || rsp.pkg.packet.length < rsp.pkg.pktsize) {
                                System.out.println("Resizing to " + rsp.pkg.pktsize + " bytes");
                                rsp.pkg.packet = new byte[rsp.pkg.pktsize];
                            }
                            inBuf.readFully(rsp.pkg.packet, 0, rsp.pkg.pktsize);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal response value");
                }
                break;
            default:
                rsp.errMsg = inbuf2strn(512);
                throw new OrbErrorException(rsp.errMsg);
        }
    }
