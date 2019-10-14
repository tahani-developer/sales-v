package com.dr7.salesmanmanager;

import java.io.InputStream;
import java.io.OutputStream;


//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

public class PortMediator {
    private static PortMediator pm;
    private InputStream is;
    private OutputStream os;

    private PortMediator() {
    }

    public static PortMediator getInstance() {
        if (pm == null) {
            pm = new PortMediator();
        }

        return pm;
    }

    public void setIs(InputStream is) {
        this.is = is;
    }

    public InputStream getIs() {
        return this.is;
    }

    public void setOs(OutputStream os) {
        this.os = os;
    }

    public OutputStream getOs() {
        return this.os;
    }
}
