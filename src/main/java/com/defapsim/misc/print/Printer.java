package com.defapsim.misc.print;

public interface Printer {

    int columnSize = 29;
    int columns = 4 + 1;

    /**
     * The headline, which is displayed in a printout
     */
    void printHeadline();
}
