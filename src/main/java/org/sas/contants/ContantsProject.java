package org.sas.contants;

public enum ContantsProject {

    SUFIX_PUB_FILE("_pub.key"),
    SUFIX_PRIV_FILE("_priv.key"),
    PUBLIC_KEY("pub"),
    PRIVATE_KEY("priv");

    String name;

    ContantsProject(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
