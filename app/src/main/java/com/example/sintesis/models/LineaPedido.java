package com.example.sintesis.models;

public class LineaPedido {
    private int cantidad;
    private Producto producto;
    private String pedido;
    private String _id;


    public LineaPedido(int cantidadad, Producto producto, String pedido, String _id) {
        this.cantidad = cantidadad;
        this.producto = producto;
        this.pedido = pedido;
        this._id = _id;
    }

    public int getCantidadad() {
        return cantidad;
    }

    public void setCantidadad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}

