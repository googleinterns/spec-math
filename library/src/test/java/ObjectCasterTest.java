import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.jupiter.api.Test;

class ObjectCasterTest {
  @Test
  void testWrongTypeThrows() {
    HashMap<String, Object> badMap = new HashMap<>();

    assertThrows(ClassCastException.class, () -> ObjectCaster.castObjectToStringObjectMap(badMap));
    assertThrows(ClassCastException.class, () -> ObjectCaster.castObjectToListOfObjects(badMap));
  }

  @Test
  void testCorrectTypeDoesntThrowWhenCastingMaps() {
    LinkedHashMap<String, Object> map = new LinkedHashMap<>();

    assertDoesNotThrow(() -> ObjectCaster.castObjectToStringObjectMap(map));
  }

  @Test
  void testCorrectTypeDoesntThrowWhenCastingLists() {
    List<Object> list = new ArrayList<>();

    assertDoesNotThrow(() -> ObjectCaster.castObjectToListOfObjects(list));
  }
}
