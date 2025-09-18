package jdr.ms_security.Interceptors;

import jdr.ms_security.Services.ValidatorsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class SecurityInterceptor implements HandlerInterceptor { // El HandlerInterceptos es una interfaz que tenemos que sobreescribir unos metodos obligatorios
    @Autowired
    private ValidatorsService validatorService;
    /**
     * Es para cuando entra
     * **/
    @Override
    public boolean preHandle(HttpServletRequest request, // La carta
                             HttpServletResponse response, // El codigo de estado (401)
                             Object handler) // Es la instancia de la puerta
            throws Exception {
        boolean success=this.validatorService.validationRolePermission(request,request.getRequestURI(),request.getMethod());
        return success;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // Lógica a ejecutar después de que se haya manejado la solicitud por el controlador
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        // Lógica a ejecutar después de completar la solicitud, incluso después de la renderización de la vista
    }
}
