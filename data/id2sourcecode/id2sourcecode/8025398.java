    public void problem(final InterpreterException reason) {
        try {
            JSONWriter.ObjectWriter json = out.startElement().startObject();
            writeClassAndAnchor(json, "Problem", mark.apply());
            json.startMember("text").writeString(Tracer.readException(reason));
            writeException(json.startMember("reason"), reason);
            writeTrace(json, Tracer.traceException(reason));
            json.finish();
        } catch (IOException e) {
            Logging.EventLoop_LOG.warn("Unable to log Causeway event", e);
        }
    }
