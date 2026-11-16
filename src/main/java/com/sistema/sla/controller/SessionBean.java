package com.sistema.sla.controller;

import java.io.Serializable;

import com.sistema.sla.model.Usuario;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@Named
@SessionScoped
public class SessionBean implements Serializable {

    private Usuario usuarioLogado;

    public Usuario getUsuarioLogado() {
        if (usuarioLogado == null) {
            usuarioLogado = (Usuario) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("usuarioLogado");
        }
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().put("usuarioLogado", usuarioLogado);
    }

    public void deslogar() {
        FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().remove("usuarioLogado");
        this.usuarioLogado = null;
    }

    public boolean isLogado() {
        return getUsuarioLogado() != null;
    }

    public String getPerfilNome() {
        Usuario u = getUsuarioLogado();
        return u != null && u.getPerfil() != null ? u.getPerfil().getNome() : null;
    }
}
