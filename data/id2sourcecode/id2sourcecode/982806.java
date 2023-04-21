    public String insertTripButtonClicked() {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy");
        String depTime = dateTimeFormat.format(getDate().getTime()).concat(" ").concat(getTime());
        dateTimeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Date departure;
        try {
            departure = dateTimeFormat.parse(depTime);
        } catch (ParseException e) {
            return "insertFailed";
        }
        Location fromLocation = new Location(fromZip, fromCity);
        Location toLocation = new Location(toZip, toCity);
        Location stopLocation = new Location(this.stopZip, this.stopCity);
        try {
            getServiceTrip().insertTrip(departure, getSeats(), fromLocation, toLocation, stopLocation);
        } catch (ServiceException e) {
            return "errorDatabase";
        }
        return "insertSuccessfull";
    }
