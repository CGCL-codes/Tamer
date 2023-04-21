    @RequestMapping(value = "/dmxDevice.do", method = RequestMethod.POST)
    public ModelAndView dmxDeviceEdit(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "controllerAddress", required = false) String controllerAddress, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "description", required = false) String description, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "channel", required = false) Short channel, @RequestParam(value = "minValue", required = false) Integer minValue, @RequestParam(value = "maxValue", required = false) Integer maxValue, HttpServletRequest request) {
        if (request.getParameter("ok") != null) {
            SmartCrib smartCrib = smartCribManager.getSmartCribInstance();
            DMXDevice device = null;
            String logAction = "modified";
            if (id != null) {
                device = smartCrib.findDeviceById(id);
            }
            if (device == null) {
                device = new DMXDevice();
                logAction = "created";
            }
            if (device != null) {
                if (name != null) device.setName(name);
                if (description != null) device.setDescription(null);
                try {
                    device.setType(DMXDevice.Type.valueOf(type));
                } catch (IllegalArgumentException exc) {
                }
                if (controllerAddress != null) device.setControllerAddress(controllerAddress);
                if (channel != null) device.setChannel(channel);
                if (minValue != null) device.setMinValue(minValue);
                if (maxValue != null) device.setMaxValue(maxValue);
                logger.info("Device with id " + device.getId() + " has been " + logAction + ". New name: " + device.getName() + "; description: " + device.getDescription() + "; channel=" + device.getChannel() + "; type=" + device.getType() + "; minValue=" + device.getMinValue() + "; maxValue=" + device.getMaxValue());
                smartCribManager.saveSmartCrib(smartCrib);
            }
        }
        ModelAndView mv = new ModelAndView("dmxDevices");
        SmartCrib smartCrib = smartCribManager.getSmartCribInstance();
        mv.addObject("dmxDevices", smartCrib.getDevices());
        mv.addObject("dmxDeviceTypes", DMXDevice.Type.values());
        return mv;
    }
