package data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SortOption {

    NAME_ASC("az"),
    NAME_DECS("za"),
    PRICE_ASC("lohi"),
    PRICE_DESC("hilo");

    @Getter
    private final String value;

}
