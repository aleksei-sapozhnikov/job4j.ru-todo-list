package todolist.filter;

import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.*;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ResponseParametersFilterTest {

    private final ServletRequest servletRequest = mock(ServletRequest.class);
    private final ServletResponse servletResponse = mock(ServletResponse.class);
    private final FilterChain filterChain = mock(FilterChain.class);
    private final FilterConfig servletConfig = mock(FilterConfig.class);

    @Test
    public void whenDoFilterThenResponseParametersSet() throws IOException, ServletException {
        var filter = new ResponseParametersFilter();
        filter.doFilter(this.servletRequest, this.servletResponse, this.filterChain);
        verify(this.servletResponse).setCharacterEncoding("UTF-8");
        verify(this.servletResponse).setContentType("text/html");
    }

    @Test
    public void whenInitThenNothingIsDone() {
        var filter = new ResponseParametersFilter();
        filter.init(this.servletConfig);
        Mockito.verifyZeroInteractions(this.servletConfig);
        filter.destroy();
    }
}