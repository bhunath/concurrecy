package JavaMemoryRegions;

import java.util.ArrayList;
import java.util.List;

/**
 * Heap : is the shared memory region that belong to a process.
 * All Object Created Using new Operator
 * Members of classes
 * Static Variables.
 * Heap is Manages and Governed by Garbage Collector.
 * Object remain in the heap as long as they are referenced.
 * Static Variables are untouched. : Stay Forever.
 * Heap is shared and Stack is exclusive.
 */
public class JavaMemoryRegions {


    /**
     * Objects vs Reference
     * References are not same as Object
     * Object reference1 = new Object();
     * Object reference2 = reference1;
     *
     * In Above example there are three different entity are present.
     * (reference1, reference2 , Object itself)
     * reference1 --> Object
     * reference2 --> Object
     * Reference are stored in the Stack if created inside Methods.
     * Reference are stored in heap if created as part of member variables.
     */
    public static void main(String[] args) {
        JavaMemoryRegions javaMemoryRegions = new JavaMemoryRegions();
        Integer x = 1;
        Integer y = 2;
        List<Integer> valueContainers = new ArrayList<>();
        valueContainers.add(x);
        valueContainers.add(y);
        Integer result = javaMemoryRegions.sum(x,y,valueContainers);
        System.out.println(result);

    }


    /**
     * Stack Size in JVM is fixed.
     * Hence Deep Recursive Calls can end up in StackOverFlow Exception.
     * Stack :
     *      1) Function Call
     *      2) Thread Creation
     * @param a
     * @param b
     * @param values
     * @return
     */
    Integer sum(Integer a , Integer b, List<Integer> values){
        values.add(50);
        int c = a+b;
        return c;
    }


}
