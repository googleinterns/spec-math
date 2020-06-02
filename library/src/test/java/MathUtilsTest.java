import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathUtilsTest {
    MathUtils mathUtils;

    @BeforeEach
    void init(){
        mathUtils = new MathUtils();
    }

    @Test
    void testAdd() {
        assertEquals(5, mathUtils.add(3,2), "The add method should add two numbers");
    }

    @Test
    void testDivide(){
        assertEquals(3,mathUtils.divide(6,2), "Divide method should divide two numbers");
        assertThrows(ArithmeticException.class, () -> mathUtils.divide(1,0), "Divide by zero should throw");
    }

    @Test
    void testComputeArea() {
        assertEquals(78,mathUtils.computeArea(5),"Compute area method should return circle area");
    }
}