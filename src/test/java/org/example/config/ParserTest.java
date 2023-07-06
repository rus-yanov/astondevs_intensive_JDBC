package org.example.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class ParserTest {

    private HttpServletRequest request;

    @BeforeEach
    public void beforeEach() {
        request = mock(HttpServletRequest.class);
        when(request.getPathInfo()).thenReturn("musics/1/genre");

    }

    @Test
    public void getIdTest() {
        String id = Parser.getId(request);
        assertThat(id).isEqualTo("1");

        when(request.getPathInfo()).thenReturn(null);
        String isNull = Parser.getId(request);
        assertThat(isNull).isNull();
    }

    @Test
    public void getPathVariableTest() {
        String pathVariable = Parser.getPathVariable(request);
        assertThat(pathVariable).isEqualTo("genre");

        when(request.getPathInfo()).thenReturn(null);
        String isNull = Parser.getPathVariable(request);
        assertThat(isNull).isNull();
    }
}
