    public static void main(String[] args) {
        World world = new World(null);
        Scanner sc = new Scanner(System.in);
        try {
            world.connect("localhost", 7777);
            System.out.print("Username:");
            String username = sc.nextLine();
            System.out.print("Password:");
            String password = sc.nextLine();
            world.login(username, password);
            while (world.handleConnection()) ;
        } catch (IOException e) {
            System.err.println(e);
        }
    }
