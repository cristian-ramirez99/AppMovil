package com.example.sintesis.results;

import com.example.sintesis.models.LineaPedido;

public class LineaPedidoResult {
    private LineaPedido lineaPedido[];

    public LineaPedido[] getLineaPedidos() {
        return lineaPedido;
    }

    public void setLineaPedidos(LineaPedido[] lineaPedidos) {
        this.lineaPedido = lineaPedidos;
    }
}
