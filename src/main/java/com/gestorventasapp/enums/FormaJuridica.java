package com.gestorventasapp.enums;

public enum FormaJuridica {
	
    SL("S.L."),
    SA("S.A."),
    COOPERATIVA("Cooperativa"),
    AUTONOMO("Autónomo");

    private final String label;

    FormaJuridica(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
