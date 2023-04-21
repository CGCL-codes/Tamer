    public int startCapture() {
        utils = new DllWrapper();
        CaptureDeviceList devList = CaptureDeviceList.getInstance();
        capFileName = getFullFileName();
        start.setTime(item.getStart());
        stop.setTime(item.getStop());
        File capFileParent = new File(capFileName).getParentFile();
        System.out.println(this + " : " + capFileParent.getAbsolutePath());
        if (capFileParent.exists() == false || capFileParent.isDirectory() == false) {
            item.setState(ScheduleItem.ERROR);
            item.setStatus("Error");
            item.log("The capture directory (" + capFileParent.getAbsolutePath() + ") does not exist, can not capture");
            System.out.println(this + " : The capture directory (" + capFileParent.getAbsolutePath() + ") does not exist, can not capture");
            item.addWarning("Directory does not exist");
            return -1;
        }
        Channel ch = store.getChannel(item.getChannel());
        if (ch == null) {
            item.setState(ScheduleItem.ERROR);
            item.setStatus("Error");
            item.log("Channel object was not found in Channel List");
            System.out.println(this + " : Channel object was not found in ScheduleItem");
            item.addWarning("Channel not found");
            return -2;
        }
        item.log("Channel data loaded from Item : " + ch.getName());
        int captureType = item.getCapType();
        if (captureType == -1) {
            captureType = ch.getCaptureType();
        }
        if (captureType == -1) {
            try {
                captureType = Integer.parseInt(store.getProperty("capture.deftype"));
            } catch (Exception e) {
                item.log("Error getting global capture type (" + store.getProperty("capture.deftype") + ")");
                System.out.println(this + " : Error getting global capture type (" + store.getProperty("capture.deftype") + ")");
                e.printStackTrace();
            }
        }
        System.out.println(this + " : About to start capture");
        item.log("File Name : " + new File(capFileName).getName());
        item.log("File Path : " + new File(capFileName).getParent());
        if (item.getCreatedFrom() != null) {
            item.log("EPG Data...");
            item.log("Title : " + item.getCreatedFrom().getName());
            item.log("Sub Title : " + item.getCreatedFrom().getSubName());
            item.log("Start : " + item.getCreatedFrom().getStart().toString());
            item.log("Duration : " + item.getCreatedFrom().getDuration());
        }
        item.log("Frequency : " + ch.getFrequency());
        item.log("Bandwidth : " + ch.getBandWidth());
        item.log("Prog Pid  : " + ch.getProgramID());
        item.log("Video Pid : " + ch.getVideoPid());
        item.log("Audio Pid : " + ch.getAudioPid());
        item.log("Cap Type  : " + captureType);
        producer = null;
        producer = devList.getProducer(ch.getFrequency(), ch.getBandWidth());
        if (producer != null) {
            System.out.println(this + " : Using existing producer, device " + producer.getDeviceIndex() + ":" + producer.getCaptureDevice().getName());
            item.log("Using existing producer, device " + producer.getDeviceIndex() + ":" + producer.getCaptureDevice().getName());
        } else {
            if (devList.getFreeDevice() == -1) {
                item.setState(ScheduleItem.WAITING);
                item.addWarning("No free device");
                System.out.println(this + " : No free device to start producer on");
                return -3;
            }
            StringBuffer producerLog = new StringBuffer();
            int startCode = -1;
            for (int x = 0; x < devList.getDeviceCount(); x++) {
                CaptureDevice cap = devList.getDevice(x);
                if (cap.isInUse() == false) {
                    runPreTask(x, cap.getID());
                    System.out.println(this + " : Starting producer, device " + x + ":" + cap.getName());
                    item.log("Starting producer, device " + x + ":" + cap.getName());
                    producer = null;
                    producer = new StreamProducerProcess(cap, x);
                    startCode = producer.startProducer(ch.getFrequency(), ch.getBandWidth(), producerLog, item.getLogFileNames());
                    if (startCode == 0) {
                        cap.setInUse(true);
                        break;
                    }
                    System.out.println(this + " : Producer start failed with code: " + startCode);
                    item.log("Producer start failed with code: " + startCode);
                    item.addWarning("Producer start failed");
                    runStartErrorTask(x, cap.getID());
                }
            }
            if (startCode != 0) {
                item.setStatus("Waiting for retry");
                item.log("No free capture device, Waiting for retry");
                System.out.println(this + " : No free capture device, Waiting for retry");
                item.setState(ScheduleItem.WAITING);
                item.addWarning("No free device");
                return -3;
            }
            System.out.println(this + " : Producer Started.");
            item.log("Producer Started.");
            item.log("Producer Start Log:\n\n" + producerLog.toString());
        }
        System.out.println(this + " : Starting consumer process");
        item.log("Starting consumer process");
        consumer = null;
        consumer = new StreamConsumerProcess();
        StringBuffer consumerLog = new StringBuffer();
        int consumerStartCode = -1;
        consumerStartCode = consumer.startConsumer(producer.getMemoryShareName(), ch.getProgramID(), ch.getVideoPid(), ch.getAudioPid(), ch.getAudioType(), captureType, capFileName, consumerLog, item.getLogFileNames());
        if (consumerStartCode == 0) {
            producer.addUsageCount();
            devList.addProducer(producer, ch.getFrequency(), ch.getBandWidth());
            System.out.println(this + " : Capture started.");
            item.log("Capture started.");
            item.log("Start Log:\n\n" + consumerLog.toString());
            new CaptureDetails().writeCaptureDetails(this);
            return producer.getDeviceIndex();
        } else {
            item.log("Start Log:\n\n" + consumerLog.toString());
            item.log("Consumer process start failed with code : " + consumerStartCode);
            item.setStatus("Waiting for retry");
            item.setState(ScheduleItem.WAITING);
            item.addWarning("Consumer did not start");
            deleteCapture(120000000);
            if (consumerStartCode == -104) {
                item.log("Consumer build graph failed, defaulting to TS-Mux capture type");
                item.addWarning("Capture Type Reset (TS-Mux)");
                item.setCapType(2);
            }
            if (consumerStartCode == -106) {
                item.log("PID's not found, defaulting to Full TS capture trye");
                item.addWarning("Capture Type Reset (Full-TS)");
                item.setCapType(0);
            }
            int usageCount = producer.getUsageCount();
            if (usageCount == 0) {
                if (producer.isProducerRunning()) {
                    int stopCode = producer.stopProducer();
                    Integer exitCode = producer.getExitCode();
                    item.log("Producer Stopped(" + stopCode + ") with exit code:" + exitCode);
                    System.out.println(this + " : Producer Stopped(" + stopCode + ") with exit code:" + exitCode);
                } else {
                    Integer exitCode = producer.getExitCode();
                    item.log("Producer already not running, it exited with:" + exitCode);
                    System.out.println(this + " : Producer already not running, it exited with:" + exitCode);
                }
                producer.getCaptureDevice().setInUse(false);
                devList.remProducer(producer.getKey());
                producer = null;
            } else {
                item.log("Producer not stopped, inUseCount(" + usageCount + ")");
                System.out.println(this + " : Producer not stopped, inUseCount(" + usageCount + ")");
            }
            return -4;
        }
    }
