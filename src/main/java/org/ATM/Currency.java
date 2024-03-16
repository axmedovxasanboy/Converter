package org.ATM;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Currency {
    private Integer id;
    private String Code;
    private String Ccy;
    private String CcyNm_RU;
    private String CcyNm_UZ;
    private String CcyNm_UZS;
    private String CcyNm_EN;
    private String Nominal;
    private String Rate;
    private String Diff;
    private String Date;

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", Code='" + Code + '\'' +
                ", Ccy='" + Ccy + '\'' +
                ", CcyNm_RU='" + CcyNm_RU + '\'' +
                ", CcyNm_UZ='" + CcyNm_UZ + '\'' +
                ", CcyNm_UZS='" + CcyNm_UZS + '\'' +
                ", CcyNm_EN='" + CcyNm_EN + '\'' +
                ", Nominal='" + Nominal + '\'' +
                ", Rate='" + Rate + '\'' +
                ", Diff='" + Diff + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}
