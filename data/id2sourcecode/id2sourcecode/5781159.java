        public void run() {
            try {
                _sheet.writeSpreadsheet(_os);
            } catch (Exception x) {
                displayError(x);
            }
            try {
                _os.close();
            } catch (IOException x) {
                displayError(x);
            }
        }
