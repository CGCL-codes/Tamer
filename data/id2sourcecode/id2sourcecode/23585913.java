    public void imputeABH() {
        imputedSnps = new byte[numberOfTaxa][numberOfSnps];
        int minCallsPerPopulation = 15;
        HashMap<String, Integer> popCountMap = new HashMap<String, Integer>();
        for (String pop : popmap.keySet()) {
            popCountMap.put(pop, 0);
        }
        System.out.println("Making initial ABH calls.");
        for (int s = 0; s < numberOfSnps; s++) {
            byte[][] popAlleles = new byte[numberOfPopulations][2];
            int popcount = 0;
            ArrayList<Integer> segList = new ArrayList<Integer>();
            for (Map.Entry<String, ArrayList<Integer>> ent : popmap.entrySet()) {
                boolean[] segsites = segregatingSites.get(ent.getKey());
                int[] allelecounts = new int[16];
                for (Integer taxon : ent.getValue()) {
                    allelecounts[snps[taxon][s]]++;
                    imputedSnps[taxon][s] = N;
                }
                int high = allelecounts[0];
                int next = 0;
                byte major = A;
                byte minor = X;
                for (int i = 1; i < 4; i++) {
                    if (allelecounts[i] > high) {
                        next = high;
                        high = allelecounts[i];
                        minor = major;
                        major = (byte) i;
                    } else if (allelecounts[i] > next) {
                        next = allelecounts[i];
                        minor = (byte) i;
                    }
                }
                if (high + next < minCallsPerPopulation) {
                    popAlleles[popcount][0] = X;
                    popAlleles[popcount][1] = X;
                    segsites[s] = false;
                } else {
                    double pval = Probability.binomial(next, high + next, 0.5);
                    if (pval < .01) {
                        popAlleles[popcount][0] = major;
                        popAlleles[popcount][1] = X;
                        segsites[s] = false;
                    } else {
                        popAlleles[popcount][0] = major;
                        popAlleles[popcount][1] = minor;
                        segsites[s] = true;
                    }
                }
                popcount++;
            }
            byte B73 = X;
            byte notB73 = X;
            boolean consistent = true;
            int nonsegpops = 0;
            int notcallable = 0;
            for (int p = 0; p < numberOfPopulations; p++) {
                if (popAlleles[p][0] == X && popAlleles[p][1] == X) notcallable++;
                if (popAlleles[p][0] == X || popAlleles[p][1] == X) nonsegpops++;
            }
            if (nonsegpops == 0 && notcallable < numberOfPopulations) {
                consistent = false;
            }
            for (int p = 0; p < numberOfPopulations; p++) if (consistent) {
                if (popAlleles[p][1] == X && popAlleles[p][0] != X) {
                    if (B73 == X) B73 = popAlleles[p][0]; else if (B73 != popAlleles[p][0]) consistent = false;
                }
            }
            if (consistent && B73 != X) {
                for (byte[] alleles : popAlleles) {
                    if (alleles[0] == X) {
                    } else if (alleles[0] != B73) {
                        if (notB73 == X) notB73 = alleles[0]; else if (notB73 != alleles[0]) consistent = false;
                    } else if (alleles[1] != X) {
                        if (notB73 == X) notB73 = alleles[1]; else if (notB73 != alleles[1]) consistent = false;
                    }
                }
            }
            if (consistent && notB73 != X && B73 != X) {
                b73Allele[s] = B73;
                nonb73Allele[s] = notB73;
                byte het = hetcodes[B73][notB73];
                for (String pop : popmap.keySet()) {
                    if (segregatingSites.get(pop)[s]) {
                        popCountMap.put(pop, popCountMap.get(pop) + 1);
                        ArrayList<Integer> taxaList = popmap.get(pop);
                        for (Integer t : taxaList) {
                            if (snps[t][s] == B73) imputedSnps[t][s] = A; else if (snps[t][s] == notB73) imputedSnps[t][s] = B; else if (snps[t][s] == het) imputedSnps[t][s] = H;
                        }
                    }
                }
            }
        }
        System.out.println("Checking snps against enclosing haplotypes.");
        boolean[] checkSite = new boolean[numberOfSnps];
        for (int s = 0; s < numberOfSnps; s++) checkSite[s] = false;
        for (boolean[] segsites : segregatingSites.values()) {
            for (int s = 0; s < numberOfSnps; s++) {
                checkSite[s] = checkSite[s] || segsites[s];
            }
        }
        for (int s = 0; s < numberOfSnps; s++) {
            if (!checkSite[s]) {
                for (int t = 0; t < numberOfTaxa; t++) imputedSnps[t][s] = N;
            }
        }
        imputeSnpsBasedOnHaplotypes(0.85);
        System.out.println("pop,A,B,N,total");
        for (String pop : popmap.keySet()) {
            ArrayList<Integer> taxaList = popmap.get(pop);
            int Acount = 0;
            int Bcount = 0;
            int Ncount = 0;
            int total = 0;
            for (Integer t : taxaList) {
                for (int s = 0; s < numberOfSnps; s++) {
                    if (imputedSnps[t][s] == A) Acount++; else if (imputedSnps[t][s] == B) Bcount++; else if (imputedSnps[t][s] == N) Ncount++;
                    total++;
                }
            }
            System.out.println(pop + "," + Acount + "," + Bcount + "," + Ncount + "," + total);
        }
        System.out.println("Identifying errors.");
        int hapsize = 6;
        for (String popname : popmap.keySet()) {
            ArrayList<Integer> taxaList = popmap.get(popname);
            for (Integer t : taxaList) {
                byte[] taxonSnps = imputedSnps[t];
                int nonmissCount = 0;
                for (byte snp : taxonSnps) {
                    if (snp != N) nonmissCount++;
                }
                if (nonmissCount < 10) {
                    System.out.println(nonmissCount + " non-missing data points for " + taxanames.get(t));
                    for (int s = 0; s < numberOfSnps; s++) imputedSnps[t][s] = N;
                } else {
                    int[] nonmissIndex = new int[nonmissCount];
                    int count = 0;
                    for (int s = 0; s < numberOfSnps; s++) {
                        if (taxonSnps[s] != N) nonmissIndex[count++] = s;
                    }
                    int[] leftcount = new int[nonmissCount];
                    leftcount[0] = 1;
                    for (int s = 1; s < nonmissCount; s++) {
                        if (taxonSnps[nonmissIndex[s]] == taxonSnps[nonmissIndex[s - 1]]) leftcount[s] = leftcount[s - 1] + 1; else leftcount[s] = 1;
                    }
                    int[] rightcount = new int[nonmissCount];
                    rightcount[nonmissCount - 1] = 1;
                    for (int s = nonmissCount - 2; s >= 0; s--) {
                        if (taxonSnps[nonmissIndex[s]] == taxonSnps[nonmissIndex[s + 1]]) rightcount[s] = rightcount[s + 1] + 1; else rightcount[s] = 1;
                    }
                    for (int s = 0; s < nonmissCount; s++) {
                        if (leftcount[s] == 1 && rightcount[s] == 1) {
                            int sIndex = nonmissIndex[s];
                            if (s == 0) {
                                if (rightcount[s + 1] >= hapsize) taxonSnps[sIndex] = N;
                            } else if (s <= hapsize) {
                                if (rightcount[s + 1] >= hapsize && leftcount[s - 1] == s) taxonSnps[sIndex] = N;
                            } else if (s == nonmissCount - 1) {
                                if (leftcount[s - 1] >= hapsize) taxonSnps[sIndex] = N;
                            } else if (s >= nonmissCount - hapsize - 1) {
                                if (leftcount[s - 1] >= hapsize && rightcount[s + 1] == nonmissCount - s) taxonSnps[sIndex] = N;
                            } else {
                                if (leftcount[s - 1] >= hapsize && rightcount[s + 1] >= hapsize) taxonSnps[sIndex] = N;
                            }
                        }
                        if (s < nonmissCount - 1 && leftcount[s] == 1 && rightcount[s] == 2 && leftcount[s + 1] == 2 && rightcount[s + 1] == 1) {
                            int sIndexLeft = nonmissIndex[s];
                            int sIndexRight = nonmissIndex[s + 1];
                            if (snpPositions[sIndexRight] - snpPositions[sIndexLeft] < 128) {
                                if (s == 0) {
                                    if (rightcount[s + 2] >= hapsize) {
                                        taxonSnps[sIndexRight] = N;
                                        taxonSnps[sIndexLeft] = N;
                                    }
                                } else if (s <= hapsize) {
                                    if (rightcount[s + 2] >= hapsize && leftcount[s - 1] == s) {
                                        taxonSnps[sIndexRight] = N;
                                        taxonSnps[sIndexLeft] = N;
                                    }
                                } else if (s == nonmissCount - 2) {
                                    if (leftcount[s - 1] >= hapsize) {
                                        taxonSnps[sIndexRight] = N;
                                        taxonSnps[sIndexLeft] = N;
                                    }
                                } else if (s >= nonmissCount - hapsize - 2) {
                                    if (leftcount[s - 1] >= hapsize && rightcount[s + 2] == nonmissCount - s - 1) {
                                        taxonSnps[sIndexRight] = N;
                                        taxonSnps[sIndexLeft] = N;
                                    }
                                } else {
                                    if (leftcount[s - 1] >= hapsize && rightcount[s + 2] >= hapsize) {
                                        taxonSnps[sIndexRight] = N;
                                        taxonSnps[sIndexLeft] = N;
                                    }
                                }
                            }
                            s++;
                        }
                    }
                }
            }
        }
    }
