package com.unipi.p15013p15120.kastropoliteiesv2;

//class pou antloume dedomena apo pinaka QUESTIONNAIRE
public class UserQuestionnaire {

    private int thalassa, vouno, pediada, istoria, en_tour, nyxt_zwh;

    public UserQuestionnaire()
    {}
    public UserQuestionnaire(int thalassa, int vouno, int pediada, int istoria, int en_tour, int nyxt_zwh)
    {
        this.thalassa = thalassa;
        this.vouno = vouno;
        this.pediada = pediada;
        this.en_tour = en_tour;
        this.istoria = istoria;
        this.nyxt_zwh = nyxt_zwh;
    }

    public int getThalassa() {
        return thalassa;
    }

    public int getVouno() {
        return vouno;
    }

    public int getPediada() {
        return pediada;
    }

    public int getIstoria() {
        return istoria;
    }

    public int getEn_tour() {
        return en_tour;
    }

    public int getNyxt_zwh() {
        return nyxt_zwh;
    }
}
