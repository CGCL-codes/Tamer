    public static boolean subtractAndSetAntibodyBeans(AntibodyBean[] beans, HttpSession session) {
        boolean lastBean = false;
        int num = beans.length - 1;
        if (num != 0) {
            AntibodyBean[] minusOneBeans = new AntibodyBean[num];
            for (int i = 0; i < num; i++) {
                minusOneBeans[i] = beans[i + 1];
            }
            session.setAttribute("antibodyBeansToEdit", minusOneBeans);
        } else lastBean = true;
        return lastBean;
    }
