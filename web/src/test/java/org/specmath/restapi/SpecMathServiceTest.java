package org.specmath.restapi;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.specmath.library.Conflict;
import org.specmath.library.UnexpectedTypeException;
import org.specmath.library.UnionConflictException;
import org.specmath.restapi.model.MergeRequest;
import org.specmath.restapi.model.OperationResponse;
import org.specmath.restapi.model.OverlayRequest;

@ExtendWith(MockitoExtension.class)
class SpecMathServiceTest {

  @Spy
  private SpecMathService specMathService;

  @Test
  void handleMergeRequest_withoutException_hasSuccessStatus()
      throws UnionConflictException, UnexpectedTypeException, IOException {
    doReturn("").when(specMathService).performUnionOperation(any(MergeRequest.class));

    MergeRequest mergeRequest = new MergeRequest();
    mergeRequest.setSpecs(Arrays.asList("", ""));

    OperationResponse actual = specMathService.handleMergeRequest(mergeRequest);
    assertThat(actual.getStatus()).isEqualTo("success");
  }

  @Test
  void handleMergeRequest_withConflictException_hasConflictStatus()
      throws UnionConflictException, UnexpectedTypeException, IOException {
    UnionConflictException unionConflictException = new UnionConflictException(
        new ArrayList<Conflict>());

    doThrow(unionConflictException).when(specMathService)
        .performUnionOperation(any(MergeRequest.class));

    MergeRequest mergeRequest = new MergeRequest();
    mergeRequest.setSpecs(Arrays.asList("", ""));

    OperationResponse actual = specMathService.handleMergeRequest(mergeRequest);
    assertThat(actual.getStatus()).isEqualTo("conflicts");
  }

  @Test
  void handleMergeRequest_withOtherException_hasOperationErrorStatus()
      throws UnionConflictException, UnexpectedTypeException, IOException {
    doThrow(IOException.class).when(specMathService).performUnionOperation(any(MergeRequest.class));

    MergeRequest mergeRequest = new MergeRequest();
    mergeRequest.setSpecs(Arrays.asList("", ""));

    OperationResponse actual = specMathService.handleMergeRequest(mergeRequest);
    assertThat(actual.getStatus()).isEqualTo("operation error");
  }

  @Test
  void handleOverlayRequest_withoutException_hasSuccessStatus() throws UnexpectedTypeException {
    doReturn("").when(specMathService).performOverlayOperation(any(OverlayRequest.class));

    OverlayRequest overlayRequest = new OverlayRequest();
    overlayRequest.setOverlay("");
    overlayRequest.setSpec("");

    OperationResponse actual = specMathService.handleOverlayRequest(overlayRequest);
    assertThat(actual.getStatus()).isEqualTo("success");
  }

  @Test
  void handleOverlayRequest_withException_hasOperationErrorStatus() throws UnexpectedTypeException {
    doThrow(UnexpectedTypeException.class).when(specMathService)
        .performOverlayOperation(any(OverlayRequest.class));

    OverlayRequest overlayRequest = new OverlayRequest();
    overlayRequest.setOverlay("");
    overlayRequest.setSpec("");

    OperationResponse actual = specMathService.handleOverlayRequest(overlayRequest);
    assertThat(actual.getStatus()).isEqualTo("operation error");
  }
}
