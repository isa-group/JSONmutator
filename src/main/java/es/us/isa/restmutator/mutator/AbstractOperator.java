package es.us.isa.restmutator.mutator;

/**
 * Superclass for mutation operators. The attribute {@link AbstractOperator#weight}
 * represents the following: when an element can be mutated in different ways (e.g.
 * a string can be replaced or set to null), the weight determines which among all
 * possible mutations will more likely be applied (the one with the highest weight).
 *
 * @author Alberto Martin-Lopez
 */
public abstract class AbstractOperator extends RandomManager {

    protected float weight;

    public AbstractOperator() {
        super();
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public abstract Object mutate(Object element);
}
