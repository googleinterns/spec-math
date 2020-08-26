package org.specmath.restapi;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.specmath.restapi.model.MergeRequest;
import org.specmath.restapi.model.OperationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestApiApplicationTests {

//	@Mock
//	SpecMathService specMathService;

	@Autowired
	private MockMvc mockMvc;
//
//	@InjectMocks
//	RestApiController controllerUnderTest;

//	@BeforeAll
//	public void setup() {
//
//		// this must be called for the @Mock annotations above to be processed
//		// and for the mock service to be injected into the controller under
//		// test.
//		MockitoAnnotations.initMocks(this);
//
//		this.mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();
//	}

	@Test
	void contextLoads() {
	}

	@Test
	public void mergeRequest_withCorrectBody_successStatus() throws Exception {
		String json = "{\"specs\": [\"fake: spec\", \"fake: spec\"]}";
		this.mockMvc.perform(post("/v1/operations/merge")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
		)
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"status\":\"success\"")));

	}

	@Test
	public void mergeRequest_withMissingRequiredParams_errorStatus() throws Exception {
		String json = "{}";
		this.mockMvc.perform(post("/v1/operations/merge")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
		)
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"status\":\"operation error\"")));
	}

	@Test
	public void mergeRequest_withConflicts_conflictStatus() throws Exception {
		String json = "{\"specs\": [\"key: value1\", \"key: value2\"]}";
		this.mockMvc.perform(post("/v1/operations/merge")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
		)
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"status\":\"conflicts\"")));
	}

	@Test
	public void overlayRequest_withCorrectBody_succeeds() throws Exception {
		String json = "{\"spec\": \"fake: spec\", \"overlay\": \"fake: overlay\"}";
		this.mockMvc.perform(post("/v1/operations/overlay")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
		)
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"status\":\"success\"")));

	}

	@Test
	public void overlayRequest_withMissingRequiredParams_errorStatus() throws Exception {
		String json = "{}";
		this.mockMvc.perform(post("/v1/operations/overlay")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
		)
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"status\":\"operation error\"")));
	}

}
