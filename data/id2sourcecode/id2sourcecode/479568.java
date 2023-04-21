    public DSoCService() {
        Random rand = new Random();
        int RandNum = rand.nextInt(MAX_NUMBER_OF_SES + 1);
        NUMBER_OF_SES = (RandNum < 3) ? 3 : RandNum;
        hx_SES = new SES[NUMBER_OF_SES];
        sx_SES = new SES[NUMBER_OF_SES];
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(4);
        nf.setMaximumIntegerDigits(4);
        str_Name = "Service" + nf.format((long) NameSec);
        NameSec++;
        init_SESs();
    }
