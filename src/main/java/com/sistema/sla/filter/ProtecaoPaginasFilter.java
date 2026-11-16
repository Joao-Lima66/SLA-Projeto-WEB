package com.sistema.sla.filter;

import com.sistema.sla.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {"*.xhtml"})
public class ProtecaoPaginasFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioLogado");

        boolean isLoginPage = uri.contains("login.xhtml");

        if (!isLoginPage && usuario == null) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
            return;
        }

        if (isLoginPage && usuario != null) {
            String perfil = usuario.getPerfil() != null ? usuario.getPerfil().getNome() : "";
            switch (perfil) {
                case "ALUNO":
                    res.sendRedirect(req.getContextPath() + "/aluno.xhtml");
                    return;
                case "PROFESSOR":
                    res.sendRedirect(req.getContextPath() + "/professor.xhtml");
                    return;
               case "COORDENADOR(A)":
    res.sendRedirect(req.getContextPath() + "/coordenadora.xhtml");
    return;
                default:
                    res.sendRedirect(req.getContextPath() + "/login.xhtml");
                    return;
            }
        }

        if (usuario != null) {
            String perfil = usuario.getPerfil() != null ? usuario.getPerfil().getNome() : "";
            String pagina = extrairPagina(uri);

            switch (perfil) {
                case "ALUNO":
                    if (!"aluno.xhtml".equals(pagina)) {
                        res.sendRedirect(req.getContextPath() + "/aluno.xhtml");
                        return;
                    }
                    break;
                case "PROFESSOR":
                    if (!"professor.xhtml".equals(pagina)) {
                        res.sendRedirect(req.getContextPath() + "/professor.xhtml");
                        return;
                    }
                    break;
                case "COORDENADOR(A)":
    if (!"coordenadora.xhtml".equals(pagina)) {
        res.sendRedirect(req.getContextPath() + "/coordenadora.xhtml");
        return;
    }
    break;
                default:
                    res.sendRedirect(req.getContextPath() + "/login.xhtml");
                    return;
            }
        }

        chain.doFilter(request, response);
    }

    private String extrairPagina(String uri) {
        int lastSlash = uri.lastIndexOf('/');
        if (lastSlash >= 0 && lastSlash < uri.length() - 1) {
            return uri.substring(lastSlash + 1);
        }
        return uri;
    }
}
