    public void Delete(String HubAddr) {
        j = 0;
        while ((j < i) && (!list[j].getAddr().equals(HubAddr))) {
            j++;
        }
        if (j < i) {
            for (int x = j; x < i - 1; x++) {
                list[x] = list[x + 1];
            }
            i--;
        }
    }
