package com.defapsim.misc;

/**
 * A simple implementation of a 2-tuple, which can contain any object.
 */
public class Tupel {

    /**
     * The first attribute of the 2-tuple.
     */
    private Object attributeA;

    /**
     * The second attribute of the 2-tuple.
     */
    private Object attributeB;

    public Tupel(Object elementA, Object elementB) {
        this.attributeA = elementA;
        this.attributeB = elementB;
    }

    /**
     * Getter
     */

    public Object getAttributeA() {
        return this.attributeA;
    }

    public Object getAttributeB() {
        return this.attributeB;
    }

    /**
     * Setter
     */

    public void setAttributeA(Object attributeA) {
        this.attributeA = attributeA;
    }

    public void setAttributeB(Object attributeB) {
        this.attributeB = attributeB;
    }

}
