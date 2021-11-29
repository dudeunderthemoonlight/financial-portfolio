package currency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum Currency {
    USD(840),EUR(978),RUB(0);
    private final int id;
}
