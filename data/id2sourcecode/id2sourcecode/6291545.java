    public List<Double> getNextEpoch() throws UserException {
        if (finished) {
            return null;
        }
        try {
            List<Double> result = new ArrayList<Double>(Epoch.getSamplesCount(samplingRate));
            String line = null;
            int nentries = Epoch.getSamplesCount(samplingRate);
            for (int i = 0; i < nentries; i++) {
                if ((line = input.readLine()) != null) {
                    result.add(Double.parseDouble(getChannelValue(line)));
                } else {
                    if (i >= samplingRate.value()) {
                        Util.add(result, 0.0, nentries - i);
                    }
                    input.close();
                    finished = true;
                    break;
                }
            }
            if (result.size() < nentries) {
                return null;
            }
            return result;
        } catch (NumberFormatException e) {
            throw new UserException(ErrorMessages.FILE_UNEXPECTED_FORMAT);
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }
