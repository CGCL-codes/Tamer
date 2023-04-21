    public void buildMET2(int bs) {
        ArrayList<Neighbour> nb;
        if (bs > this.wsn.size()) return;
        dist_BS[bs] = 0;
        boolean complete = false;
        int index = 0, index_tmp;
        int dist_tmp[] = new int[GlobalInfo.NoNodes];
        int link2BS[] = new int[GlobalInfo.NoNodes];
        int dist_wait[] = new int[GlobalInfo.NoNodes];
        int link_wait[] = new int[GlobalInfo.NoNodes];
        scan = new int[GlobalInfo.NoNodes];
        int done[] = new int[GlobalInfo.NoNodes];
        int done2[] = new int[GlobalInfo.NoNodes];
        for (int i = 0; i < GlobalInfo.NoNodes; i++) {
            dist_tmp[i] = GlobalInfo.WirelessRange;
            done[i] = 0;
            done2[i] = 0;
            link2BS[i] = GlobalInfo.NoNodes;
            dist_wait[i] = GlobalInfo.WirelessRange;
            link_wait[i] = GlobalInfo.NoNodes;
        }
        scan[0] = bs;
        link2BS[bs] = bs;
        done[bs] = 1;
        done2[bs] = 1;
        index_tmp = 1;
        int dim = 0;
        while (!complete) {
            wsn.get(scan[index]).orderNeighbour_LinkSize();
            for (int j = 0; j < wsn.get(scan[index]).neighborood.size(); j++) {
                dim = wsn.get(wsn.get(scan[index]).neighborood.get(j).getId()).neighborood.size() - 1;
                if (dim % 2 == 0) dim = dim / 2; else dim = (dim + 1) / 2;
                if (index == 0) {
                    if (wsn.get(scan[index]).neighborood.get(j).getLinkSize() <= wsn.get(wsn.get(scan[index]).neighborood.get(j).getId()).neighborood.get(dim).getLinkSize()) {
                        link2BS[wsn.get(scan[index]).neighborood.get(j).getId()] = bs;
                        dist_BS[wsn.get(scan[index]).neighborood.get(j).getId()] = 1;
                        dist_tmp[wsn.get(scan[index]).neighborood.get(j).getId()] = wsn.get(scan[index]).neighborood.get(j).getLinkSize();
                        wsn.get(wsn.get(scan[index]).neighborood.get(j).getId()).setRange(dist_tmp[wsn.get(scan[index]).neighborood.get(j).getId()]);
                        done[wsn.get(scan[index]).neighborood.get(j).getId()]++;
                        if (done[wsn.get(scan[index]).neighborood.get(j).getId()] == 1) {
                            scan[index_tmp] = wsn.get(scan[index]).neighborood.get(j).getId();
                            index_tmp++;
                        }
                    } else {
                        if (dist_wait[wsn.get(scan[index]).neighborood.get(j).getId()] >= wsn.get(scan[index]).neighborood.get(j).getLinkSize()) {
                            dist_wait[wsn.get(scan[index]).neighborood.get(j).getId()] = wsn.get(scan[index]).neighborood.get(j).getLinkSize();
                            link_wait[wsn.get(scan[index]).neighborood.get(j).getId()] = scan[index];
                        }
                    }
                } else if (index > 0) {
                    if (wsn.get(scan[index]).neighborood.get(j).getLinkSize() <= wsn.get(wsn.get(scan[index]).neighborood.get(j).getId()).neighborood.get(dim).getLinkSize()) {
                        if (dist_BS[wsn.get(scan[index]).ID] < dist_BS[wsn.get(scan[index]).neighborood.get(j).getId()]) {
                            if (dist_BS[wsn.get(scan[index]).neighborood.get(j).getId()] == (int) GlobalInfo.WirelessRange) {
                                link2BS[wsn.get(scan[index]).neighborood.get(j).getId()] = wsn.get(scan[index]).ID;
                                dist_BS[wsn.get(scan[index]).neighborood.get(j).getId()] = dist_BS[wsn.get(scan[index]).ID] + 1;
                                dist_tmp[wsn.get(scan[index]).neighborood.get(j).getId()] = wsn.get(scan[index]).neighborood.get(j).getLinkSize();
                                wsn.get(wsn.get(scan[index]).neighborood.get(j).getId()).setRange(dist_tmp[wsn.get(scan[index]).neighborood.get(j).getId()]);
                                done[wsn.get(scan[index]).neighborood.get(j).getId()]++;
                                if (done[wsn.get(scan[index]).neighborood.get(j).getId()] == 1) {
                                    scan[index_tmp] = wsn.get(scan[index]).neighborood.get(j).getId();
                                    index_tmp++;
                                }
                            } else if (dist_tmp[wsn.get(scan[index]).neighborood.get(j).getId()] > wsn.get(scan[index]).neighborood.get(j).getLinkSize()) {
                                link2BS[wsn.get(scan[index]).neighborood.get(j).getId()] = wsn.get(scan[index]).ID;
                                dist_BS[wsn.get(scan[index]).neighborood.get(j).getId()] = dist_BS[wsn.get(scan[index]).ID] + 1;
                                dist_tmp[wsn.get(scan[index]).neighborood.get(j).getId()] = wsn.get(scan[index]).neighborood.get(j).getLinkSize();
                                wsn.get(wsn.get(scan[index]).neighborood.get(j).getId()).setRange(dist_tmp[wsn.get(scan[index]).neighborood.get(j).getId()]);
                                done[wsn.get(scan[index]).neighborood.get(j).getId()]++;
                                if (done[wsn.get(scan[index]).neighborood.get(j).getId()] == 1) {
                                    scan[index_tmp] = wsn.get(scan[index]).neighborood.get(j).getId();
                                    index_tmp++;
                                }
                            }
                        }
                    } else {
                        if (dist_wait[wsn.get(scan[index]).neighborood.get(j).getId()] >= wsn.get(scan[index]).neighborood.get(j).getLinkSize()) {
                            dist_wait[wsn.get(scan[index]).neighborood.get(j).getId()] = wsn.get(scan[index]).neighborood.get(j).getLinkSize();
                            link_wait[wsn.get(scan[index]).neighborood.get(j).getId()] = scan[index];
                        }
                    }
                }
            }
            index++;
            if (index == GlobalInfo.NoNodes || index == index_tmp) complete = true;
        }
        System.out.print("\n-----\n  ");
        for (int i = 0; i < GlobalInfo.NoNodes; i++) {
            System.out.print(done[i] + " (" + scan[i] + ")  ");
            if (i != 0 && i % 25 == 0) System.out.println();
        }
        System.out.print("\n-----\n  ");
        int count = 0;
        int[] todo = new int[GlobalInfo.NoNodes];
        count = 0;
        for (int i = 0; i < GlobalInfo.NoNodes; i++) if (done[i] == 0) count++;
        {
            while (count > 0) {
                for (int i = 0; i < GlobalInfo.NoNodes; i++) {
                    if (done[i] == 0) {
                        if (link_wait[i] != GlobalInfo.NoNodes && dist_BS[i] == GlobalInfo.WirelessRange) {
                            link2BS[i] = link_wait[i];
                            dist_BS[i] = dist_BS[link_wait[i]] + 1;
                            dist_tmp[i] = dist_wait[i];
                            wsn.get(i).setRange(dist_tmp[i]);
                            done[i]++;
                            count++;
                            int nindex = 0;
                            for (int j = 0; j < wsn.get(i).neighborood.size(); j++) {
                                dim = wsn.get(wsn.get(i).neighborood.get(j).getId()).neighborood.size() - 1;
                                if (dim % 2 == 0) dim = dim / 2; else dim = (dim + 1) / 2;
                                if (wsn.get(i).neighborood.get(j).getLinkSize() <= wsn.get(wsn.get(i).neighborood.get(j).getId()).neighborood.get(dim).getLinkSize()) {
                                    if (dist_BS[i] < dist_BS[wsn.get(i).neighborood.get(j).getId()]) {
                                        nindex = wsn.get(i).neighborood.get(j).getId();
                                        link2BS[nindex] = i;
                                        dist_BS[nindex] = dist_BS[i] + 1;
                                        dist_tmp[nindex] = wsn.get(i).neighborood.get(j).getLinkSize();
                                        wsn.get(wsn.get(i).neighborood.get(j).getId()).setRange(dist_tmp[nindex]);
                                    }
                                } else {
                                    if (dist_wait[wsn.get(i).neighborood.get(j).getId()] >= wsn.get(i).neighborood.get(j).getLinkSize()) {
                                        dist_wait[wsn.get(i).neighborood.get(j).getId()] = wsn.get(i).neighborood.get(j).getLinkSize();
                                        link_wait[wsn.get(i).neighborood.get(j).getId()] = i;
                                    }
                                }
                            }
                        }
                    }
                }
                count--;
            }
            count = 0;
            for (int i = 0; i < GlobalInfo.NoNodes; i++) if (done[i] == 0) count++;
            while (count > 0) {
                for (int i = 0; i < GlobalInfo.NoNodes; i++) {
                    if (done[i] == 0) {
                        if (dist_BS[i] != GlobalInfo.WirelessRange) {
                            done[i]++;
                            int nindex = 0;
                            for (int j = 0; j < wsn.get(i).neighborood.size(); j++) {
                                dim = wsn.get(wsn.get(i).neighborood.get(j).getId()).neighborood.size() - 1;
                                if (dist_BS[i] < dist_BS[wsn.get(i).neighborood.get(j).getId()]) {
                                    nindex = wsn.get(i).neighborood.get(j).getId();
                                    link2BS[nindex] = i;
                                    dist_BS[nindex] = dist_BS[i] + 1;
                                    dist_tmp[nindex] = wsn.get(i).neighborood.get(j).getLinkSize();
                                    wsn.get(wsn.get(i).neighborood.get(j).getId()).setRange(dist_tmp[nindex]);
                                }
                            }
                        }
                    }
                }
                count--;
            }
            System.out.println();
            int count2 = 0;
            for (int i = 0; i < GlobalInfo.NoNodes; i++) {
                System.out.print(done[i] + "  ");
                if (i != 0 && i % 25 == 0) System.out.println();
                if (done[i] == 0) count2++;
            }
            System.out.println("\n----++++----");
            System.out.println(count2);
            System.out.println("++++----++++");
            for (int i = 0; i < GlobalInfo.NoNodes; i++) {
                nb = new ArrayList<Neighbour>(1);
                if (done[i] != 0) nb.add(new Neighbour(dist_tmp[i], link2BS[i]));
                wsn.get(i).setNeighborood(nb);
                this.network.set(i, this.wsn.get(i));
                if (done[i] != 0) done2[link2BS[i]]++;
            }
            double costo_rete = 0, costo_medio = 0, costo_max = 0;
            double fissa = 0.1;
            double variabile = 0.1;
            double datafusion = 0.01;
            double epsonK = 0.0002;
            double valori[] = new double[GlobalInfo.NoNodes];
            for (int i = 0; i < GlobalInfo.NoNodes; i++) {
                valori[i] = (double) (fissa * done2[i] + datafusion + variabile + epsonK * dist_tmp[i] * dist_tmp[i]);
                costo_rete += valori[i];
                if (i != 0 && valori[i] > costo_max) costo_max = valori[i];
            }
            costo_medio = costo_rete / GlobalInfo.NoNodes;
            this.infoBattery = new String[3];
            this.infoBattery[0] = "MAX " + costo_max;
            this.infoBattery[1] = "AVG " + costo_medio;
            this.infoBattery[2] = "Net " + costo_rete;
            System.out.println("------------------------");
            System.out.println("MAX " + costo_max);
            System.out.println("MEDIO " + costo_medio);
            System.out.println("RETE " + costo_rete);
        }
    }
