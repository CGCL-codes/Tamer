            public void run() {
                try {
                    int read;
                    while ((read = rDriverInput.read()) != -1) {
                        process.getOutputStream().write(read);
                    }
                    process.getOutputStream().close();
                } catch (IOException iop) {
                    exceptionArray[0] = iop;
                    throw new RuntimeException("Problem with reading R Script, or writing to process", iop);
                }
            }
