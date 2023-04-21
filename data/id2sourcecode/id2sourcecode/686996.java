    public double getUcret(String paraBirimi, ExchangeRateProvider provider) {
        Date currentDate = new Date();
        long kiraGunu = currentDate.getTime() - this.baslangicTarihi.getTimeInMillis();
        kiraGunu = kiraGunu / (1000 * 60 * 60 * 24);
        if (kiraGunu >= 2) {
            fiyat = 2 + (kiraGunu - 2) / 2;
        } else {
            fiyat = 2;
        }
        return (double) (fiyat * provider.getRateFromTo("TR", "EUR"));
    }
