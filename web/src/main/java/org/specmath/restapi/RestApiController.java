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
  SpecMathService specMathService = new SpecMathService();

  @Override
  public ResponseEntity<OperationResponse> overlay(@Valid @RequestBody OverlayRequest overlayRequest) {
    return ResponseEntity.ok(specMathService.handleOverlayRequest(overlayRequest));

  }

  @Override
  public ResponseEntity<OperationResponse> union(@Valid @RequestBody MergeRequest mergeRequest) {
    return ResponseEntity.ok(specMathService.handleMergeRequest(mergeRequest));
  }
}
