import java.util.LinkedHashMap;
import java.util.List;

public class TypeChecker {
  static boolean isObjectMap(Object value) {
    return value instanceof LinkedHashMap;
  }

  static boolean isObjectList(Object value){
    return value instanceof List;
  }

  static boolean isObjectPrimitive(Object value) {
    return value instanceof String
        || value instanceof Boolean
        || value instanceof Integer
        || value instanceof Double;
  }
}
