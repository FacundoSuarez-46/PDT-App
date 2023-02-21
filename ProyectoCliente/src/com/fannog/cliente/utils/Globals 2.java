package com.fannog.cliente.utils;

import com.fannog.servidor.entities.Usuario;
import java.time.format.DateTimeFormatter;

public class Globals {

    private static Usuario loggedUser;

    public static Usuario getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(Usuario loggedUser) {
        Globals.loggedUser = loggedUser;
    }

    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm");

}
