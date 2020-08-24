package org.specmath.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.ArrayChangeListener;
import javax.validation.Valid;
import org.specmath.library.Conflict;
import org.specmath.library.SpecMath;
import org.specmath.library.UnexpectedTypeException;
import org.specmath.library.UnionConflictException;
import org.specmath.library.UnionOptions;
import org.specmath.restapi.model.MergeConflict;
import org.specmath.restapi.model.OperationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.specmath.restapi.api.OperationsApiDelegate;
import org.specmath.restapi.model.MergeRequest;
import org.specmath.restapi.model.OverlayRequest;

@Controller
public class RestApiController implements OperationsApiDelegate {
  @Override
  public ResponseEntity<OperationResponse> overlay(@Valid @RequestBody OverlayRequest overlayRequest) {
    OperationResponse ret = new OperationResponse();
    ret.setStatus("Success");
    return ResponseEntity.ok(ret);
  }

  @Override
  public ResponseEntity<OperationResponse> union(@Valid @RequestBody MergeRequest mergeRequest) {
    try {
      OperationResponse ret = new OperationResponse();

      List<String> specs = mergeRequest.getSpecs();

      MergeConflict mergeConflict = new MergeConflict();

      ObjectMapper mapper = new ObjectMapper();

      String conflictRes = mapper
          .writeValueAsString(mergeRequest);
      String defaults = mergeRequest.getDefaults();

      UnionOptions unionOptions = UnionOptions.builder().defaults(defaults).conflictResolutions(conflictRes).build();

      String res = SpecMath.union(specs, unionOptions);

      ret.setResult(res);
      ret.setStatus("success");
      return ResponseEntity.ok(ret);

    } catch (UnionConflictException e){
      OperationResponse ret = new OperationResponse();

      ret.setStatus("conflicts");

      List<MergeConflict> mergeConflicts = new ArrayList<MergeConflict>();

      for (Conflict conflict : e.getConflicts()){
        MergeConflict currentConflict = new MergeConflict();
        currentConflict.setConflicts(conflict.getOptions().stream().map(Object::toString).collect(
            Collectors.toList()));
        currentConflict.setKeypath(conflict.getKeypath());
      }

      ret.setConflicts(mergeConflicts);

      return ResponseEntity.ok(ret);

    } catch (UnexpectedTypeException | IOException e) {
      e.printStackTrace();
      OperationResponse ret = new OperationResponse();

      ret.setStatus("operation error");
      return ResponseEntity.ok(ret);
    }
  }
}