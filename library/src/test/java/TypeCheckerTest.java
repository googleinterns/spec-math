import static com.google.common.truth.Truth.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;

class TypeCheckerTest {

  @Test
  void testObjectIsLinkedHashMap() {
    Object map = new LinkedHashMap<String, Object>();
    assertThat(TypeChecker.isObjectMap(map)).isTrue();
  }

  @Test
  void testObjectIsNotLinkedHashMap() {
    Object map = new HashMap<>();
    Object notAMap = "notAMap";
    assertThat(TypeChecker.isObjectMap(map)).isFalse();
    assertThat(TypeChecker.isObjectMap(notAMap)).isFalse();
  }

  @Test
  void testObjectIsList() {
    Object list = new ArrayList<Object>();
    assertThat(TypeChecker.isObjectList(list)).isTrue();
  }

  @Test
  void testObjectIsNotList() {
    Object notAList = "notAList";
    assertThat(TypeChecker.isObjectList(notAList)).isFalse();
  }

  @Test
  void testObjectIsPrimitiveObject() {
    Object str = "str";
    Object integer = 5;
    Object doub = 5.0;
    Object bool = false;

    assertThat(TypeChecker.isObjectPrimitive(str)).isTrue();
    assertThat(TypeChecker.isObjectPrimitive(integer)).isTrue();
    assertThat(TypeChecker.isObjectPrimitive(doub)).isTrue();
    assertThat(TypeChecker.isObjectPrimitive(bool)).isTrue();
  }

  @Test
  void testObjectIsNotPrimitiveObject() {
    Object list = new ArrayList<Object>();
    Object map = new LinkedHashMap<String, Object>();

    assertThat(TypeChecker.isObjectPrimitive(list)).isFalse();
    assertThat(TypeChecker.isObjectPrimitive(map)).isFalse();
  }
}
