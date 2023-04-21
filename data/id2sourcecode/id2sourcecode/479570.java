    public DSoCService(int MaximumNumberOfSegmentation, String ServiceName) {
        MAX_NUMBER_OF_SES = MaximumNumberOfSegmentation;
        Random rand = new Random();
        int RandNum = rand.nextInt(MAX_NUMBER_OF_SES + 1);
        NUMBER_OF_SES = (RandNum < 3) ? 3 : RandNum;
        hx_SES = new SES[NUMBER_OF_SES];
        sx_SES = new SES[NUMBER_OF_SES];
        str_Name = ServiceName;
        init_SESs();
    }
