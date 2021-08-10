package com.defapsim.misc.print;

import com.defapsim.application.Component;

import java.util.List;

/**
 * Print method, which allow to get information about the components
 */
public class ComponentPrinter implements Printer {

    /**
     * Private static Singleton instance
     */
    private static final ComponentPrinter instance = new ComponentPrinter();

    /**
     * Private constructor to prevent it from being accessed
     */
    private ComponentPrinter() {
    }

    /**
     * Access to the static Singleton instance
     * @return      Static Singleton instance
     */
    public static ComponentPrinter getInstance() {
        return instance;
    }

    @Override
    public void printHeadline() {
        System.out.println(ConsoleFormatter.line(this.columnSize * this.columns));
        System.out.println("|\t" +
                ConsoleFormatter.center("Identifier", this.columnSize) +
                "\t|\t" +
                ConsoleFormatter.center("RAM demand (GB)", this.columnSize) +
                /*"\t|\t" +
                ConsoleFormatter.center("Storage demand (GB)", this.columnSize) +*/
                "\t|\t" +
                ConsoleFormatter.center("ComputingPower demand", this.columnSize) +
                "\t|\t" +
                ConsoleFormatter.center("Worst-case execution time", this.columnSize) +
                "\t|");
        System.out.println("|\t" +
                ConsoleFormatter.center("", this.columnSize) +
                "\t|\t" +
                ConsoleFormatter.center("", this.columnSize) +
                /*"\t|\t" +
                ConsoleFormatter.center("", this.columnSize) +*/
                "\t|\t" +
                ConsoleFormatter.center("(vCPU)", this.columnSize) +
                "\t|\t" +
                ConsoleFormatter.center("(ms)", this.columnSize) +
                "\t|");
        System.out.println("| " + ConsoleFormatter.line((this.columnSize * this.columns) - 4) + " |");
    }

    /**
     * Shows the component configurations of a component
     * @param component     The component whose component configuration is printed out
     */
    public void printComponentConfiguration(Component component) {
        this.printHeadline();
        System.out.println("|\t" +
                ConsoleFormatter.center(component.getIdentifier(), this.columnSize) +
                "\t|\t" +
                ConsoleFormatter.center(component.getMemoryDemand().toString(), this.columnSize) +
                /*"\t|\t" +
                ConsoleFormatter.center(component.getStorageDemand().toString(), this.columnSize) +*/
                "\t|\t" +
                ConsoleFormatter.center(component.getComputingPowerDemand().toString(), this.columnSize) +
                "\t|\t" +
                ConsoleFormatter.center(component.getWorstCaseExecutionTime().toString(), this.columnSize) +
                "\t|");

        System.out.println(ConsoleFormatter.line(this.columnSize * this.columns));
    }

    /**
     * Shows the component configurations of a list of components
     * @param componentList     The list of components whose component configuration is printed out
     */
    public void printComponentConfigurationList(List<Component> componentList) {
        this.printHeadline();
        for (Component component : componentList) {
            System.out.println("|\t" +
                    ConsoleFormatter.center(component.getIdentifier(), this.columnSize) +
                    "\t|\t" +
                    ConsoleFormatter.center(component.getMemoryDemand().toString(), this.columnSize) +
                    /*"\t|\t" +
                    ConsoleFormatter.center(component.getStorageDemand().toString(), this.columnSize) +*/
                    "\t|\t" +
                    ConsoleFormatter.center(component.getComputingPowerDemand().toString(), this.columnSize) +
                    "\t|\t" +
                    ConsoleFormatter.center(component.getWorstCaseExecutionTime().toString(), this.columnSize) +
                    "\t|");
        }
        System.out.println(ConsoleFormatter.line(this.columnSize * this.columns));
    }
}
