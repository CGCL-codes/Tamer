    public SuperColliderPlayer(final Session doc, final Server server, RoutingConfig oCfg) throws IOException {
        this.server = server;
        this.doc = doc;
        final AudioTrail at = doc.getAudioTrail();
        final Runnable runTrigger;
        final SynthDef[] defs;
        OSCBundle bndl;
        transport = doc.getTransport();
        nw = NodeWatcher.newFrom(server);
        numInputChannels = at.getChannelNum();
        channelMaps = at.getChannelMaps();
        sourceRate = doc.timeline.getRate();
        serverRate = server.getSampleRate();
        DISKBUF_SIZE = (Math.max(44100, (int) sourceRate) + DISKBUF_PAD) << 1;
        DISKBUF_SIZE_H = DISKBUF_SIZE >> 1;
        DISKBUF_SIZE_HM = DISKBUF_SIZE_H - DISKBUF_PAD;
        bndl = new OSCBundle();
        grpRoot = Group.basicNew(server);
        grpRoot.setName("Root-" + doc.getName());
        nw.register(grpRoot);
        bndl.addPacket(grpRoot.addToHeadMsg(server.getDefaultGroup()));
        grpInput = Group.basicNew(server);
        grpInput.setName("Input");
        nw.register(grpInput);
        bndl.addPacket(grpInput.addToTailMsg(grpRoot));
        grpOutput = Group.basicNew(server);
        grpOutput.setName("Output");
        nw.register(grpOutput);
        bndl.addPacket(grpOutput.addToTailMsg(grpRoot));
        bndl.addPacket(grpOutput.runMsg(false));
        server.sendBundle(bndl);
        busPhasor = Bus.audio(server);
        runTrigger = new Runnable() {

            public void run() {
                final OSCMessage msg = trigMsg;
                final int nodeID = ((Number) msg.getArg(0)).intValue();
                final int nextClock, fill, bufOff;
                final long pos, start;
                final OSCBundle bndl2;
                final int even;
                final Span[] bufSpans;
                int numCh;
                try {
                    clock = ((Number) msg.getArg(2)).intValue();
                    nextClock = clock + 1;
                    even = nextClock & 1;
                    bndl2 = new OSCBundle();
                    if ((ct == null) || (ct.synthPhasor == null) || (nodeID != ct.synthPhasor.getNodeID())) return;
                    if (trigNodeID == -1) return;
                    pos = nextClock * DISKBUF_SIZE_HM - ((1 - even) * DISKBUF_PAD) + playOffset;
                    start = Math.max(0, pos);
                    fill = (int) (start - pos);
                    bufOff = even * DISKBUF_SIZE_H;
                    if (fill > 0) {
                        for (int j = 0; j < ct.bufsDisk.length; j++) {
                            numCh = ct.bufsDisk[j].getNumChannels();
                            bndl2.addPacket(ct.bufsDisk[j].fillMsg(bufOff * numCh, fill * numCh, 0.0f));
                        }
                    }
                    bufSpans = transport.foldSpans(new Span(start, pos + DISKBUF_SIZE_H), MIN_LOOP_LEN);
                    doc.getAudioTrail().addBufferReadMessages(bndl2, bufSpans, ct.bufsDisk, bufOff + fill);
                    lastBufSpans[even] = bufSpans;
                    if (DEBUG_FOLD) {
                        System.out.println("------C " + nextClock + ", " + even + ", " + playOffset + ", " + pos);
                        for (int k = 0, m = bufOff + fill; k < bufSpans.length; k++) {
                            System.out.println("i = " + k + "; " + bufSpans[k] + " -> " + m);
                            m += bufSpans[k].getLength();
                        }
                        System.out.println();
                    }
                    if (!server.sync(bndl2, TIMEOUT)) {
                        printTimeOutMsg("bufUpdate");
                    }
                } catch (IOException e1) {
                    printError("Receive /tr", e1);
                } catch (ClassCastException e2) {
                    printError("Receive /tr", e2);
                }
            }
        };
        trigResp = new OSCResponderNode(server, "/tr", new OSCListener() {

            public void messageReceived(OSCMessage msg, SocketAddress sender, long time) {
                final int nodeID = ((Number) msg.getArg(0)).intValue();
                if (nodeID == trigNodeID) {
                    trigMsg = msg;
                    EventQueue.invokeLater(runTrigger);
                }
            }
        });
        defs = createInputDefs(channelMaps);
        if (defs != null) {
            bndl = new OSCBundle();
            for (int i = 0; i < defs.length; i++) {
                bndl.addPacket(defs[i].recvMsg());
            }
            if (!server.sync(bndl, TIMEOUT)) {
                printTimeOutMsg("defs");
            }
        }
        srcFactor = sourceRate / serverRate;
        updateSRC();
        setOutputConfig(oCfg);
        syncInput = new GroupSync();
        syncOutput = new GroupSync();
        transport.addTransportListener(this);
        doc.audioTracks.addListener(this);
        doc.timeline.addTimelineListener(this);
        osc = new OSCRouterWrapper(doc, this);
    }
