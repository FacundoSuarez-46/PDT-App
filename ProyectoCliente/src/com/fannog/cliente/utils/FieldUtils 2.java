package com.fannog.cliente.utils;

import java.awt.event.KeyEvent;

public class FieldUtils {

    public static void esTexto(java.awt.event.KeyEvent evt) {
        Character c = evt.getKeyChar();

        if (!Character.isLetter(c) && c != KeyEvent.VK_SPACE) {
            evt.consume();
        }
    }

    public static void esTextoConPunto(java.awt.event.KeyEvent evt) {
        Character c = evt.getKeyChar();

        if (!Character.isLetter(c) && c != KeyEvent.VK_PERIOD) {
            evt.consume();
        }
    }

    public static void esNumero(java.awt.event.KeyEvent evt) {
        Character c = evt.getKeyChar();
        if (Character.isLetter(c) && c != KeyEvent.VK_PERIOD) {
            evt.consume();
        }
    }

}
