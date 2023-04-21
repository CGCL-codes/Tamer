    @Before
    public void setUp() throws Exception {
        String configFileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<rules>" + "	<database name=\"db1\">" + "		<cube name=\"cube1\">" + "			<readRules>" + "				<rule type=\"deny\">" + "					<objects>" + "						<user name=\"user1\"/>" + "						<group name=\"group1\"/>" + "					</objects>" + "					<subject>" + "						<dimension name=\"dim1\"> " + "							<element name=\"dim1_elem1\"/>" + "							<element name=\"dim1_elem2\"/>" + "						</dimension>" + "						<dimension name=\"dim2\"/>" + "					</subject>" + "				</rule>" + "				<rule type=\"deny\">" + "					<objects>" + "						<user name=\"user2\"/>" + "					</objects>" + "					<subject>" + "						<dimension name=\"dim2\"/> " + "						<dimension name=\"dim3\"/>" + "					</subject>" + "				</rule>" + "			</readRules>" + "			<writeRules>" + "				<rule type=\"deny\">" + "					<objects>" + "						<user name=\"user3\"/>" + "					</objects>" + "					<subject>" + "						<dimension name=\"dim1\"/> " + "						<dimension name=\"dim3\"/>" + "					</subject>" + "				</rule>" + "			</writeRules>" + "		</cube>" + "	</database>" + "</rules>";
        File configFile = new File(DefaultTest._configFile);
        configFile.delete();
        configFile.createNewFile();
        FileWriter writer = new FileWriter(configFile);
        writer.write(configFileContent);
        writer.flush();
        writer.close();
    }
