    public static void quickSort(Object[] datas, int datasCount, IComparator comparator) {
        int debut = 0;
        int fin = datasCount - 1;
        int presumee;
        int I, J;
        Object pivot;
        ArrayList<Integer> memo = new ArrayList<Integer>();
        if (datasCount < 2) {
            return;
        }
        for (; ; ) {
            while (fin - debut > 1) {
                presumee = (debut + fin) / 2;
                I = debut;
                if (comparator.compare(datas[presumee], datas[I]) < 0) {
                    I = presumee;
                }
                if (comparator.compare(datas[fin], datas[I]) < 0) {
                    I = fin;
                }
                if (debut != I) {
                    ManagerAction.exchangeDatasElementsAt(debut, I, datas, datasCount);
                }
                if (comparator.compare(datas[presumee], datas[fin]) < 0) {
                    pivot = datas[presumee];
                    datas[presumee] = null;
                } else {
                    pivot = datas[fin];
                    datas[fin] = datas[presumee];
                    datas[presumee] = null;
                }
                I = debut + 1;
                J = fin - 1;
                while (I < J) {
                    while (I < presumee) {
                        if (comparator.compare(pivot, datas[I]) < 0) {
                            break;
                        }
                        I = I + 1;
                    }
                    while (J > presumee) {
                        if (comparator.compare(datas[J], pivot) < 0) {
                            break;
                        }
                        J = J - 1;
                    }
                    if (I == J) {
                        break;
                    }
                    if (I == presumee) {
                        datas[I] = datas[J];
                        datas[J] = null;
                        presumee = J;
                        I = I + 1;
                    } else if (J == presumee) {
                        datas[J] = datas[I];
                        datas[I] = null;
                        presumee = I;
                        J = J - 1;
                    } else {
                        ManagerAction.exchangeDatasElementsAt(I, J, datas, datasCount);
                        I = I + 1;
                        J = J - 1;
                    }
                }
                datas[presumee] = pivot;
                pivot = null;
                if (presumee - debut < fin - presumee) {
                    memo.add(new Integer(presumee + 1));
                    memo.add(new Integer(fin));
                    fin = presumee - 1;
                } else {
                    memo.add(new Integer(debut));
                    memo.add(new Integer(presumee - 1));
                    debut = presumee + 1;
                }
            }
            if (fin == debut + 1) {
                if (comparator.compare(datas[fin], datas[debut]) < 0) {
                    ManagerAction.exchangeDatasElementsAt(debut, fin, datas, datasCount);
                }
            }
            if (memo.size() == 0) {
                return;
            }
            fin = ((Integer) memo.get(memo.size() - 1)).intValue();
            memo.remove(memo.size() - 1);
            debut = ((Integer) memo.get(memo.size() - 1)).intValue();
            memo.remove(memo.size() - 1);
        }
    }
