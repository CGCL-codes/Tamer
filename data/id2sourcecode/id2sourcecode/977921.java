    private float energyUtility(Node node, VM vm) {
        int renew_ene = 0;
        int nonrenew_ene = 0;
        float dornw = 0;
        float eneff = 0;
        float ecoeff = 0;
        LinkedList<Integer> renewableHosts = new LinkedList<Integer>();
        LinkedList<Integer> nonRenewableHosts = new LinkedList<Integer>();
        float[] hostsAvailable = new float[onNodes.size()];
        for (int i = 0; i < onNodes.size(); i++) {
            String nodeId = this.onNodes.get(i);
            Node host = (Node) nodes.get(nodeId);
            if (host.isRenewable()) {
                renewableHosts.add(i);
                renew_ene += 4 - host.getAvailableCPU() * host.getCapacityCPU() / 100;
            } else {
                nonRenewableHosts.add(i);
                nonrenew_ene += 4 - host.getAvailableCPU() * host.getCapacityCPU() / 100;
            }
        }
        if (nonrenew_ene == 0 && renew_ene != 0) {
            dornw = 1.0F;
            eneff = (40 - (renew_ene + nonrenew_ene)) / 40;
        } else if (nonrenew_ene == 0 && renew_ene == 0) {
            dornw = 0.0F;
            eneff = 1.0F;
        } else {
            dornw = 1 - (nonrenew_ene / renew_ene);
            eneff = (40 - (renew_ene + nonrenew_ene)) / 40;
        }
        ecoeff = (dornw + eneff) / 2;
        System.out.println("VM ID = " + vm.id + "; Actual RNW CAP = " + renew_ene + ";NON RNW CAP = " + nonrenew_ene + "; DoRNW = " + dornw);
        System.out.println("Actual EnEff = " + eneff);
        if (node == null) {
            System.out.println("OUT - VM ID = " + vm.id);
            if (dornw == 0) dornw = 0.0F; else dornw = dornw * 0.9F;
            ecoeff = (dornw + eneff) / 2;
            return ecoeff;
        } else {
            if (node.isRenewable()) {
                System.out.println("LOCAL RNW - VM ID = " + vm.id);
                if (nonrenew_ene == 0 && dornw != 0) dornw = dornw * 1.1F; else if (nonrenew_ene == 0 && dornw == 0) dornw = 1.0F; else dornw = 1 - (nonrenew_ene / renew_ene++);
                eneff = (40 - (renew_ene++ + nonrenew_ene)) / 40;
            } else {
                System.out.println("LOCAL NON RNW - VM ID = " + vm.id);
                if (nonrenew_ene == 0 && dornw != 0) dornw = dornw * 0.5F; else if (nonrenew_ene == 0 && dornw == 0) dornw = 0.0F; else dornw = 1 - (nonrenew_ene++ / nonrenew_ene);
                eneff = (40 - (renew_ene + nonrenew_ene++)) / 40;
            }
            ecoeff = (dornw + eneff) / 2;
        }
        System.out.println("Future DoRNW = " + dornw);
        System.out.println("Future EnEff = " + eneff);
        return ecoeff;
    }
