package com.rainett.javagram.update.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rainett.javagram.update.service.UpdateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UpdateController.class)
class UpdateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UpdateService updateService;

    @Configuration
    static class TestConfig {
        @Bean
        public UpdateService updateService() {
            return Mockito.mock(UpdateService.class);
        }

        @Bean
        public UpdateController updateController(UpdateService updateService) {
            return new UpdateController(updateService);
        }
    }

    private static final String UPDATE_JSON = "{\"message\":{\"text\":\"Test message\"}}";

    @Test
    void onUpdateReceived_Success() throws Exception {
        doNothing().when(updateService).handleUpdate(Mockito.any());

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPDATE_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    void onUpdateReceived_Error() throws Exception {
        doThrow(new RuntimeException("Service Error"))
                .when(updateService).handleUpdate(Mockito.any());

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPDATE_JSON))
                .andExpect(status().isInternalServerError());
    }
}
