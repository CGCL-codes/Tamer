    public synchronized NoFlushSeekableOutputStream process(ImageInputStream inputData, NoFlushSeekableOutputStream outputData, ObjectParameters params) throws ImageProcessorException {
        if (inputData == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null", "inputData");
            throw new IllegalArgumentException(msg);
        }
        if (outputData == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null", "outputData");
            throw new IllegalArgumentException(msg);
        }
        if (params == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null", "params");
            throw new IllegalArgumentException(msg);
        }
        if (params.containsName(ParameterNames.DESTINATION_FORMAT_RULE)) {
            ImageWriter writer = null;
            Pipeline pipeline = null;
            String rule = null;
            try {
                rule = params.getParameterValue(ParameterNames.DESTINATION_FORMAT_RULE);
                writer = theImageWriterFactory.getWriter(rule, params);
            } catch (Exception e) {
                LOGGER.error("instantiating-error", new String[] { "image writer", rule });
                throw new ImageProcessorException(e);
            }
            if (writer == null) {
                String msg = EXCEPTION_LOCALIZER.format("instantiating-error", new String[] { "image writer", rule });
                throw new ImageProcessorException(msg);
            }
            try {
                pipeline = new Pipeline();
                if (params.containsName(ParameterNames.LEFT_X) && params.containsName(ParameterNames.RIGHT_X) && params.containsName(ParameterNames.BOTTOM_Y) && params.containsName(ParameterNames.TOP_Y)) {
                    Tool croppingTool = theToolFactory.getTool("CroppingTool");
                    pipeline.addTool(croppingTool);
                }
                pipeline.addTool(theToolFactory.getTool("RGBConverterTool"));
                if (params.containsName(ParameterNames.PRESERVE_X_LEFT) || params.containsName(ParameterNames.PRESERVE_X_RIGHT)) {
                    Tool clippingTool = theToolFactory.getTool("ClippingTool");
                    pipeline.addTool(clippingTool);
                }
                if (params.containsName(ParameterNames.IMAGE_WIDTH)) {
                    Tool resizingTool = theToolFactory.getTool("ResizingTool");
                    pipeline.addTool(resizingTool);
                }
                if (params.containsName(ParameterNames.WATERMARK_URL)) {
                    Tool watermarkingTool = theToolFactory.getTool("WatermarkingTool");
                    pipeline.addTool(watermarkingTool);
                }
                RenderedOp[] ops = null;
                ops = ImageReader.loadImage(inputData);
                if (ops.length > 0) {
                    ColorModel colorModel = ops[0].getColorModel();
                    if (colorModel.hasAlpha() && colorModel.getTransparency() != ColorModel.OPAQUE) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("Resetting scale mode to " + "SCALE_MODE_NEAREST");
                        }
                        params.setParameterValue(ParameterNames.SCALE_MODE, Integer.toString(ImageConstants.SCALE_MODE_NEAREST));
                    }
                }
                ops = pipeline.process(ops, params);
                outputData.mark();
                outputData = writer.process(ops, params, outputData);
            } catch (Exception e) {
                LOGGER.error("processor-failure");
                throw new ImageProcessorException(e);
            }
        } else {
            byte buffer[] = new byte[1024];
            int readBytes;
            try {
                while ((readBytes = inputData.read(buffer)) != -1) {
                    outputData.write(buffer, 0, readBytes);
                }
            } catch (IOException e) {
                throw new ImageProcessorException(e);
            }
            try {
                params.setParameterValue(ParameterNames.OUTPUT_IMAGE_MIME_TYPE, params.getParameterValue(ParameterNames.SOURCE_IMAGE_MIME_TYPE));
            } catch (MissingParameterException e) {
                throw new ImageProcessorException(e);
            }
        }
        return outputData;
    }
