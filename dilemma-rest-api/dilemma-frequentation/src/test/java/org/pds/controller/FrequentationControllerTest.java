package org.pds.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.pds.model.Frequentation;
import org.pds.service.FrequentationImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Arrays;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FrequentationController.class)
public class FrequentationControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private FrequentationImplService service;

    @Test
    public void findAllFrequentationsTest() throws Exception {

        Frequentation frequentation = new Frequentation(null,"111","zara","Entree","");
        Iterable<Frequentation>  allFrequentations = Arrays.asList(frequentation);
        given(service.findAll()).willReturn(allFrequentations);
        mvc.perform(get("/api/v1/frequentations/findAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].storeName", CoreMatchers.is(frequentation.getStoreName())));
    }

    @Test
    public void findFrequentationsByIdTest()
            throws Exception {

        Frequentation frequentation = new Frequentation("1234","111","zara","Entree","");
        given(service.findById("1234")).willReturn(java.util.Optional.of(frequentation));
        mvc.perform(get("/api/v1/frequentations/1234")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void saveFrequentationTest() throws Exception {
        Frequentation frequentation = new Frequentation(null,"111","zara","Entree","");

        Mockito.when(service.save(Mockito.any(Frequentation.class))).thenReturn(frequentation);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/frequentations")
                .content(asJsonString(frequentation))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
