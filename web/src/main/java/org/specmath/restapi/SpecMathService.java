package org.specmath.restapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.specmath.library.Conflict;
import org.specmath.library.SpecMath;
import org.specmath.library.UnexpectedTypeException;
import org.specmath.library.UnionConflictException;
import org.specmath.library.UnionOptions;
import org.specmath.restapi.model.MergeConflict;
import org.specmath.restapi.model.MergeRequest;
import org.specmath.restapi.model.OperationResponse;
import org.specmath.restapi.model.OverlayRequest;

/**
 * Service functions which handle Spec Math related requests to the API
 */
public class SpecMathService {

  OperationResponse handleMergeRequest(MergeRequest mergeRequest) {
    OperationResponse ret = new OperationResponse();

    try {
      String resultSpec = performUnionOperation(mergeRequest);

      ret.setResult(resultSpec);
      ret.setStatus("success");

      return ret;
    } catch (UnionConflictException e) {
      List<MergeConflict> mergeConflicts = castMergeConflictObject(e);

      ret.setStatus("conflicts");
      ret.setConflicts(mergeConflicts);

      return ret;
    } catch (Exception e) {
      ret.setStatus("operation error");

      return ret;
    }
  }

  OperationResponse handleOverlayRequest(OverlayRequest overlayRequest) {
    OperationResponse ret = new OperationResponse();
    try {
      String resultSpec = applyOverlay(overlayRequest.getOverlay(), overlayRequest.getSpec());

      ret.setResult(resultSpec);
      ret.setStatus("success");

      return ret;
    } catch (UnexpectedTypeException e) {
      ret.setStatus("operation error");
      return ret;
    }
  }

  /**
   * Class function for the static SpecMath function, used for mocking purposes
   */
  String union(List<String> specs, UnionOptions unionOptions)
      throws UnionConflictException, UnexpectedTypeException, IOException {
    return SpecMath.union(specs, unionOptions);
  }

  /**
   * Class function for the static SpecMath function, used for mocking purposes
   */
  String applyOverlay(String overlay, String spec) throws UnexpectedTypeException {
    return SpecMath.applyOverlay(overlay, spec);
  }

  /**
   * Cast the conflict objects in {@code e} to {@code MergeConflict} objects
   */
  private List<MergeConflict> castMergeConflictObject(UnionConflictException e) {
    List<MergeConflict> mergeConflicts = new ArrayList<>();

    for (Conflict conflict : e.getConflicts()) {
      MergeConflict mergeConflict = new MergeConflict();
      mergeConflict.setOptions(conflict.getOptions().stream().map(Object::toString).collect(
          Collectors.toList()));
      mergeConflict.setKeypath(conflict.getKeypath());
      mergeConflicts.add(mergeConflict);
    }

    return mergeConflicts;
  }

  /**
   * Performs the union operation by creating the {@code UnionOptions} and other parameters needed
   */
  private String performUnionOperation(MergeRequest mergeRequest)
      throws IOException, UnionConflictException, UnexpectedTypeException {
    String conflictRes = castConflictResolutionsToString(mergeRequest);
    String defaults = mergeRequest.getDefaults() == null ? "" : mergeRequest.getDefaults();

    UnionOptions unionOptions = UnionOptions.builder().defaults(defaults)
        .conflictResolutions(conflictRes).build();

    List<String> specs = mergeRequest.getSpecs();

    return union(specs, unionOptions);
  }

  /**
   * Turn the incoming conflict resolutions object into a string to be used by the Spec Math
   * Library
   */
  private String castConflictResolutionsToString(MergeRequest mergeRequest)
      throws JsonProcessingException {
    if (mergeRequest.getConflictResolutions() != null) {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(mergeRequest.getConflictResolutions());
    }
    return "";
  }
}
