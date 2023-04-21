    public void setLinkPositionsInLayer() {
        Iterator it = layers.iterator();
        while (it.hasNext()) {
            LinkedList tlay = (LinkedList) it.next();
            Iterator it2 = tlay.iterator();
            Node[] nodes = new Node[tlay.size()];
            int count = 0;
            while (it2.hasNext()) {
                Node tn = (Node) it2.next();
                nodes[count++] = tn;
            }
            ArrayList toHigher = new ArrayList();
            ArrayList toLower = new ArrayList();
            ArrayList actualDirection;
            toHigher.add(new LinkedList());
            toLower.add(new LinkedList());
            for (Node node : nodes) {
                Iterator it3 = node.getOutLinks().iterator();
                while (it3.hasNext()) {
                    Link tl = (Link) it3.next();
                    if (tl.from.getLayer() == tl.to.getLayer() && tl.from.getPositionInLayer() != tl.to.getPositionInLayer() && tl.from.getPositionInLayer() != tl.to.getPositionInLayer() + 1 && tl.from.getPositionInLayer() != tl.to.getPositionInLayer() - 1) {
                        if (tl.from.getPositionInLayer() < tl.to.getPositionInLayer()) {
                            actualDirection = toHigher;
                        } else {
                            actualDirection = toLower;
                        }
                        boolean placed = false;
                        int actual = 0;
                        while (!placed) {
                            Iterator it4 = ((LinkedList) actualDirection.get(actual)).iterator();
                            boolean fits = true;
                            while (it4.hasNext()) {
                                Link checkLink = (Link) it4.next();
                                if (tl.checkLateralCrossing(checkLink)) {
                                    fits = false;
                                    break;
                                }
                            }
                            if (fits) {
                                ((LinkedList) actualDirection.get(actual)).add(tl);
                                if (actual % 2 == 0) actual = -actual / 2; else if (actual % 2 == 1) actual = (actual + 1) / 2;
                                tl.setLateralAngle(actual);
                                tl.setLateralLevel(Math.abs(tl.to.getPositionInLayer() - tl.from.getPositionInLayer()) - 1);
                                placed = true;
                            } else {
                                actual++;
                                actualDirection.add(new LinkedList());
                            }
                        }
                    }
                }
            }
        }
    }
