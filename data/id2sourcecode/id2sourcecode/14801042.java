    public void run() {
        System.out.println(Thread.currentThread().getName() + " Downloader Avviato da " + this.conn.getIPVicino() + ", " + this.conn.getPortaVicino());
        if (c.interested(conn.getBitfield())) {
            conn.setInteresseDown(true);
            conn.sendDown(new Messaggio(Messaggio.INTERESTED, null));
        } else {
            conn.setInteresseDown(false);
            conn.sendDown(new Messaggio(Messaggio.NOT_INTERESTED, null));
        }
        int count = 0;
        PIO p = null;
        while (true) {
            if (!this.c.getStato() || this.conn.getTermina()) {
                if (pendingRequest > PREQ) {
                    this.c.liberaPio(pendingRequest);
                }
                conn.sendDown(new Messaggio(Messaggio.CLOSE, null));
                break;
            }
            Messaggio m = this.conn.receiveDown();
            if (m == null) {
                this.failed++;
                continue;
            } else {
                this.failed = 0;
            }
            int tipo = m.getTipo();
            switch(tipo) {
                case Messaggio.HAVE:
                    {
                        this.conn.setArrayBitfield((int[]) m.getObj());
                        if (this.conn.getInteresseDown() == false) {
                            this.conn.setInteresseDown(this.c.interested(this.conn.getBitfield()));
                        }
                        break;
                    }
                case Messaggio.CHOKE:
                    {
                        this.conn.setStatoDown(Connessione.CHOKED);
                        break;
                    }
                case Messaggio.UNCHOKE:
                    {
                        this.conn.setStatoDown(Connessione.UNCHOKED);
                        break;
                    }
                case Messaggio.CLOSE:
                    {
                        break;
                    }
                case Messaggio.CHUNK:
                    {
                        count++;
                        Chunk chunk = (Chunk) m.getObj();
                        int offset = chunk.getOffset();
                        try {
                            byte[] stringa = c.getHash();
                            byte[] sha = new byte[Creek.DIMSHA];
                            int i = Creek.DIMSHA * offset;
                            for (int j = 0; j < Creek.DIMSHA; j++) {
                                sha[j] = stringa[i + j];
                            }
                            MessageDigest md = null;
                            try {
                                md = MessageDigest.getInstance("SHA-1");
                            } catch (NoSuchAlgorithmException ex) {
                                throw new ErrorException("No such Algorithm");
                            }
                            md.update(chunk.getData());
                            byte[] ris = new byte[Creek.DIMSHA];
                            ris = md.digest();
                            for (i = 0; i < ris.length; i++) {
                                if (ris[i] != sha[i]) {
                                    float temp = (float) c.getDimensione() / (float) BitCreekPeer.DIMBLOCCO;
                                    int dim = (int) Math.ceil(temp);
                                    if (chunk.getOffset() != dim - 1) {
                                        throw new ErrorException("SHA non corretto");
                                    }
                                }
                            }
                        } catch (ErrorException ex) {
                            int[] richiesta = new int[1];
                            richiesta[0] = chunk.getOffset();
                            conn.sendDown(new Messaggio(Messaggio.REQUEST, richiesta));
                            break;
                        }
                        try {
                            if (c.scriviChunk(chunk)) {
                                conn.incrDown();
                            }
                        } catch (NullPointerException ex) {
                            tipo = Messaggio.CLOSE;
                            break;
                        }
                        c.settaPerc();
                        if (count % 100 == 0) {
                            conn.ResetDown();
                        }
                        this.pendingRequest = PREQ;
                    }
            }
            if (tipo == Messaggio.CLOSE) {
                if (conn != null) {
                    conn.sendDown(new Messaggio(Messaggio.CLOSE, null));
                }
                break;
            }
            if (pendingRequest == PREQ && !endgame) {
                p = c.getNext(this.conn.getBitfield());
                if (p != null) {
                    int id = p.getId();
                    if (id == Downloader.ENDGAME) {
                        int[] ultimi = c.getLast();
                        this.endgame = true;
                        if (ultimi.length > 0) {
                            conn.sendDown(new Messaggio(Messaggio.REQUEST, ultimi));
                            this.pendingRequest = ultimi[0];
                        }
                    } else {
                        int[] toSend = new int[1];
                        toSend[0] = id;
                        conn.sendDown(new Messaggio(Messaggio.REQUEST, toSend));
                        this.pendingRequest = toSend[0];
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        System.err.println("Sono stato interrotto");
                    }
                }
            }
        }
        peer.decrConnessioni();
        c.decrPeer();
        if (p != null) {
            p.setFree();
        }
        this.conn.setSocketDown(null);
        System.out.println(Thread.currentThread().getName() + " Downloader Terminato, ho scaricato: " + this.conn.getDownloaded() + " Chunk");
    }
