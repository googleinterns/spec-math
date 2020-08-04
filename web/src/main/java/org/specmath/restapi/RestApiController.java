package org.specmath.restapi;

import javax.validation.Valid;
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
    OperationResponse ret = new OperationResponse();
    ret.setStatus("Success");
    return ResponseEntity.ok(ret);
  }
}