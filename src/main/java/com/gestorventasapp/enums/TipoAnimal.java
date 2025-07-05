package com.gestorventasapp.enums;

public enum TipoAnimal {
	
    PERRO("Perro"),
    GATO("Gato"),
    CABALLO("Caballo");

    private final String label;

    TipoAnimal(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
