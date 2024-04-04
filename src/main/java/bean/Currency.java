package bean;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Currency {
    private Integer id;
    @SerializedName("Ccy")
    private String ext;
    @SerializedName("CcyNm_RU")
    private String RU;
    @SerializedName("CcyNm_UZ")
    private String UZ;
    @SerializedName("CcyNm_EN")
    private String EN;
    private String Rate;

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", Ext='" + ext + '\'' +
                ", RU='" + RU + '\'' +
                ", UZ='" + UZ + '\'' +
                ", EN='" + EN + '\'' +
                ", Rate='" + Rate + '\'' +
                '}';
    }
}
