    private void sendDestClientServerGreetingResponse(DataOutputStream dos, DestClientServerGreetingResponse destClientServerGreetingResponse) throws IOException {
        dos.writeByte(DestClientServerGreetingResponseID);
        dos.writeBoolean(destClientServerGreetingResponse.pcNameAlreadyUsed);
    }
