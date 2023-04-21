    @SuppressWarnings("unchecked")
    private void sortParcelByDate(List parcelsData, int lowerValue, int hightValue) throws Exception {
        if (lowerValue == hightValue) {
            return;
        }
        int length = hightValue - lowerValue + 1;
        int pivot = (lowerValue + hightValue) / 2;
        sortParcelByDate(parcelsData, lowerValue, pivot);
        sortParcelByDate(parcelsData, pivot + 1, hightValue);
        List working = new ArrayList();
        for (int i = 0; i < length; i++) working.add(i, parcelsData.get(lowerValue + i));
        int m1 = 0;
        int m2 = pivot - lowerValue + 1;
        for (int i = 0; i < length; i++) {
            if (m2 <= hightValue - lowerValue) {
                if (m1 <= pivot - lowerValue) {
                    GDDate payDate = null;
                    GDDate payDate2 = null;
                    if (working.get(m1) instanceof ParcelData) {
                        ParcelData parcelData = (ParcelData) working.get(m1);
                        ParcelData parcelData2 = (ParcelData) working.get(m2);
                        payDate = new GDDate(parcelData.getParcelDate());
                        payDate2 = new GDDate(parcelData2.getParcelDate());
                    } else if (working.get(m1) instanceof Parcel) {
                        Parcel parcelData = (Parcel) working.get(m1);
                        Parcel parcelData2 = (Parcel) working.get(m2);
                        payDate = new GDDate(parcelData.getDate());
                        payDate2 = new GDDate(parcelData2.getDate());
                    }
                    if (payDate.afterDay(payDate2)) {
                        parcelsData.set(i + lowerValue, working.get(m2++));
                    } else {
                        parcelsData.set(i + lowerValue, working.get(m1++));
                    }
                } else {
                    parcelsData.set(i + lowerValue, working.get(m2++));
                }
            } else {
                parcelsData.set(i + lowerValue, working.get(m1++));
            }
        }
    }
