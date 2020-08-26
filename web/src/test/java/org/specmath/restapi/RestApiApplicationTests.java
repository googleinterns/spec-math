package org.specmath.restapi;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RestApiApplicationTests {

  @Autowired
  private MockMvc mockMvc;

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
