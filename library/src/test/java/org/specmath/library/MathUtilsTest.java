/*
Copyright 2020 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.specmath.library;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MathUtilsTest {
  MathUtils mathUtils;

  @BeforeEach
  void init() {
    mathUtils = new MathUtils();
  }

  @Nested
  class AddTest {
    @Test
    void testAddingTwoPositives() {
      assertEquals(5, mathUtils.add(3, 2), "Add method should return the sum of two numbers");
    }

    @Test
    void testAddingTwoNegatives() {
      assertEquals(-14, mathUtils.add(-13, -1), "Add method should return the sum of two numbers");
    }

    @Test
    void testAddingAPositiveAndANegative() {
      assertEquals(0, mathUtils.add(-5, 5), "Add method should return the sum of two numbers");
    }
  }

  @Test
  void testSumOfArray() {
    assertAll(
        () -> assertEquals(0, mathUtils.sumOfArray(Arrays.asList(new Integer[] {1, -1}))),
        () -> assertEquals(6, mathUtils.sumOfArray(Arrays.asList(new Integer[] {1, 2, 3}))),
        () -> assertEquals(0, mathUtils.sumOfArray(Arrays.asList(new Integer[] {}))));
  }

  @Nested
  class DivideTest {
    @Test
    void testDivide() {
      assertEquals(3, mathUtils.divide(6, 2), "Divide method should divide two numbers");
    }

    @Test
    void testDivideThrowsOnDivideByZero() {
      assertThrows(
          ArithmeticException.class, () -> mathUtils.divide(1, 0), "Divide by zero should throw");
    }
  }

  @Test
  void testComputeArea() {
    assertEquals(78, mathUtils.computeArea(5), "Compute area method should return circle area");
  }
}
