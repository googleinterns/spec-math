import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
  TreeData data;
  List<TreeNode> children = new ArrayList<TreeNode>();

  TreeNode(TreeData data) {
    this.data = data;
  }

  public TreeNode buildTreeFromArray(List<Object> data) {
    Yaml yaml = new Yaml();

    for (Object element : data) {
      if (element instanceof List) {
        this.children.add(buildTreeFromArray((List<Object>) element));
      } else {
        this.data = (TreeData) element;
      }
    }

    return this;
  }
}
