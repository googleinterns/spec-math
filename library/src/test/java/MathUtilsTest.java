import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {

    @Test
    void testAdd() {
        MathUtils math = new MathUtils();
        assertEquals(5, math.add(3,2), "The add method should add two numbers");
    }

    @Test
    void testDivide(){
        MathUtils math = new MathUtils();
        assertEquals(3,math.divide(6,2), "Divide method should divide two numbers");
        assertThrows(ArithmeticException.class, () -> math.divide(1,0), "Divide by zero should throw");
    }

    @Test
    void testComputeArea() {
        MathUtils math = new MathUtils();
        assertEquals(78,math.computeArea(5),"Compute area method should return circle area");
    }
}