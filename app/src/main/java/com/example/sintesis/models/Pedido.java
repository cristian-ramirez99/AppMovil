package com.example.sintesis.models;

public class Pedido {
    private String estado;
    private String usuario;
    private String _id;

    public Pedido(String estado, String usuario, String _id) {
        this.estado = estado;
        this.usuario = usuario;
        this._id = _id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
