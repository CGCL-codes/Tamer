    @ActionScriptProperty(read = true, write = true, bindable = true)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }
