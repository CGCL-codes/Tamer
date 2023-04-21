    public void init() throws IOException {
        if ((this.scanner == null && this.connection == null && this.weatherStation != null) || isStationChanged) {
            this.datasets = new ArrayList<DataSet>();
            this.currentLine = 1;
            if (httpProxy != null && httpProxy.isEnabled()) {
                System.setProperty("proxySet", "true");
                System.setProperty("http.proxyHost", httpProxy.getUrl());
                System.setProperty("http.proxyPort", Integer.toString(httpProxy.getPort()));
                if (httpProxy.getUsername() != null && !httpProxy.getUsername().equalsIgnoreCase("")) {
                    System.setProperty("http.proxyUser", httpProxy.getUsername());
                }
                if (httpProxy.getPassword() != null && !httpProxy.getPassword().equalsIgnoreCase("")) {
                    System.setProperty("http.proxyPassword", httpProxy.getPassword());
                }
            }
            URL url = new URL(this.url.replace("{1}", this.weatherStation.getStationId()));
            if (this.weatherDate != null) {
                if (this.dataGraphSpan.equals(DataGraphSpan.DAY)) {
                    DateTime waetherDateTime = new DateTime(weatherDate.getTime());
                    StringBuilder dateUrl = new StringBuilder(this.url.replace("{1}", this.weatherStation.getStationId()));
                    dateUrl.append("&day=").append(waetherDateTime.get(DateTimeFieldType.dayOfMonth())).append("&");
                    dateUrl.append("year=").append(waetherDateTime.get(DateTimeFieldType.year())).append("&");
                    dateUrl.append("month=").append(waetherDateTime.get(DateTimeFieldType.monthOfYear())).append("&");
                    url = new URL(dateUrl.toString());
                } else if (this.dataGraphSpan.equals(DataGraphSpan.MONTH)) {
                    DateTime weatherDateTime = new DateTime(weatherDate.getTime());
                    StringBuilder dateUrl = new StringBuilder(this.url.replace("{1}", this.weatherStation.getStationId()));
                    dateUrl.append("&month=").append(weatherDateTime.get(DateTimeFieldType.monthOfYear())).append("&");
                    dateUrl.append("year=").append(weatherDateTime.get(DateTimeFieldType.year())).append("&");
                    dateUrl.append("graphspan=month");
                    url = new URL(dateUrl.toString());
                }
            }
            this.connection = (HttpURLConnection) url.openConnection();
            this.connection.setRequestMethod("GET");
            this.connection.setUseCaches(false);
            this.scanner = new Scanner(this.connection.getInputStream());
            String line = this.scanner.nextLine();
            if (line == null || !StringUtils.hasText(line)) {
                this.scanner.nextLine();
            }
            isStationChanged = false;
        }
    }
