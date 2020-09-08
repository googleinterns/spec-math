package org.specmath.restapi;

import javax.validation.Valid;
import org.specmath.restapi.api.OperationsApiDelegate;
import org.specmath.restapi.model.MergeRequest;
import org.specmath.restapi.model.OperationResponse;
import org.specmath.restapi.model.OverlayRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RestApiController implements OperationsApiDelegate {

  SpecMathService specMathService = new SpecMathService();

  @Override
  public ResponseEntity<OperationResponse> overlay(
      @Valid @RequestBody OverlayRequest overlayRequest) {
    return ResponseEntity.ok(specMathService.handleOverlayRequest(overlayRequest));

  }

  @Override
  public ResponseEntity<OperationResponse> union(@Valid @RequestBody MergeRequest mergeRequest) {
    return ResponseEntity.ok(specMathService.handleMergeRequest(mergeRequest));
  }
}
