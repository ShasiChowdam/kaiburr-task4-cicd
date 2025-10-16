package com.kaiburr.tasks.service;

import org.springframework.stereotype.Component;

@Component
public class CommandValidator {
    private static final String[] DENY = new String[]{
            ";","&&","||","|","`","$(",
            "/etc/passwd","rm -rf","chmod ","chown ",
            ">>","> ","< ","curl http","wget http"
    };
    public void validate(String cmd){
        if(cmd==null || cmd.isBlank()) throw new IllegalArgumentException("Empty command");
        String c = cmd.trim().toLowerCase();
        for(String d: DENY){
            if(c.contains(d)) throw new IllegalArgumentException("Unsafe command: " + d);
        }
    }
}
