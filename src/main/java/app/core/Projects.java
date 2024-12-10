package app.core;

import app.storage.Storable;

public class Projects extends Storable<Node> {
    private static Projects instance = null;

    private Projects() {
        super("projects", new Node("root"));
        // [TODO] Add a recursive event listener to the root 
        // that detects change in the node. Within this event, 
        // whenever there is a change, the Projects store function 
        // is called and if a node is added, this event is also added to it. 
        // Did you get the idea? It's complicated, getting in touch with anything.
        // [TIP] A good idea is to create a method in Projects that takes a node as 
        // a parameter and adds this listener event to it through the function itself 
        // (using recursion). This is good because because it is a projects method, 
        // you will already have access to the store within it.
    };

    public static Projects getInstance() {
        if (Projects.instance == null) Projects.instance = new Projects();
        return Projects.instance;
    };
};
