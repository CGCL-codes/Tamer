        private void buildLength(int childs[]) {
            this.length = new byte[freqs.length];
            int numNodes = childs.length / 2;
            int numLeafs = (numNodes + 1) / 2;
            int overflow = 0;
            for (int i = 0; i < maxLength; i++) bl_counts[i] = 0;
            int lengths[] = new int[numNodes];
            lengths[numNodes - 1] = 0;
            for (int i = numNodes - 1; i >= 0; i--) {
                if (childs[2 * i + 1] != -1) {
                    int bitLength = lengths[i] + 1;
                    if (bitLength > maxLength) {
                        bitLength = maxLength;
                        overflow++;
                    }
                    lengths[childs[2 * i]] = lengths[childs[2 * i + 1]] = bitLength;
                } else {
                    int bitLength = lengths[i];
                    bl_counts[bitLength - 1]++;
                    this.length[childs[2 * i]] = (byte) lengths[i];
                }
            }
            if (DeflaterConstants.DEBUGGING) {
                System.err.println("Tree " + freqs.length + " lengths:");
                for (int i = 0; i < numLeafs; i++) System.err.println("Node " + childs[2 * i] + " freq: " + freqs[childs[2 * i]] + " len: " + length[childs[2 * i]]);
            }
            if (overflow == 0) return;
            int incrBitLen = maxLength - 1;
            do {
                while (bl_counts[--incrBitLen] == 0) ;
                do {
                    bl_counts[incrBitLen]--;
                    bl_counts[++incrBitLen]++;
                    overflow -= 1 << (maxLength - 1 - incrBitLen);
                } while (overflow > 0 && incrBitLen < maxLength - 1);
            } while (overflow > 0);
            bl_counts[maxLength - 1] += overflow;
            bl_counts[maxLength - 2] -= overflow;
            int nodePtr = 2 * numLeafs;
            for (int bits = maxLength; bits != 0; bits--) {
                int n = bl_counts[bits - 1];
                while (n > 0) {
                    int childPtr = 2 * childs[nodePtr++];
                    if (childs[childPtr + 1] == -1) {
                        length[childs[childPtr]] = (byte) bits;
                        n--;
                    }
                }
            }
            if (DeflaterConstants.DEBUGGING) {
                System.err.println("*** After overflow elimination. ***");
                for (int i = 0; i < numLeafs; i++) System.err.println("Node " + childs[2 * i] + " freq: " + freqs[childs[2 * i]] + " len: " + length[childs[2 * i]]);
            }
        }
