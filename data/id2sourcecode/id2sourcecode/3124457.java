        public void fire() {
            if (devicePrinter != null) {
                devicePrinter.println("Tick : " + writeCount);
            }
            int address = read16(EEARH_reg, EEARL_reg);
            if (writeCount > 0) {
                if (writeEnableWritten) {
                    if (devicePrinter != null) devicePrinter.println("EEPROM: " + EEDR_reg.read() + " written to " + address);
                    EEPROM_data[address] = EEDR_reg.read();
                    mainClock.insertEvent(writeFinishedEvent, (long) (mainClock.getHZ() * 0.0085));
                    simulator.delay(2);
                }
            }
            if (readEnableWritten && !writeEnable) {
                if (devicePrinter != null) devicePrinter.println("EEPROM: " + EEPROM_data[address] + " read from " + address);
                EEDR_reg.write(EEPROM_data[address]);
                EECR_reg.resetEERE();
                simulator.delay(4);
            }
            if (writeCount > 0) {
                writeCount--;
                mainClock.insertEvent(ticker, 1);
            }
            if (writeCount == 0) {
                if (devicePrinter != null) devicePrinter.println("EEPROM: write count hit 0, clearing EEMWE");
                writeCount--;
                EECR_reg.resetEEMWE();
            }
            writeEnableWritten = false;
            readEnableWritten = false;
        }
