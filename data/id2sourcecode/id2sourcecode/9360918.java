        @Override
        public void onSuccess(Schedule schedule) {
            Date start = new Date(day.getTime());
            Date end = new Date(day.getTime() + 24 * 60 * 60 * 1000);
            int module = 30 * 60 * 1000;
            int column = channel2column.get(schedule.getChannel().getCode());
            List<Transmission> transmissions = schedule.getTransmissions();
            Iterator<Transmission> transmissionIt = null;
            Transmission transmission = null;
            if (!transmissions.isEmpty()) {
                transmissionIt = transmissions.iterator();
                transmission = transmissionIt.next();
                Date currentDate = new Date(start.getTime());
                int row = 0;
                do {
                    currentDate = new Date(currentDate.getTime() + module);
                    ResizableVerticalPanel transmissionPanel = new ResizableVerticalPanel();
                    while (transmission != null && transmission.getStart().compareTo(currentDate) < 0) {
                        transmissionPanel.add(new TransmissionWidget(transmission));
                        if (transmissionIt.hasNext()) {
                            transmission = transmissionIt.next();
                        } else {
                            transmission = null;
                        }
                    }
                    scheduleTable.setWidget(row, column, transmissionPanel);
                    row++;
                } while (currentDate.compareTo(end) < 0);
            } else {
                Label label = new Label("Nessun dato");
                scheduleTable.setWidget(0, column, label);
                Date currentDate = new Date(start.getTime() + module);
                int row = 1;
                do {
                    currentDate = new Date(currentDate.getTime() + module);
                    scheduleTable.setWidget(row, column, new Label());
                    row++;
                } while (currentDate.compareTo(end) < 0);
            }
            scheduleTable.setHeaderColumnWidth("50px");
            scheduleTable.layoutByColumn(column);
            if (selectedRow >= 0) {
                scheduleTable.ensureRowVisible(selectedRow, column);
            }
        }
