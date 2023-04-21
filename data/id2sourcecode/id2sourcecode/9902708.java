        public static Spectrum.OneDim read(File aFile) throws SpecFormat_spc.IOException {
            RandomAccessFile theSpecFile;
            try {
                theSpecFile = new RandomAccessFile(aFile, "r");
                Header header = new Header();
                header.read(theSpecFile);
                int[] arrayOfChannelCount;
                switch(header.loadFormatIndicator) {
                    case 0:
                        arrayOfChannelCount = getChannelsMUSORT(theSpecFile, header);
                        break;
                    default:
                        arrayOfChannelCount = getChannelsSMAUG(theSpecFile, header);
                        break;
                }
                theSpecFile.close();
                SimpleDateFormat aDateFormat = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
                String dateTimeString = header.creationDate + " " + header.creationTime;
                ParsePosition pos = new ParsePosition(0);
                Date specDate = aDateFormat.parse(dateTimeString, pos);
                return (new Spectrum.OneDim(header.specName, specDate, new SpecChannel.OneDim((int) header.xLength, arrayOfChannelCount)));
            } catch (java.io.IOException e) {
                throw new SpecFormat_spc.IOException("Could not read the spectrum from the named file\n\n", e);
            } catch (SpectrumException se) {
                throw new SpecFormat_spc.IOException("Could not read the spectrum from the named file\n\n", se);
            }
        }
