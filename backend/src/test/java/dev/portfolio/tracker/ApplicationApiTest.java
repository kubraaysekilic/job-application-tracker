package dev.portfolio.tracker;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDate;
import java.util.Map;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class ApplicationApiTest {
 @Autowired MockMvc mvc;
 @Autowired ObjectMapper json;
 @Autowired ApplicationRepository repository;
 @BeforeEach void clear(){repository.deleteAll();}
 Map<String,Object> payload(String url){return Map.of("company","Example Co","position","Java Developer","url",url,"appliedOn",LocalDate.now().toString(),"status","APPLIED","notes","Portfolio test");}
 @Test void lifecycleAndStaleUpdate() throws Exception {
  String created=mvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(payload("https://example.com/jobs")))).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
  var node=json.readTree(created);long id=node.get("id").asLong();
  var change=new java.util.HashMap<>(payload("https://example.com/jobs"));change.put("status","INTERVIEW");change.put("version",0);
  mvc.perform(put("/api/applications/"+id).contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(change))).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("INTERVIEW")).andExpect(jsonPath("$.version").value(1));
  mvc.perform(put("/api/applications/"+id).contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(change))).andExpect(status().isConflict());
  mvc.perform(get("/api/applications")).andExpect(jsonPath("$[0].company").value("Example Co"));
  mvc.perform(delete("/api/applications/"+id).param("version","0")).andExpect(status().isConflict());
  mvc.perform(delete("/api/applications/"+id).param("version","1")).andExpect(status().isNoContent());
  mvc.perform(get("/api/applications")).andExpect(content().json("[]"));
 }
 @Test void rejectsUnsafeUrls() throws Exception {
  mvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(payload("javascript:alert(1)")))).andExpect(status().isBadRequest());
 }
 @Test void validatesFieldsAndDates() throws Exception {
  var fields=new java.util.HashMap<>(payload(""));fields.put("company"," ");
  mvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(fields))).andExpect(status().isBadRequest());
  fields.put("company","Company");fields.put("appliedOn",LocalDate.now().plusDays(1).toString());
  mvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(fields))).andExpect(status().isBadRequest());
 }
 @Test void missingRecordReturns404() throws Exception {
  mvc.perform(delete("/api/applications/999999").param("version","0")).andExpect(status().isNotFound());
 }
}
