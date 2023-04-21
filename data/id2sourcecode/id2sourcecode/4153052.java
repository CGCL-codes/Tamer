        @Override
        public void run() {
            try {
                pw = new PrintWriter(s.getOutputStream(), true);
                br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                fis = new FileInputStream(RenderHost.this.scene);
                long fc = RenderHost.this.scene.length();
                pw.println(fc);
                while (fc-- > 0) {
                    s.getOutputStream().write(fis.read());
                }
                String[] clientConfig = br.readLine().split(",");
                int cores = Integer.valueOf(clientConfig[0]);
                int clock = Integer.valueOf(clientConfig[1]);
                rh.registerNode(id, clock, cores);
                while (!rh.nodesReady()) {
                    try {
                        Thread.sleep(sleepTime);
                        sleepTime <<= 1;
                    } catch (InterruptedException e) {
                        System.err.println("Thread interrupted!");
                        break;
                    }
                }
                RenderHost.this.sendWorkLoad(id, pw);
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                }
                int pnumber;
                int tp = lb.getPieces().length;
                while (pieceCount < tp) {
                    pnumber = 0;
                    try {
                        pnumber = Integer.valueOf(br.readLine());
                    } catch (NumberFormatException e) {
                        break;
                    }
                    int cols = LoadBalancer.getTileCount(w);
                    int vs = (pnumber / cols) * RayTracer.TILE_SIZE;
                    int ve = (pnumber / cols + 1) * RayTracer.TILE_SIZE;
                    int hs = (pnumber % cols) * RayTracer.TILE_SIZE;
                    int he = (pnumber % cols + 1) * RayTracer.TILE_SIZE;
                    ve = ve > h ? h : ve;
                    he = he > w ? w : he;
                    for (int q = vs; q < ve; ++q) {
                        String[] rowData = br.readLine().split(",");
                        for (int j = hs; j < he; ++j) {
                            String[] pixelData = rowData[j - hs].split("\\|");
                            for (int k = 0; k < 3; ++k) {
                                fb[j][q][k] = Float.valueOf(pixelData[k]);
                            }
                        }
                    }
                    fbw.writeSection(fb, hs, vs, he - hs, ve - vs);
                    pieceCount++;
                }
                long end = System.currentTimeMillis() - startTime;
                fbw.timeLabel.setText(end / 1000 + " secs");
                fbw.bufferUpdater.terminate();
                pw.close();
                br.close();
                s.close();
            } catch (IOException e) {
                rh.errorMsg = e.getMessage();
                rh.error = true;
            }
        }
